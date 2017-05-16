/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Andres
 */
public class Server implements Runnable {
	
	static ServerSocket socket;
	static boolean running;
	static Listener l;
	static boolean[] reqInfo;
	public static int[] wins;
	public static int rounds;
	
    public Server() {
		System.out.println("Creating server...");
		try {
			socket = new ServerSocket(bomberman.Bomberman.PORT);
			socket.setSoTimeout(100);
			l = new Listener(this, socket);
			running = true;
			wins = new int[] {0,0,0,0};
			rounds = 0;
			reqInfo = new boolean[4];
			for (int i = 0; i < reqInfo.length; i++) {
				reqInfo[i] = false;
			}
		} catch(Exception e) { }
    }
	
	/**
	 * Returns host address.
	 * @return 
	 */
	public static String getAddress() {
		String a = "0.0.0.0";
		try {
			a = java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) { }
		return a;
	}
	
	/**
	 * Listener method to add a player connection to server.
	 * @param newSocket 
	 */
	public void newConnection(Socket newSocket) {
		try {
			new java.io.PrintWriter(newSocket.getOutputStream(), true).println(Handler.addPlayer(newSocket));
			if (!Handler.spaceAvailable()) {
				l.stopListening();
			}
		} catch (Exception e) { }
	}

	/**
	 * Mercilessly stops the server.
	 */
	public void stopServer() {
		System.out.println("Stopping server...");
		running = false;
		try {
			socket.close();
		} catch (Exception e) { }
		l.stopListening();
		Handler.initPlayers();
	}
	
	/**
	 * Server thread.
	 * Sends message to clients, and receives clients' messages.
	 */
	@Override
	public void run() {
		while (running) {
			boolean changesReceived = false;
			String changesToClients = Handler.serverSendChanges();
			String[] changesFromClients  = new String[Handler.NUM_PLAYERS];
			for (int i = 1; i < Handler.NUM_PLAYERS; i++) {
				if (Handler.players[i].isEnabled()) {
					if (reqInfo[i]) {
						reqInfo[i] = false;
//						System.out.println("Sending scene init");
						Handler.players[i].getOut().println("!"+Handler.currentScene.sceneInit());
					} else {
//						System.out.println("Sending "+changesToClients);
						Handler.players[i].getOut().println(changesToClients);
					}
					
					try {
						changesFromClients[i] = Handler.players[i].getIn().readLine();
						
						if (changesFromClients[i] == null) {
							changesFromClients[i] = "";
							System.out.println("Forcing player "+i+" disconnection.");
							Handler.checkGameWin();
							Handler.setGhost(i);
							if (Handler.spaceAvailable()) {
								l.listen();
							}
						} else if (changesFromClients[i].contains("?")) {
							changesFromClients[i] = "";
//							System.out.println("Client "+i+" requested update.");
							reqInfo[i] = true;
						}
						
						changesReceived = true;
						Handler.players[i].timeOuts = 0;
					} catch (Exception e) {
						Handler.players[i].timeOuts++;
						if (Handler.players[i].timeOuts > 5) {
							System.out.println("Player "+i+" timed out!");
							System.out.println("Player "+i+" disconnected.");
							Handler.setGhost(i);
							Handler.checkGameWin();
							if (Handler.spaceAvailable()) {
								l.listen();
							}
						}
					}
				}
			}
//			if (!Handler.players[0].isEnabled()) {
//				running = false;
//				l.stopListening();
//				Handler.server = null;
//				System.out.println("Host lost, deleting server.");
//			}
			if (changesReceived) {
				Handler.serverReceiveChanges(changesFromClients);
			}
			try {
				Thread.sleep(50);
			} catch (Exception e) { }
			
		}
	}
	
}

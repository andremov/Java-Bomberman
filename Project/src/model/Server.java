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
	
    public Server() {
		System.out.println("Creating server...");
		try {
			socket = new ServerSocket(bomberman.Bomberman.PORT);
			socket.setSoTimeout(100);
			l = new Listener(this, socket);
			running = true;
		} catch(Exception e) { }
    }
	
	public static String getAddress() {
		String a = "0.0.0.0";
		try {
			a = java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) { }
		return a;
	}
	
	public static void dispose() {
		running = false;
		try {
			socket.close();
		} catch (Exception e) { }
	}
	
	public void newConnection(Socket newSocket) {
		try {
			String infoToSend = Handler.addPlayer(newSocket)+";"+getAddress();
			new java.io.PrintWriter(newSocket.getOutputStream(), true).println(infoToSend);
			if (!Handler.spaceAvailable()) {
				l.stopListening();
			}
		} catch (Exception e) { }
	}

	@Override
	public void run() {
		while (running) {
			boolean changesReceived = false;
			String changesToClients = Handler.getChangesServer();
			for (int i = 1; i < Handler.players.length; i++) {
				String changesFromClients = "";
				if (Handler.players[i].isEnabled()) {
					Handler.players[i].getOut().println(changesToClients);
					
					try {
						changesFromClients = Handler.players[i].getIn().readLine();
						if (changesFromClients == null) {
							changesFromClients = "";
							System.out.println("Forcing player "+i+" disconnection.");
							Handler.removePlayer(i);
						}
						
						changesReceived = true;
						Handler.players[i].timeOuts = 0;
					} catch (Exception e) {
						Handler.players[i].timeOuts++;
						if (Handler.players[i].timeOuts > 5) {
							System.out.println("Player "+i+" timed out!");
							System.out.println("Player "+i+" disconnected.");
							Handler.removePlayer(i);
						}
					}
				}
				Handler.saveClientChange(i, changesFromClients);
			}
			if (!Handler.players[0].isEnabled()) {
				running = false;
				l.stopListening();
				Handler.server = null;
				System.out.println("Host lost, deleting server.");
			}
			if (changesReceived) {
				Handler.saveClientChange(0, "");
				Handler.applyClientChanges();
			}
			try {
				Thread.sleep(50);
			} catch (Exception e) { }
			
		}
	}
	
}

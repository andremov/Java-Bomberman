/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import view.Window;

/**
 *
 * @author Andres
 */
public class Client implements Runnable {
	
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
	private int timeOuts;
	
	private static Window gameWindow;
	
	public Client() {
		disconnect();
		timeOuts = 0;
		gameWindow = new Window();
		gameWindow.startCanvas();
	}
	
	public void host() {
		connect("localhost");
		if (socket == null) {
			Handler.server = new Server();
			Handler.clientChanges = new String[4];
			for (int i = 0; i < Handler.clientChanges.length; i++) {
				Handler.clientChanges[i] = "";
			}
			connect("localhost");
			new Thread(Handler.server).start();
		}
	}
	
	public void connect(String serverAddress) {
		((scenes.Connection) model.Handler.currentScene).setMessage(scenes.Connection.MSG_CONNECTING);
		System.out.println("Connecting to "+serverAddress);
		
		try {
			Thread.sleep(10);
		} catch (Exception e1) { }
		
		try {
			socket = new Socket(serverAddress, bomberman.Bomberman.PORT);
			socket.setSoTimeout(100);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			String infoReceived = in.readLine();
			Handler.playerID = Integer.parseInt(infoReceived.split(";")[0]);
			Handler.address = infoReceived.split(";")[1];
			
			System.out.println("Connected successfully.");
			
			Handler.currentScene = new scenes.Lobby();
			new Thread(this).start();
		} catch (Exception e2) {
			socket = null;
			in = null;
			out = null;
			
			((scenes.Connection) model.Handler.currentScene).setMessage(scenes.Connection.MSG_FAILED);
		}
	}
	
	public void disconnect() {
		socket = null;
		in = null;
		out = null;
		Handler.currentScene = new scenes.Connection();
	}

	@Override
	public void run() {
		while (socket != null && Handler.server == null) {
			try {
				String changesFromServer = in.readLine();
				Handler.applyChanges(changesFromServer);
				
				timeOuts = 0;
			} catch (Exception e1) {
				timeOuts++;
				if (timeOuts > 10) {
					timeOuts = 0;
					System.out.println("Connection timed out.");
					disconnect();
				}
			}
			try {
				String changesToServer = Handler.getChanges();
				out.println(changesToServer);
			} catch (Exception e2) { }
				
			
			try {
				Thread.sleep(50);
			} catch (Exception e) { }
		}
	}
	
}

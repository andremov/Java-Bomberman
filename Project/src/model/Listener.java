/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andres
 */
public class Listener implements Runnable {
	
	private boolean listening;
	private ServerSocket socket;
	private Server owner;
	
	public Listener(Server owner, ServerSocket socket) {
		this.owner = owner;
		this.socket = socket;
		this.listen();
	}
	
	/**
	 * Stops listener thread.
	 */
	public void stopListening() {
		listening = false;
	}
	
	/**
	 * Starts listener thread.
	 */
	public void listen() {
		if (!listening) {
			listening = true;
			new Thread(this).start();
		}
	}
	
	/**
	 * Sends socket to server.
	 * @param connection 
	 */
	private void sendSocket(Socket connection) {
		owner.newConnection(connection);
	}

	/**
	 * Listener thread.
	 * Checks for 100ms for a connection, if succeeds, sends socket, if fails, tries again.
	 */
	@Override
	public void run() {
		while(listening) {
			Socket newSocket = null;
			try {
				newSocket = socket.accept();
				if (newSocket != null) {
					System.out.println("New connection.");
					try {
						newSocket.setSoTimeout(100);
						sendSocket(newSocket);
					} catch (IOException ex) { }
				}
			} catch (Exception e) { }
		}
	}
	
}

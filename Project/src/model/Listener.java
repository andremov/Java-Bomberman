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
	
	public void stopListening() {
		listening = false;
	}
	
	public void listen() {
		listening = true;
		new Thread(this).start();
	}
	
	private void sendSocket(Socket connection) {
		owner.newConnection(connection);
	}

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

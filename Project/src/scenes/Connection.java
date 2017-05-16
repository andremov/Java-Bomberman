/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Andres
 */
public class Connection extends Scene {

	public static final int SCENE_ID = 0;
	
	public static int CODE_DEL = -1;
	// NUMBERS FROM 0 TO 9 RESERVED
	public static int CODE_PERIOD = 10;
	public static int CODE_ENTER = 11;
	public static int CODE_SWITCH = 12;
	
	public static int MSG_ENTER = 0;
	public static int MSG_CONNECTING = 1;
	public static int MSG_FAILED = 2;
	
	private String serverAddress;
	private int selectedAction;
	private int currentMsg;

	public Connection() {
		super("Connection", true);
		this.selectedAction = 0;
		this.serverAddress = "";
		this.currentMsg = MSG_ENTER;
	}
	
	/**
	 * Returns scene ID.
	 * @return 
	 */
	@Override
	public int getID() {
		return SCENE_ID;
	}
	
	/**
	 * Returns all necessary values for a client to duplicate this scene.
	 * @return 
	 */
	@Override
	public String sceneInit() {
		return SCENE_ID+"";
	}
	
	/**
	 * Receives an action code and responds accordingly.
	 * @param actionCode 
	 */
	@Override
	public void receiveKeyAction(int actionCode) {
		if (actionCode == CODE_DEL) {
			if (!serverAddress.isEmpty()) {
				serverAddress = serverAddress.substring(0,serverAddress.length()-1);
			}
		} else if (actionCode == CODE_PERIOD) {
			serverAddress = serverAddress + ".";
		} else if (actionCode == CODE_SWITCH) {
			selectedAction = 1 - selectedAction;
		} else if (actionCode == CODE_INVALID) {
		} else if (actionCode == CODE_ENTER) {
			if (selectedAction == 1) {
				model.Handler.client.host();
			} else if (!serverAddress.isEmpty()) {
				model.Handler.client.connect(serverAddress);
			}
		} else {
			serverAddress = serverAddress + actionCode;
		}
	}

	/**
	 * Sets the status message to a message code.
	 */
	public void setMessage(int message) {
		this.currentMsg = message;
	}
	
	/**
	 * Returns this scene's display image.
	 * @return
	 * @throws IOException 
	 */
	@Override
	public BufferedImage getDisplay() throws IOException {
		int size = bomberman.Bomberman.SCREEN_SIZE;
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		
		// BACKGROUND
		g.setColor(color(205.7, 18.3, 90.2));
		g.fillRect(0, 0, size, size);
		
		
		// TITLE
		g.setColor(Color.black);
		
		Font font = new Font("Arial",Font.BOLD,40);
		g.setFont(font);
		String s = "Connection Lobby";
		java.awt.FontMetrics metrics = g.getFontMetrics(font);
		int fontX = (size - metrics.stringWidth(s)) / 2;
		
		g.drawString(s, fontX, 50);
		
		
		// SERVER ADDRESS TITLE
		font = new Font("Arial",Font.BOLD,17);
		g.setFont(font);
		s = "Server Address:";
		metrics = g.getFontMetrics(font);
		fontX = (size - metrics.stringWidth(s)) / 2;
		g.drawString(s, fontX, 100);
		
		
		// SERVER ADDRESS
		font = new Font("Arial",Font.BOLD,30);
		g.setFont(font);
		s = "> " + serverAddress + " <";
		metrics = g.getFontMetrics(font);
		fontX = (size - metrics.stringWidth(s)) / 2;
		g.drawString(s, fontX, 140);
		
		// MESSAGE DISPLAY
		String[] messages = {
			"Ingrese una direccion de servidor.",
			"Conectando a servidor...",
			"Error conectando a servidor."
		};
		font = new Font("Arial",Font.BOLD,20);
		g.setFont(font);
		s = messages[currentMsg];
		metrics = g.getFontMetrics(font);
		fontX = (size - metrics.stringWidth(s)) / 2;
		
		g.drawString(s, fontX, 200);
		
		
		
		// BUTTONS
		String[] labels = { "Connect", "Host" };
		int[] y = { 250, 340 };
		int buttonWidth = 200;
		int buttonHeight = 50;
		int strokeWidth = 2;
		
		font = new Font("Arial",Font.BOLD,30);
		g.setFont(font);
		metrics = g.getFontMetrics(font);
		
		for (int i = 0; i < 2; i++) {
			int buttonX = (size-buttonWidth)/2;
			int buttonY = y[i];
			
			g.setColor(Color.black);
			g.fillRect(buttonX,buttonY,buttonWidth,buttonHeight);
			
			s = labels[i];
			
			if (i == selectedAction) {
				g.setColor(color(210, 78.8, 90.6));
				s = "> " + s + " <";
			} else {
				g.setColor(color(188.2, 27.7, 93.3));
			}
			g.fillRect(buttonX+strokeWidth,buttonY+strokeWidth,buttonWidth-(strokeWidth*2),buttonHeight-(strokeWidth*2));
			
			fontX = (size - metrics.stringWidth(s)) / 2;
			int fontY = ((buttonHeight - metrics.getHeight()) / 2) + metrics.getAscent();
			g.setColor(Color.black);
			g.drawString(s, fontX, y[i]+fontY);
		}
		
		
		// INSTRUCTIONS
		font = new Font("Arial",Font.PLAIN,16);
		g.setFont(font);
		metrics = g.getFontMetrics(font);
		s = "Digite la direccion del servidor al que desea conectarse, o "
				+ "presione las flechas para cambiar a 'host' y presione 'Enter'"
				+ " para aceptar.";
		
		int maxLineWidth = 490;
		int max = (int) Math.ceil(metrics.stringWidth(s)/maxLineWidth);
		int startIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < max+1; i++) {
			String line = s.substring(startIndex,endIndex);
			while (metrics.stringWidth(line) < maxLineWidth && endIndex < s.length()) {
				endIndex = endIndex+1;
				line = s.substring(startIndex, endIndex);
			}
			line = s.substring(startIndex, endIndex);

			fontX = (size - metrics.stringWidth(line)) / 2;

			g.drawString(line, fontX, 560+(20*i));
			startIndex = endIndex;
		}
		
		
		return img;
	}
	
}

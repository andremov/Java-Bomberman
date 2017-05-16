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
import model.Handler;

/**
 *
 * @author Andres
 */
public class Lobby extends Scene {

	public static final int SCENE_ID = 1;
	
	public static int CODE_SWITCH = 0;
	public static int CODE_ENTER = 1;
	
	private String serverAddress;
	private int[] wins;
	private int roundsPlayed;
	private boolean[] ready;
			
	public Lobby(String serverAddress, int[] wins, int roundsPlayed) {
		super("Lobby", true);
		this.serverAddress = serverAddress;
		this.wins = wins;
		this.roundsPlayed = roundsPlayed;
		this.ready = new boolean[4];
		for (int i = 0; i < ready.length; i++) {
			ready[i] = false;
		}
		for (int i = 0; i < Handler.NUM_PLAYERS; i++) {
			Handler.players[i].defaultAnimation();
		}
	}
	
	/**
	 * Returns the scene ID.
	 * @return 
	 */
	@Override
	public int getID() {
		return SCENE_ID;
	}

	/**
	 * Returns the string of information for clients to create this scene.
	 * @return 
	 */
	@Override
	public String sceneInit() {
		String winString = "";
		for (int i = 0; i < wins.length; i++) {
			winString = winString+wins[i]+",";
		}
		return SCENE_ID+":"+serverAddress+";"+winString+";"+roundsPlayed+";";
	}

	/**
	 * Sets the given index ready value to the given value.
	 * @param index
	 * @param value 
	 */
	public void setReady(int index, boolean value) {
		ready[index] = value;
		checkReady();
	}
	
	/**
	 * Checks if all players are ready, starts the game.
	 */
	private void checkReady() {
		boolean go = true;
		for (int i = 0; i < ready.length; i++) {
			if (!ready[i] && Handler.players[i].isEnabled()) {
				// not ready, is enabled
				go = false;
			}
		}
		if (go) {
			Handler.engageGame(wins,roundsPlayed);
		}
	}
	
	/**
	 * Returns the ready status of the given index.
	 * @param index
	 * @return 
	 */
	public boolean getReady(int index) {
		return ready[index];
	}
	
	/**
	 * Receives an action code, and responds accordingly.
	 * @param actionCode 
	 */
	@Override
	public void receiveKeyAction(int actionCode) {
		if (actionCode == CODE_SWITCH) {
			if (!ready[Handler.playerID]) {
				Handler.players[Handler.playerID].setColor(Handler.nextColor(Handler.players[Handler.playerID].getColor()));
			}
		} else if (actionCode == CODE_ENTER) {
			setReady(Handler.playerID, !ready[Handler.playerID]);
		}
	}
	
	/**
	 * Return scene display.
	 * @return
	 * @throws IOException 
	 */
	@Override
	public BufferedImage getDisplay() throws IOException {
		int size = bomberman.Bomberman.SCREEN_SIZE;
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		
		// BACKGROUND
		g.setColor(color(6.4, 63, 99.6));
		g.fillRect(0, 0, size, size);
		
		
		// TITLE
		g.setColor(Color.black);
		
		Font font = new Font("Arial",Font.BOLD,40);
		g.setFont(font);
		String s = "Lobby";
		java.awt.FontMetrics metrics = g.getFontMetrics(font);
		int fontX = (size - metrics.stringWidth(s)) / 2;
		g.drawString(s,fontX,50);
		
		
		// ADDRESS
		font = new Font("Arial",Font.BOLD,20);
		g.setFont(font);
		s = serverAddress;
		metrics = g.getFontMetrics(font);
		fontX = (size - metrics.stringWidth(s)) / 2;
		g.drawString(s,fontX,100);
		
		
		// ROUNDS PLAYED
		font = new Font("Arial",Font.BOLD,20);
		g.setFont(font);
		s = roundsPlayed + " rondas jugadas";
		metrics = g.getFontMetrics(font);
		fontX = (size - metrics.stringWidth(s)) / 2;
		g.drawString(s,fontX,220);
		
		// PLAYERS
		
		font = new Font("Arial",Font.BOLD,40);
		g.setFont(font);
		metrics = g.getFontMetrics(font);
			
		for (int i = 0; i < model.Handler.NUM_PLAYERS; i++) {
			model.Player thisPlayer = model.Handler.players[i];
			int y = 250;
			int section = size/5;
			int startX = size/5*i;
			g.drawImage(thisPlayer.getDisplay(),startX+section-32,y,64,100,null);
			
			
			s = wins[i]+"";
			fontX = (((size/5) - metrics.stringWidth(s)) / 2);
			BufferedImage img = data.NIC.star;
			g.drawString(s, startX + fontX + (section/4), y+110+(img.getHeight()/4));
			g.drawImage(img, startX + (section/2) + (img.getWidth()/4), y+120, (img.getWidth()/4), (img.getHeight()/4), null);
			
			img = data.NIC.cross;
			if (ready[i]) {
				img = data.NIC.check;
			}
			g.drawImage(img, startX+section-(img.getWidth()/8), y+200, (img.getWidth()/4),(img.getHeight()/4),null);
		}
		
		// INSTRUCTIONS
		font = new Font("Arial",Font.PLAIN,16);
		g.setFont(font);
		metrics = g.getFontMetrics(font);
		s = "Presione las flechas direccionales para cambiar de color, y la tecla"
				+ " 'Enter' para marcarse como 'Listo'.";
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
		
		return image;
	}
	
}

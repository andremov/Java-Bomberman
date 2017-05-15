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

	public static int CODE_SWITCH = 0;
	
	public Lobby() {
		super("Lobby", true);
	}

	@Override
	public void receiveKeyAction(int actionCode) {
		if (actionCode == CODE_SWITCH) {
			Handler.players[Handler.playerID].setColor(Handler.nextColor(Handler.players[Handler.playerID].getColor()));
		}
	}

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
		String s = model.Handler.address;
		java.awt.FontMetrics metrics = g.getFontMetrics(font);
		int fontX = (size - metrics.stringWidth(s)) / 2;
		g.drawString(s,fontX,150);
		
		for (int i = 0; i < model.Handler.players.length; i++) {
			model.Player thisPlayer = model.Handler.players[i];
			int y = 200;
			int x = size/5*(i+1);
			g.drawImage(thisPlayer.getDisplay(),x-32,y,64,100,null);
		}
		
		return image;
	}
	
}

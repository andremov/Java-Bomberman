/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.KeyHandler;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import model.Client;

/**
 *
 * @author Andres
 */
public class Display extends Canvas implements Runnable {

	private BufferedImage lastFrame;
			
	public Display() {
		int size = bomberman.Bomberman.SCREEN_SIZE;
		lastFrame = new BufferedImage(size, size,BufferedImage.TYPE_INT_ARGB);
		addKeyListener(new KeyHandler());
	}

	@Override
	public void run() {
		createBufferStrategy(2);
		while (true){
            Graphics g = getBufferStrategy().getDrawGraphics();
			
			try {
				lastFrame = model.Handler.getDisplay();
			}catch(Exception e) {
				System.err.println("Error display.");
			}
			
			g.drawImage (lastFrame,0,0,getWidth(),getHeight(),null);
                        
			getBufferStrategy().show();
			
			try {
				Thread.sleep(50);
			} catch (Exception e) { }
		}
	}
	
	
	
}

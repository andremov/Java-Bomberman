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
import model.Handler;

/**
 *
 * @author Andres
 */
public class Display extends Canvas implements Runnable {

	private Handler h;
	private BufferedImage lastFrame;
			
	public Display(Handler h) {
		this.h = h;
		lastFrame = new BufferedImage(Handler.SCREEN_SIZE,Handler.SCREEN_SIZE,BufferedImage.TYPE_INT_ARGB);
		addKeyListener(new KeyHandler());
	}

	@Override
	public void run() {
		createBufferStrategy(2);
		while (true){
            Graphics g = getBufferStrategy().getDrawGraphics();
			
			try {
				lastFrame = Handler.currentScene.getDisplay();
			}catch(Exception e) { }
			
			g.drawImage (lastFrame,0,0,getWidth(),getHeight(),null);
                        
			getBufferStrategy().show();
			
			try {
				Thread.sleep(50);
			} catch (Exception e) { }
		}
	}
	
	
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.KeyHandler;
import java.awt.Canvas;
import java.awt.Graphics;
import model.Handler;

/**
 *
 * @author Andres
 */
public class Display extends Canvas implements Runnable {

	private Handler h;
	
	public Display(Handler h) {
		this.h = h;
		addKeyListener(new KeyHandler());
	}

	@Override
	public void paint(Graphics g) {
		try{
			g.drawImage (Handler.currentScene.getDisplay(),0,0,getWidth(),getHeight(),null);
		} catch (Exception e) {
			
		}
	}

	@Override
	public void run() {
		createBufferStrategy(2);
		while (true){
			repaint();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				
			}
		}
	}
	
	
	
}

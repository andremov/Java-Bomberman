/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import control.KeyHandler;
import java.awt.image.BufferedImage;
import java.io.IOException;
import model.Handler;

/**
 *
 * @author Andres
 */
public class Game extends Scene {

	public Game(Handler main, boolean full) {
		super(main, "Game", full);
	}

	@Override
	public void receiveKeyAction(int action, int state) {
		if (action == KeyHandler.ACTION_A) {
			accept();
		}
	}

	@Override
	protected void accept() {
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void cancel() {
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void start() {
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected void move(int dir) {
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public BufferedImage getDisplay() throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}

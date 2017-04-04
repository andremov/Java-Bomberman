/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import control.KeyHandler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import model.Handler;
import model.Map;

/**
 *
 * @author Andres
 */
public class Game extends Scene {

	Map gameMap;
	
	public Game(Handler main, boolean full) {
		super(main, "Game", full);
		
		gameMap = new Map();
		for (int i = 0; i < Handler.numPlayers; i++) {
			Handler.players[i].setXY(20,20);
		}
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
		int mapSize = Handler.TILE_SIZE*data.NIC.SIZE_MAP;
		BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.drawImage(gameMap.getDisplay(), 0, 0, mapSize, mapSize, null);
		
		for (int i = 0; i < Handler.numPlayers; i++) {
			g.drawImage(Handler.players[i].getDisplay(),Handler.players[i].getX(),Handler.players[i].getY(),null);
		}
		
		return image;
	}
	
}

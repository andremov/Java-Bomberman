/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package model;

import scenes.Scene;
import view.Window;

/**
 *
 * @author Andres
 */
public class Handler {

	public static final int SCREEN_SIZE = 600;
	public static final int TILE_SIZE = 16;
	public static Scene currentScene;

	public static Player[] players;
	private Window gameWindow;
	
	/**
	 * Build game.
	 */
	public Handler() {
		players = new Player[4];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player();
		}
		gameWindow = new Window(this);
		gameWindow.startCanvas();
		addPlayer(0,1);
		addPlayer(1,2);
		
		currentScene = (new scenes.Game(this, true));
	}
	
	private void addPlayer(int index, int color) {
		players[index].setColor(color);
		players[index].setEnabled(true);
	}
	
	private void removePlayer(int index) {
		players[index] = null;
		players[index] = new Player();
	}
        
	public static scenes.Game getGame() {
		scenes.Game g = null;
		if (currentScene instanceof scenes.Game) {
			g = (scenes.Game)currentScene;
		}
		return g;
	}
	
}

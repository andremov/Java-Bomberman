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

	// <editor-fold defaultstate="collapsed" desc="Attributes">
		/**
		 * Window size in X dimension.
		 */
		public static int SCREEN_SIZE;
		public static int TILE_SIZE = 16;
		/**
		 * Current battle handler.
		 */
		//		public Combat currentBattle;
		/**
		 * List of states.
		 */
		public static Scene currentScene;
	// </editor-fold>
	
	public static Player[] players;
	public static int numPlayers;
	private Window gameWindow;
	
	/**
	 * Build game.
	 * @float resize Resize factor of window.
	 */
	public Handler() {
		SCREEN_SIZE = 600;
		numPlayers = 1;
		players = new Player[4];
		players[0] = new Player(1);
		gameWindow = new Window(this);
		gameWindow.startCanvas();
		
		currentScene = (new scenes.Game(this, true));
	}
	
	/*
	public void canContinue() {
		gw.setVisible(true);
		gw.startCanvasThread();
	}
	
	public void clearStates(String limit) {
		boolean pullOut = true;
		while (pullOut && currentScene.get(currentScene.size() - 1).getName().compareTo(limit) != 0) {
			currentScene.get(currentScene.size() - 1).dispose();
			pullOut = !currentScene.isEmpty();
		}
	}
	
	public pokemonviolet.model.Player getPlayer() {
		pokemonviolet.model.Player player = null;
		boolean found = false;
		int counter = 0;
		while (!found && counter < currentScene.size()) {
			if (currentScene.get(counter).getName().compareTo("GAME") == 0) {
				player = ((pokemonviolet.scenes.Game) currentScene.get(counter)).getPlayer();
				found = true;
			} else {
				counter = counter + 1;
			}
		}
		
		return player;
	}
	*/
	
}

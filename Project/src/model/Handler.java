/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package model;

import java.util.ArrayList;
import scenes.Scene;
import view.Window;

/**
 *
 * @author Andres
 */
public class Handler {

	// <editor-fold defaultstate="collapsed" desc="Attributes">
		// <editor-fold defaultstate="collapsed" desc="Statics">
			/**
			 * Canvas size in X dimension.
			 */
			public static int SCREEN_SIZE_X;
			/**
			 * Canvas size in Y dimension.
			 */
			public static int SCREEN_SIZE_Y;
			
			public static int BASE_SCREEN_SIZE_X = 240;
			public static int BASE_SCREEN_SIZE_Y = 160;
		//</editor-fold>

		/**
		 * Current battle handler.
		 */
		//		public Combat currentBattle;
		/**
		 * List of states.
		 */
		public static ArrayList<Scene> gameState;
		private GameWindow gw;
	// </editor-fold>

	/**
	 * Build game.
	 * @float resize Resize factor of window.
	 */
	public Handler() {
		gameState = new ArrayList<Scene>();
		
		SCREEN_SIZE_X = (int) (BASE_SCREEN_SIZE_X * RESIZE);
		SCREEN_SIZE_Y = (int) (BASE_SCREEN_SIZE_Y * RESIZE);
		
		gw = new GameWindow(SCREEN_SIZE_X, SCREEN_SIZE_Y);

		
		gameState.add(new pokemonviolet.scenes.Title(this, true));
	}

	public void canContinue() {
		gw.setVisible(true);
		gw.startCanvasThread();
	}
	
	public void clearStates(String limit) {
		boolean pullOut = true;
		while (pullOut && gameState.get(gameState.size() - 1).getName().compareTo(limit) != 0) {
			gameState.get(gameState.size() - 1).dispose();
			pullOut = !gameState.isEmpty();
		}
	}
	
	public pokemonviolet.model.Player getPlayer() {
		pokemonviolet.model.Player player = null;
		boolean found = false;
		int counter = 0;
		while (!found && counter < gameState.size()) {
			if (gameState.get(counter).getName().compareTo("GAME") == 0) {
				player = ((pokemonviolet.scenes.Game) gameState.get(counter)).getPlayer();
				found = true;
			} else {
				counter = counter + 1;
			}
		}
		
		return player;
	}
	
	
}

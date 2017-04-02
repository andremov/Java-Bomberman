/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Andres
 */
public abstract class Parser {

	public static BufferedImage displayImage() {
		BufferedImage display = new BufferedImage(Handler.SCREEN_SIZE_X, Handler.SCREEN_SIZE_Y, BufferedImage.TYPE_INT_ARGB);
		Graphics g = display.getGraphics();

		int counter = 0;
		boolean done = false;
		
		if (!Handler.gameState.isEmpty()) {
			while (!done) {
				counter = counter + 1;
				Scene thisScene = Handler.gameState.get(Handler.gameState.size() - counter);

				done = thisScene.isFull();
			}
			
			if (Handler.gameState.get(Handler.gameState.size() - counter).getName().compareTo("GAME") == 0) {
				for (int i = 0; i < (Handler.SCREEN_SIZE_X/32) + 2; i++) {
					for (int j = 0; j < (Handler.SCREEN_SIZE_Y/32) + 2; j++) {
						g.drawImage(GameDisplay.water, i*32, j*32, null);
					}
				}
			}
			
			for (int i = Handler.gameState.size() - counter; i < Handler.gameState.size(); i++) {
				try {
					g.drawImage(Handler.gameState.get(i).getDisplay(), 0, 0, null);
				} catch (IOException ex) {
				}
			}
		}

		return display;
	}

}

/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Handler;

/**
 *
 * @author Andres
 */
public abstract class Scene {

	protected final Handler main;
	private final String name;
	private final boolean full;

	public Scene(Handler main, String name, boolean full) {
		this.main = main;
		this.name = name;
		this.full = full;
	}

	public abstract void receiveKeyAction(int action, int state);

	protected abstract void accept();

	protected abstract void cancel();

	protected abstract void start();

	public void dispose() {
		main.gameState.remove(main.gameState.size() - 1);
	}

	protected abstract void move(int dir);

	public abstract BufferedImage getDisplay() throws IOException;

	/**
	 * Generates a window with the given theme and given dimensions.
	 *
	 * @param theme Window theming.
	 * @param dimX Window dimension in X.
	 * @param dimY Window dimension in Y.
	 * @return Window Buffered Image.
	 * @throws IOException
	 */
	protected BufferedImage genWindow(int theme, double dimX, double dimY) throws IOException {
		int dimXint = (int) dimX;
		int dimYint = (int) dimY;
		
		BufferedImage window = new BufferedImage(dimXint, dimYint, BufferedImage.TYPE_INT_ARGB);
		Graphics g = window.getGraphics();

		int pieceDim = 7;

		if (dimXint < pieceDim * 2) {
			dimXint = pieceDim * 2;
		}

		if (dimYint < pieceDim * 2) {
			dimYint = pieceDim * 2;
		}

		int numTilesX = dimXint / pieceDim, numTilesY = dimYint / pieceDim;

		for (int i = 0; i < numTilesY + 1; i++) {
			for (int j = 0; j < numTilesX + 1; j++) {
				int pieceX = 0, pieceY = 0;
				int thisY = i * pieceDim, thisX = j * pieceDim;

				if (i > 0) {
					pieceY = pieceY + 1;
				}

				if (i == numTilesY) {
					pieceY = pieceY + 1;
					thisY = dimYint - pieceDim;
				}

				if (j > 0) {
					pieceX = pieceX + 1;
				}

				if (j == numTilesX) {
					pieceX = pieceX + 1;
					thisX = dimXint - pieceDim;
				}

				g.drawImage(ImageIO.read(new File("assets/windows/" + theme + ".png")).getSubimage(7 * pieceX, 7 * pieceY, 7, 7), thisX, thisY, pieceDim, pieceDim, null);
			}
		}

		return window;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the full
	 */
	public boolean isFull() {
		return full;
	}
	
	protected String[] genMultilineText(String originalText, int charsInLine) {
		java.util.ArrayList<String> listResult = new java.util.ArrayList<String>();
		
		for (int i = 0; i < (originalText.length() / charsInLine) + 1; i++) {
			String prefix = "", suffix = "";

			int thisLineFirstChar = i * charsInLine;
			int thisLinePrevChar = thisLineFirstChar - 1;
			int thisLineLastChar = ((i + 1) * charsInLine) - 2;
			int thisLineNextChar = thisLineLastChar + 1;

			if (thisLineFirstChar != 0){
				if (originalText.charAt(thisLinePrevChar) != ' ') {
					prefix = "" + originalText.charAt(thisLinePrevChar);
				}
			}

			if (thisLineNextChar < originalText.length()) {
				if (originalText.charAt(thisLineNextChar) != ' ' && originalText.charAt(thisLineLastChar) != ' ') {
					suffix = "-";
				}
			} else {
				thisLineLastChar = originalText.length() - 1;
			}
			listResult.add(prefix + originalText.substring(thisLineFirstChar, thisLineLastChar+1) + suffix);
		}
		
		String[] result = new String[listResult.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = listResult.get(i);
		}
		
		return result;
	}

}

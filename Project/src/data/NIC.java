/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Andres
 */
public abstract class NIC {

	private static BufferedImage tiles;
	private static BufferedImage powerups;
	private static BufferedImage bombs;
	private static BufferedImage explosion;
	private static BufferedImage[] playerSprites;
	public static BufferedImage star;
	public static BufferedImage cross;
	public static BufferedImage check;
	

	public static int[][] mapTemplate;
		
	public static void loadAllData() {
		
		try {
			loadImages();
		} catch (IOException ex) {
			
		}
		
		try {
			loadMap();
		} catch (IOException ex) {
			
		}
	}
	
	private static void loadMap() throws IOException {
		java.util.List<String> readInfoQ;

		File archivo = new File("db/map.txt");
		readInfoQ = java.nio.file.Files.readAllLines(archivo.toPath());
		mapTemplate = new int[bomberman.Bomberman.SIZE_MAP][bomberman.Bomberman.SIZE_MAP];
		for (int i = 0; i < readInfoQ.size(); i++) {
			String[] columns = readInfoQ.get(i).split(",");
			for (int j = 0; j < columns.length; j++) {
				mapTemplate[i][j] = Integer.parseInt(columns[j]);
			}
		}
	}
	
	private static void loadImages() throws IOException {
		
		tiles = ImageIO.read(new File("assets/tiles.png"));
		powerups = ImageIO.read(new File("assets/powerups.png"));
		bombs = ImageIO.read(new File("assets/bombsprite.png"));
		explosion = ImageIO.read(new File("assets/explodesprite.png"));
		
		check = ImageIO.read(new File("assets/check.png"));
		cross = ImageIO.read(new File("assets/cross.png"));
		star = ImageIO.read(new File("assets/star.png"));
		
		int playerSpriteSheets = 6;
		playerSprites = new BufferedImage[playerSpriteSheets];
		for (int i = 0; i < playerSpriteSheets; i++) {
			playerSprites[i] = ImageIO.read(new File("assets/player"+i+".png"));
		}
	}
	
	public static BufferedImage getBombFrame(int index) {
		return bombs.getSubimage(index*16, 0, 16, 16);
	}
	
	public static BufferedImage getPowerupFrame(int powerIndex, int frameIndex) {
		return powerups.getSubimage(powerIndex*16, frameIndex*16, 16, 16);
	}
	
	public static BufferedImage getTile(int index) {
		return tiles.getSubimage(index*16, 0, 16, 16);
	}
	
	public static BufferedImage getExplosionFrame(int frameIndex, int type) {
		int random = (int)(Math.ceil(Math.random()*4));
		int frameDisplace = frameIndex*4*16;
		int frameY = type*16;
		int frameX = (random-1)*16;
		return explosion.getSubimage(frameDisplace+frameX, frameY, 16, 16);
	}
	
	public static BufferedImage getPlayerFrame(int playerColor, int anim, int frame) {
		BufferedImage thisSheet;
		thisSheet = playerSprites[playerColor];
		int x;
		int y;
		if (anim < 5) {
			x = (frame-1)*16;
			y = (anim-1)*25;
		} else {
			x = 48;
			y = (frame-1)*25;
		}
		return thisSheet.getSubimage(x, y, 16, 25);
	}
}

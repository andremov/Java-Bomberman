/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public class Map {
	
	private Tile[][] tiles;
	
	public Map() {
		tiles = new Tile [bomberman.Bomberman.SIZE_MAP][bomberman.Bomberman.SIZE_MAP];
		for (int i = 0; i < bomberman.Bomberman.SIZE_MAP; i++) {
			for (int j = 0; j < bomberman.Bomberman.SIZE_MAP; j++) {
				Tile newTile = new Tile(data.NIC.mapTemplate[i][j]);
				tiles[i][j] = newTile;
			}
		}
	}
	
	/**
	 * Returns display image for this map.
	 * @return 
	 */
	public BufferedImage getDisplay() {
		int mapSize = bomberman.Bomberman.TILE_SIZE*bomberman.Bomberman.SIZE_MAP;
		BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		for (int i = 0; i < bomberman.Bomberman.SIZE_MAP; i++) {
			for (int j = 0; j < bomberman.Bomberman.SIZE_MAP; j++) {
				int posX = i*bomberman.Bomberman.TILE_SIZE;
				int posY = j*bomberman.Bomberman.TILE_SIZE;
				g.drawImage(getTile(i,j).getImage(), posX, posY, null);
			}
		}
		
		return image;
	}

	/**
	 * @return the tiles
	 */
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
}

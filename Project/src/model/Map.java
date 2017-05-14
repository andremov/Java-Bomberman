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
		tiles = new Tile [data.NIC.SIZE_MAP][data.NIC.SIZE_MAP];
		for (int i = 0; i < data.NIC.SIZE_MAP; i++) {
			for (int j = 0; j < data.NIC.SIZE_MAP; j++) {
				Tile newTile = new Tile(data.NIC.mapTemplate[i][j]);
				tiles[i][j] = newTile;
			}
		}
	}
	
	public BufferedImage getDisplay() {
		int mapSize = Handler.TILE_SIZE*data.NIC.SIZE_MAP;
		BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		for (int i = 0; i < data.NIC.SIZE_MAP; i++) {
			for (int j = 0; j < data.NIC.SIZE_MAP; j++) {
				int posX = i*Handler.TILE_SIZE;
				int posY = j*Handler.TILE_SIZE;
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

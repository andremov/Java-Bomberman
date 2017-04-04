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
	
	Tile[][] tiles;
	BufferedImage map;
	boolean changed;
	public Map() {
		tiles = new Tile [data.NIC.SIZE_MAP][data.NIC.SIZE_MAP];
		for (int i = 0; i < data.NIC.SIZE_MAP; i++) {
			for (int j = 0; j < data.NIC.SIZE_MAP; j++) {
				int value = data.NIC.mapTemplate[i][j];
				Tile newTile = new Tile();
				if (value == 0) {
					newTile.setObject(new Wall(false));
				} else if (value == 2) {
					double roll = Math.random();
					if (roll > 0.2) {
						newTile.setObject(new Wall(true));
					}
				}
				tiles[i][j] = newTile;
			}
		}
		genMap();
	}
	
	private void genMap() {
		int mapSize = Handler.TILE_SIZE*data.NIC.SIZE_MAP;
		BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		for (int i = 0; i < data.NIC.SIZE_MAP; i++) {
			for (int j = 0; j < data.NIC.SIZE_MAP; j++) {
				int posX = i*Handler.TILE_SIZE;
				int posY = j*Handler.TILE_SIZE;
				g.drawImage(tiles[i][j].getImage(), posX, posY, null);
			}
		}
		this.changed = false;
		this.map = image;
	}
	
	public BufferedImage getDisplay() {
		if (changed) {
			genMap();
		}
		return map;
	}
}

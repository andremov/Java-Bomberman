/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import data.NIC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public class Wall extends TileObject {
	
	boolean breakable;

	public Wall(boolean breakable) {
		this.breakable = breakable;
	}

	@Override
	public void tick() {
		// walls do nothing
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(Handler.TILE_SIZE, Handler.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		int tile = 1;
		if (breakable) {
			tile = 2;
		}
		
		g.drawImage(NIC.getTile(tile), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
		
		
		return image;
		
	}
}

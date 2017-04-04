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
public class Tile extends TileObject {
	
	private TileObject object;

	public Tile() {
		this.object = null;
	}
	
	@Override
	public void tick() {
		this.getObject().tick();
	}
	
	public int blewUp() {
		int firePower = 0;
		if (this.getObject() instanceof Bomb) {
			if (((Bomb) this.getObject()).blewUp()) {
				firePower = ((Bomb) this.getObject()).getFirePower();
				int bombs = ((Bomb) this.getObject()).getOwner().getBombsLeft();
				((Bomb) this.getObject()).getOwner().setBombsLeft(bombs+1);
				this.setObject(new Boom(Boom.TYPE_CENTER,Boom.ROTATION_90));
			}
		}
		return firePower;
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(Handler.TILE_SIZE, Handler.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.drawImage(NIC.getTile(3), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
		
		if (this.getObject() != null) {
			g.drawImage(this.getObject().getImage(), 0, 0, null);
		}
		
		return image;
	}

	/**
	 * @return the object
	 */
	public TileObject getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(TileObject object) {
		this.object = object;
	}
	
}

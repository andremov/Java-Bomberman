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
public class Tile {
	
	public static final int OBJECT_EMPTY = 0;
	public static final int OBJECT_BLOCK = 1;
	public static final int OBJECT_WALL = 2;
	public static final int OBJECT_BOMB = 3;
	public static final int OBJECT_POWERUP_BOMB = 4;
	public static final int OBJECT_POWERUP_FIRE = 5;
	public static final int OBJECT_BOOM = 6;
	
	public static final int TYPE_CENTER = 0;
	public static final int TYPE_LONG = 1;
	public static final int TYPE_TIP = 2;
	// TYPE_LONG + ROTATED 90 = 3
	// TYPE_TIP + ROTATED 90 = 4
	// TYPE_LONG + ROTATED 180 = 5
	// TYPE_TIP + ROTATED 180 = 6
	// TYPE_LONG + ROTATED 270 = 7
	// TYPE_TIP + ROTATED 270 = 8
	public static final int TYPE_ROTATE = 2;
//	public static final int TYPE_ROTATE_180 = 4;
//	public static final int TYPE_ROTATE_270 = 6;
	
	public static int BOMB_MAX_SPRITE = 3;
	public static int BOOM_MAX_SPRITE = 5;
	
	
	private int object;
	private int frame;
	private int delta;

	public Tile() {
		this.object = 0;
		this.frame = -1;
		this.delta = 1;
	}
	
	public boolean isSolid() {
		return this.object == OBJECT_BLOCK || this.isBreakable();
	}
	
	public boolean isBreakable() {
		return this.object == OBJECT_WALL;
	}
	
	public boolean isEmpty() {
		return this.object == OBJECT_EMPTY;
	}
	
	public boolean isBoom() {
		return this.object >= OBJECT_BOOM;
	}

	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(Handler.TILE_SIZE, Handler.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.drawImage(NIC.getTile(2), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
		
		if (this.object != OBJECT_EMPTY) {
			if (this.object == OBJECT_BLOCK) {
				g.drawImage(NIC.getTile(0), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
			} else if (this.object == OBJECT_WALL) {
				g.drawImage(NIC.getTile(1), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
			} else {
				this.frame = this.frame + this.delta;
				if (this.frame == 0) {
					this.delta = 1;
				}
				if (this.object == OBJECT_BOMB) {
					if (this.frame == BOMB_MAX_SPRITE-1) {
						this.delta = -1;
					}
					g.drawImage(NIC.getBombFrame(this.frame), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
				} else if (this.object >= OBJECT_BOOM) {
					int type = this.object-OBJECT_BOOM;
					if (this.frame == 1 && this.delta == -1) {
						this.object = OBJECT_EMPTY;
					}
					if (this.frame == BOOM_MAX_SPRITE-1) {
						this.delta = -1;
					}
					int x = 0;
					int y = 0;
					if (type != TYPE_CENTER) {
						int rotations = (int)Math.floor((type-1)/2);
						
						((java.awt.Graphics2D)g).rotate(Math.toRadians(90*rotations));
						type = type-(TYPE_ROTATE*rotations);

						if (rotations == 1) {
							y = -Handler.TILE_SIZE;
						} else if(rotations == 2){
							x = -Handler.TILE_SIZE;
							y = -Handler.TILE_SIZE;
						} else if (rotations == 3) {
							x = -Handler.TILE_SIZE;
						}
					}
					g.drawImage(NIC.getExplosionFrame(this.frame, type), x, y, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
				}
				
			}
		}
		
		return image;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(int object) {
		this.object = object;
		this.frame = -1;
		this.delta = 1;
	}
	
}

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
	
	private static final int OBJECT_EMPTY = 0;
	private static final int OBJECT_BLOCK = 1;
	private static final int OBJECT_WALL = 2;
	public static final int OBJECT_BOMB = 3;
	private static final int OBJECT_POWERUP_BOMB = 4;
	private static final int OBJECT_POWERUP_FIRE = 5;
	private static final int OBJECT_BOOM = 6;
	
	private static final int BOOM_CENTER = 0;
	private static final int BOOM_ARM = 1;
	private static final int BOOM_MOD_TIP = 1;
	// TYPE_LONG + ROTATED 90 = 3
	// TYPE_TIP + ROTATED 90 = 4
	// TYPE_LONG + ROTATED 180 = 5
	// TYPE_TIP + ROTATED 180 = 6
	// TYPE_LONG + ROTATED 270 = 7
	// TYPE_TIP + ROTATED 270 = 8
	private static final int BOOM_MOD_ROTATE = 2;
	
	private static final int BOMB_MAX_SPRITE = 3;
	private static final int BOOM_MAX_SPRITE = 5;
	
	
	private int object;
	private int frame;
	private int delta;

	public Tile(int value) {
		if (value == 0) {
			this.setObject(OBJECT_BLOCK);
		} else if (value == 1) {
			this.setObject(OBJECT_EMPTY);
		} else if (value == 2) {
			if (Math.random() > 0.2) {
				this.setObject(OBJECT_WALL);
			}
		}
	}
        
	/**
	 * Returns true if this tile is a solid or non solid wall.
	 * @return 
	 */
	public boolean isSolid() {
		return this.object == OBJECT_BLOCK || this.isBreakable();
	}
	
	/**
	 * Returns power up code, removes power up.
	 * @return 
	 */
	public int takePowerup() {
		int p = 0;
		if (isPowerup()) {
			if (this.object == OBJECT_POWERUP_BOMB) {
				p = 1;
			} else if (this.object == OBJECT_POWERUP_FIRE) {
				p = 2;
			}
			this.setObject(OBJECT_EMPTY);
		}
		return p;
	}

	/**
	 * Returns true if this tile contains a power up.
	 * @return 
	 */
	public boolean isPowerup() {
		return this.object == OBJECT_POWERUP_BOMB || this.object == OBJECT_POWERUP_FIRE;
	}
        
	/**
	 * Returns true if this tile is breakable.
	 * @return 
	 */
	public boolean isBreakable() {
		return this.object == OBJECT_WALL;
	}
	
	/**
	 * Returns true if this tile is empty.
	 * @return 
	 */
	public boolean isEmpty() {
		return this.object == OBJECT_EMPTY;
	}
	
	/**
	 * Returns true if this tile is a burn tile.
	 */
	public boolean isBoom() {
		return this.object >= OBJECT_BOOM;
	}
	
	/**
	 * Returns true if this tile is a bomb.
	 * @return 
	 */
	public boolean isBomb() {
		return this.object == OBJECT_BOMB;
	}

	/**
	 * Returns this tile's image.
	 * @return 
	 */
	public BufferedImage getImage() {
		int tileSize = bomberman.Bomberman.TILE_SIZE;
		BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.drawImage(NIC.getTile(2), 0, 0, tileSize, tileSize, null);
		
		if (this.object != OBJECT_EMPTY) {
			if (this.object == OBJECT_BLOCK) {
				g.drawImage(NIC.getTile(0), 0, 0, tileSize, tileSize, null);
			} else if (this.object == OBJECT_WALL) {
				g.drawImage(NIC.getTile(1), 0, 0, tileSize, tileSize, null);
			} else {
				this.frame = this.frame + this.delta;
				if (this.frame == 0) {
					this.delta = 1;
				}
				if (this.object == OBJECT_BOMB) {
					if (this.frame == BOMB_MAX_SPRITE-1) {
						this.delta = -1;
					}
					g.drawImage(NIC.getBombFrame(this.frame), 0, 0, tileSize, tileSize, null);
				} else if (this.object == OBJECT_POWERUP_BOMB) {
					g.drawImage(NIC.getPowerupFrame(0,this.frame%2), 0, 0, tileSize, tileSize, null);
				} else if (this.object == OBJECT_POWERUP_FIRE) {
					g.drawImage(NIC.getPowerupFrame(1,this.frame%2), 0, 0, tileSize, tileSize, null);
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
					if (type != BOOM_CENTER) {
						int rotations = (int)Math.floor((type-1)/2);
						
						((java.awt.Graphics2D)g).rotate(Math.toRadians(90*rotations));
						type = type-(BOOM_MOD_ROTATE*rotations);

						if (rotations == 1) {
							y = -tileSize;
						} else if(rotations == 2){
							x = -tileSize;
							y = -tileSize;
						} else if (rotations == 3) {
							x = -tileSize;
						}
					}
					g.drawImage(NIC.getExplosionFrame(this.frame, type), x, y, tileSize, tileSize, null);
				}
				
			}
		}
		
		return image;
	}
        
	/**
	 * Destroys wall, rolls for power up.
	 */
	public void destroyWall() {
		if (this.isBreakable()) {
			int random = (int)(Math.ceil(Math.random()*10));
			if (random <= 6) {
				this.object = OBJECT_EMPTY;
			} else if (random <= 8) {
				this.object = OBJECT_POWERUP_BOMB;
			} else if (random <= 10) {
				this.object = OBJECT_POWERUP_FIRE;
			}
		}
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(int object) {
		this.object = object;
		this.frame = -1;
		this.delta = 1;
	}

	/**
	 * Returns the object.
	 * @return 
	 */
	public int getObject() {
		return object;
	}
	
	/**
	 * Sets as burn tile.
	 * @param x
	 * @param y 
	 */
	public void setBoom(int x, int y) {
		int type = OBJECT_BOOM;
		
		if (x == 0 && y == 0) {
			type = type+BOOM_CENTER;
		} else {
			type = type + BOOM_ARM;
			if (Integer.max(Math.abs(x),Math.abs(y)) == 2) {
				type = type + BOOM_MOD_TIP;
			}
			
			if (x <= 0) {
				type = type + BOOM_MOD_ROTATE;
				if (y < 0) {
					type = type + (BOOM_MOD_ROTATE*2);
				}
				if (x < 0) {
					type = type + BOOM_MOD_ROTATE;
				}
			}
		}
		if (this.object != OBJECT_BOOM + BOOM_CENTER) {
			this.setObject(type);
		}
	
	}
	
}

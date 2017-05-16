/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.KeyHandler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public class RealPlayer extends Player {
	
	private static final int COLLIDER_SIZE = 5;
	private static final int MOVE_PX = 3;
	
	private int firePower;
	private int bombsLeft;
	private int xDelta;
	private int yDelta;
	private boolean firstX;
	
	public RealPlayer() {
		super();
	}
	
	/**
	 * Give this player the default values.
	 * @param cd 
	 */
	public void init(Coordinate cd) {
		this.firePower = 2;
		this.bombsLeft = 1;
		this.setAlive(true);
		this.setCoordinate(cd);
		this.firstX = false;
		this.setMoving(false);
		defaultAnimation();
	}
	
	/**
	 * Checks collisions, moves the player, advances the animation.
	 * All processes needed per system 'tick'.
	 */
	private void tick() {
		boolean[] collisions = new boolean[6];
		for (int i = 0; i < collisions.length; i++) {
			collisions[i] = false;
		}

		// CHECK IF BURNING
		boolean killPlayer = false;
		for (int x = getCollisionMinTileX(); x <= getCollisionMaxTileX(); x++) {
			for (int y = getCollisionMinTileY(); y <= getCollisionMaxTileY(); y++) {
				killPlayer = killPlayer || !Handler.getGame().burnTileCheck(x, y);
			}
		}
		this.setAlive(killPlayer);

		if (this.isMoving()) {
			// NOT MOVING
			
			//DO ANIMATION
			this.setFrame(this.getFrame()+1);
			if (this.getFrame() > 3) {
				this.setFrame(1);
			}
			
			// CHECK X
			boolean moveX = false;
			if (xDelta != 0) {
				int x;
				if (xDelta > 0) {
					// RIGHT
					x = getCollisionMaxX();
				} else {
					// LEFT
					x = getCollisionMinX();
				}
				x = x + (MOVE_PX*xDelta);
				
				Coordinate cd1 = new Coordinate(x,getCollisionMinY(),Coordinate.TYPE_REAL);
				Coordinate cd2 = new Coordinate(x,getCollisionMaxY(),Coordinate.TYPE_REAL);
				
				if (this.getCoordinate().equals(cd2) && this.getCoordinate().equals(cd1)) {
					moveX = true;
				} else {
					boolean collision = Handler.getGame().solidTileCheck(cd1.getTileX(), cd1.getTileY());
					collision = collision || Handler.getGame().solidTileCheck(cd2.getTileX(), cd2.getTileY());
					if (!collision) {
						moveX = true;
					}
				}
			}
			
			// CHECK Y
			boolean moveY = false;
			if (yDelta != 0) {
				int y;
				if (yDelta > 0) {
					// DOWN
					y = getCollisionMaxY();
				} else {
					// UP
					y = getCollisionMinY();
				}
				y = y + (MOVE_PX*yDelta);
				
				Coordinate cd1 = new Coordinate(getCollisionMinX(),y,Coordinate.TYPE_REAL);
				Coordinate cd2 = new Coordinate(getCollisionMaxX(),y,Coordinate.TYPE_REAL);
				
				if (this.getCoordinate().equals(cd2) && this.getCoordinate().equals(cd1)) {
					moveY = true;
				} else {
					boolean collision = Handler.getGame().solidTileCheck(cd1.getTileX(), cd1.getTileY());
					collision = collision || Handler.getGame().solidTileCheck(cd2.getTileX(), cd2.getTileY());
					if (!collision) {
						moveY = true;
					}
				}
			}
			
			// DIAGONAL CHECK
			if (moveX && moveY) {
				int y;
				if (yDelta > 0) {
					// DOWN
					y = getCollisionMaxY();
				} else {
					// UP
					y = getCollisionMinY();
				}
				y = y + (MOVE_PX*yDelta);
				int x;
				if (xDelta > 0) {
					// RIGHT
					x = getCollisionMaxX();
				} else {
					// LEFT
					x = getCollisionMinX();
				}
				x = x + (MOVE_PX*xDelta);
				Coordinate cd = new Coordinate(x,y,Coordinate.TYPE_REAL);
				
				if (!this.getCoordinate().equals(cd)) {
					boolean collision = Handler.getGame().solidTileCheck(cd.getTileX(), cd.getTileY());
					if (collision) {
						if (firstX) {
							moveY = false;
						} else {
							moveX = false;
						}
					}
				}
				
			}
			
			// COMMIT MOVEMENTS
			if (moveX) {
				this.getCoordinate().setRealX(this.getCoordinate().getRealX() + (MOVE_PX*xDelta));
			}
			
			if (moveY) {
				this.getCoordinate().setRealY(this.getCoordinate().getRealY() + (MOVE_PX*yDelta));
			}
			
			// POWERUP CHECK
			for (int x = getCollisionMinTileX(); x <= getCollisionMaxTileX(); x++) {
				for (int y = getCollisionMinTileY(); y <= getCollisionMaxTileY(); y++) {
					int power = Handler.getGame().powerTileCheck(x, y);
					if (power == 1) {
						this.bombsLeft++;
					} else if (power == 2) {
						this.firePower++;
					}
				}
			}
			
		} else {
			// NOT MOVING
		}
		
		
	}

	/**
	 * Gets the image representation of this player.
	 * @return 
	 */
	@Override
	public BufferedImage getDisplay() {
		BufferedImage image = new BufferedImage(16, 25, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		if (Handler.getGame() != null) {
			tick();
		}
		int trueColor = getColor();
		if (!isEnabled()) {
			trueColor = 0;
		}
		g.drawImage(data.NIC.getPlayerFrame(trueColor,this.getAnimation(),this.getFrame()), 0, 0, 16, 25, null);
		
		return image;
	}
	
	/**
	 * If the player can plant a bomb, reduces the bombs left, and returns true.
	 * Else, returns false.
	 * @return 
	 */
	public boolean plantBomb() {
		boolean canPlant = this.bombsLeft > 0 && isAlive();
		if (canPlant) {
			this.bombsLeft--;
		}
		return canPlant;
	}
	
    /**
	 * Makes sure the player animation is the proper one.
	 */
	private void checkAnim() {
		this.setMoving(true);
		if (yDelta < 0) {
			// UP
			this.setAnimation(ANIM_MOVE_UP);
		} else if (yDelta > 0) {
			// DOWN
			this.setAnimation(ANIM_MOVE_DOWN);
		} else if (xDelta < 0) {
			// LEFT
			this.setAnimation(ANIM_MOVE_LEFT);
		} else if (xDelta > 0) {
			// RIGHT
			this.setAnimation(ANIM_MOVE_RIGHT);
		} else {
			// NONE
			this.setMoving(false);
			this.setFrame(2);
		}
	}
	
	/**
	 * Receives an action code, and responds accordingly.
	 */
	public void receiveAction(int actionCode) {
		int stateCode = actionCode%2;
		int directionCode = actionCode - stateCode;
		if (stateCode == KeyHandler.MOD_PRESS) {
			if (directionCode == KeyHandler.ACTION_DOWN) {
				yDelta = 1;
				this.firstX = false;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_LEFT) {
				xDelta = -1;
				this.firstX = true;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_RIGHT) {
				xDelta = 1;
				this.firstX = true;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_UP) {
				yDelta = -1;
				this.firstX = false;
				checkAnim();
			}
		} else {
			if (directionCode == KeyHandler.ACTION_DOWN && yDelta > 0) {
				yDelta = 0;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_LEFT && xDelta < 0) {
				xDelta = 0;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_RIGHT && xDelta > 0) {
				xDelta = 0;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_UP && yDelta < 0) {
				yDelta = 0;
				checkAnim();
			}
		}
	}
	
	//<editor-fold defaultstate="collapsed" desc="Getters & Setters">
	
	
	private int getCollisionMinTileX() {
		return new Coordinate(this.getCoordinate().getRealX() - COLLIDER_SIZE,this.getCoordinate().getRealY(),Coordinate.TYPE_REAL).getTileX();
	}
	
	private int getCollisionMaxTileX() {
		return new Coordinate(this.getCoordinate().getRealX() + COLLIDER_SIZE,this.getCoordinate().getRealY(),Coordinate.TYPE_REAL).getTileX();
	}
	
	private int getCollisionMinTileY() {
		return new Coordinate(this.getCoordinate().getRealX(),this.getCoordinate().getRealY() - COLLIDER_SIZE,Coordinate.TYPE_REAL).getTileY();
	}
	
	private int getCollisionMaxTileY() {
		return new Coordinate(this.getCoordinate().getRealX(),this.getCoordinate().getRealY() + COLLIDER_SIZE,Coordinate.TYPE_REAL).getTileY();
	}
	
	private int getCollisionMinX() {
		return this.getCoordinate().getRealX() - COLLIDER_SIZE;
	}
	
	private int getCollisionMaxX() {
		return this.getCoordinate().getRealX() + COLLIDER_SIZE;
	}
	
	private int getCollisionMinY() {
		return this.getCoordinate().getRealY() - COLLIDER_SIZE;
	}
	
	private int getCollisionMaxY() {
		return this.getCoordinate().getRealY() + COLLIDER_SIZE;
	}
	
	/**
	 * @return the firePower
	 */
	public int getFirePower() {
		return firePower;
	}

	/**
	 * @param firePower the firePower to set
	 */
	public void setFirePower(int firePower) {
		this.firePower = firePower;
	}


	/**
	 * @return the bombsLeft
	 */
	public int getBombsLeft() {
		return bombsLeft;
	}

	/**
	 * @param bombsLeft the bombsLeft to set
	 */
	public void setBombsLeft(int bombsLeft) {
		this.bombsLeft = bombsLeft;
	}
	//</editor-fold>


}

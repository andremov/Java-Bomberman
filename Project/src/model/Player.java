/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.KeyHandler;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public class Player {
	
	public static int COLLIDER_SIZE = 12;
	public static int DELTA = 4;
	
	public static final int DELTA_CENTER_X = 8;
	public static final int DELTA_CENTER_Y = 19;
			
	public static int ANIM_MOVE_DOWN = 1;
	public static int ANIM_MOVE_LEFT = 2;
	public static int ANIM_MOVE_RIGHT = 3;
	public static int ANIM_MOVE_UP = 4;
	public static int ANIM_DEAD = 4;
	
	private boolean enabled;
	private int firePower;
	private boolean alive;
	private int bombsLeft;
	private Coordinate coordinate;
	private int color;
	private int currentFrame;
	private int currentAnim;
	private int xDelta;
	private int yDelta;
	private boolean moving;
	private boolean bombCollision;
	
	public Player() {
		this.enabled = false;
		this.alive = true;
		this.moving = false;
		this.bombCollision = true;
		this.firePower = 2;
		this.bombsLeft = 1;
		resetPlayer();
	}
	
	public void resetPlayer() {
		this.currentAnim = ANIM_MOVE_DOWN;
		this.currentFrame = 2;
	}
        
	private void tick() {
		float physicsX = this.coordinate.getxReal()-(COLLIDER_SIZE/2);
		float physicsY = this.coordinate.getyReal()-(COLLIDER_SIZE/2);
		float newX = this.coordinate.getxReal();
		float newY = this.coordinate.getyReal();
		if (xDelta != 0) {
			newX = newX+xDelta+((xDelta/Math.abs(xDelta))*(COLLIDER_SIZE/2));
		}
		if (xDelta != 0) {
			newY = newY+yDelta+((yDelta/Math.abs(yDelta))*(COLLIDER_SIZE/2));
		}
		if (!this.bombCollision) {
			this.bombCollision = !Handler.getGame().openSpace(physicsX, physicsY);
		}
		if (moving) {
			currentFrame++;
			if (currentFrame > 3) {
				currentFrame = 1;
			}
			Coordinate cd = new Coordinate(Coordinate.TYPE_REAL, newX, newY);
			if (cd.getxTile() != this.coordinate.getxTile() || cd.getyTile() != this.coordinate.getyTile()) {
				if (!Handler.getGame().collision(physicsX+xDelta,physicsY,this.bombCollision)){
					this.coordinate.setxReal(physicsX+xDelta);
				}
				if (!Handler.getGame().collision(physicsX,physicsY+yDelta,this.bombCollision)){
					this.coordinate.setyReal(physicsY+yDelta);
				}
				int powerup = Handler.getGame().powerCheck(physicsX,physicsY);
				if (powerup != 0) {
					if (powerup == 1) {
						this.bombsLeft++;
					} else {
						this.firePower++;
					}
				}
			} else {
				this.coordinate.setyReal(physicsY+yDelta);
				this.coordinate.setxReal(physicsX+xDelta);
			}
		}
		if (Handler.getGame().burn(physicsX,physicsY)) {
			this.alive = false;
		}
	}

	public BufferedImage getDisplay() {
		BufferedImage image = new BufferedImage(16, 25, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		tick();
                
		g.drawImage(data.NIC.getPlayerFrame(color,currentAnim,currentFrame), 0, 0, 16, 25, null);
		
		return image;
	}
        
	public boolean plantBomb() {
		boolean canPlant = this.bombsLeft > 0;
		if (canPlant) {
			this.bombsLeft--;
			this.bombCollision = false;
		}
		return canPlant;
	}
        
	private void checkAnim() {
		moving = true;
		if (yDelta < 0) {
			// UP
			currentAnim = ANIM_MOVE_UP;
		} else if (yDelta > 0) {
			// DOWN
			currentAnim = ANIM_MOVE_DOWN;
		} else if (xDelta < 0) {
			// LEFT
			currentAnim = ANIM_MOVE_LEFT;
		} else if (xDelta > 0) {
			// RIGHT
			currentAnim = ANIM_MOVE_RIGHT;
		} else {
			// NONE
			moving = false;
			currentFrame = 2;
		}
	}
	
	public void move(int actionCode) {
		int stateCode = actionCode%2;
		int directionCode = actionCode - stateCode;
		if (stateCode == KeyHandler.MOD_PRESS) {
			if (directionCode == KeyHandler.ACTION_DOWN) {
				yDelta = DELTA;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_LEFT) {
				xDelta = -DELTA;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_RIGHT) {
				xDelta = DELTA;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_UP) {
				yDelta = -DELTA;
				checkAnim();
			}
		} else {
			if (directionCode == KeyHandler.ACTION_DOWN && yDelta == DELTA) {
				yDelta = 0;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_LEFT && xDelta == -DELTA) {
				xDelta = 0;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_RIGHT && xDelta == DELTA) {
				xDelta = 0;
				checkAnim();
			} else if (directionCode == KeyHandler.ACTION_UP && yDelta == -DELTA) {
				yDelta = 0;
				checkAnim();
			}
		}
	}
	
	//<editor-fold defaultstate="collapsed" desc="Getters & Setters">

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
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
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

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param coordinate the coordinate to set
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	//</editor-fold>
}

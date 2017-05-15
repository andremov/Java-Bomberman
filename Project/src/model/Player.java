/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import control.KeyHandler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author Andres
 */
public class Player {
	
	private static final int COLLIDER_SIZE = 6;
	private static final int MOVE_PX = 3;
	
	private static final int DELTA_CENTER_X = 8;
	private static final int DELTA_CENTER_Y = 19;
			
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
	private boolean firstX;
	private Coordinate cantCollideWith;
    private BufferedReader in;
    private PrintWriter out;
	public int timeOuts;
	
	public Player() {
		this.enabled = false;
		this.cantCollideWith = null;
		this.timeOuts = 0;
		defaultAnimation();
	}
	
	public void init(Coordinate cd) {
		this.firePower = 2;
		this.bombsLeft = 1;
		this.alive = true;
		this.coordinate = cd;
		this.firstX = false;
		this.moving = false;
		defaultAnimation();
	}
	
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
		this.alive = killPlayer;

		if (moving) {
			// NOT MOVING
			
			//DO ANIMATION
			currentFrame++;
			if (currentFrame > 3) {
				currentFrame = 1;
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
				
				if (this.coordinate.equals(cd2) && this.coordinate.equals(cd1)) {
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
				
				if (this.coordinate.equals(cd2) && this.coordinate.equals(cd1)) {
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
				
				if (!this.coordinate.equals(cd)) {
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
				this.coordinate.setRealX(this.coordinate.getRealX() + (MOVE_PX*xDelta));
			}
			
			if (moveY) {
				this.coordinate.setRealY(this.coordinate.getRealY() + (MOVE_PX*yDelta));
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

	public BufferedImage getDisplay() {
		BufferedImage image = new BufferedImage(16, 25, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		if (Handler.getGame() != null) {
			tick();
		}
		int trueColor = color;
		if (!isEnabled()) {
			trueColor = 0;
		}
		g.drawImage(data.NIC.getPlayerFrame(trueColor,currentAnim,currentFrame), 0, 0, 16, 25, null);
		
		return image;
	}
        
	public boolean plantBomb() {
		boolean canPlant = this.bombsLeft > 0;
		if (canPlant) {
			this.bombsLeft--;
			this.cantCollideWith = new Coordinate(this.coordinate.getTileX(),this.coordinate.getTileY(),Coordinate.TYPE_TILE);
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
	
	public void defaultAnimation() {
		this.currentAnim = ANIM_MOVE_DOWN;
		this.currentFrame = 2;
	}
	
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
	
	public int getRawX() {
		return this.coordinate.getRealX();
	}
	
	public int getRawY() {
		return this.coordinate.getRealY();
	}
	public void setRawX(int x) {
		this.coordinate.setRealX(x);
	}
	
	public void setRawY(int y) {
		this.coordinate.setRealY(y);
	}
	
	public int getImageX() {
		return this.coordinate.getRealX()-DELTA_CENTER_X;
	}
	
	public int getImageY() {
		return this.coordinate.getRealY()-DELTA_CENTER_Y;
	}
	
	public int getTileX() {
		return this.coordinate.getTileX();
	}
	
	public int getTileY() {
		return this.coordinate.getTileY();
	}
	
	private int getCollisionMinTileX() {
		return new Coordinate(this.coordinate.getRealX() - COLLIDER_SIZE,this.coordinate.getRealY(),Coordinate.TYPE_REAL).getTileX();
	}
	
	private int getCollisionMaxTileX() {
		return new Coordinate(this.coordinate.getRealX() + COLLIDER_SIZE,this.coordinate.getRealY(),Coordinate.TYPE_REAL).getTileX();
	}
	
	private int getCollisionMinTileY() {
		return new Coordinate(this.coordinate.getRealX(),this.coordinate.getRealY() - COLLIDER_SIZE,Coordinate.TYPE_REAL).getTileY();
	}
	
	private int getCollisionMaxTileY() {
		return new Coordinate(this.coordinate.getRealX(),this.coordinate.getRealY() + COLLIDER_SIZE,Coordinate.TYPE_REAL).getTileY();
	}
	
	private int getCollisionMinX() {
		return this.coordinate.getRealX() - COLLIDER_SIZE;
	}
	
	private int getCollisionMaxX() {
		return this.coordinate.getRealX() + COLLIDER_SIZE;
	}
	
	private int getCollisionMinY() {
		return this.coordinate.getRealY() - COLLIDER_SIZE;
	}
	
	private int getCollisionMaxY() {
		return this.coordinate.getRealY() + COLLIDER_SIZE;
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
	//</editor-fold>

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(java.net.Socket socket) {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) { }
	}

	/**
	 * @return the in
	 */
	public BufferedReader getIn() {
		return in;
	}

	/**
	 * @return the out
	 */
	public PrintWriter getOut() {
		return out;
	}

}

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
public class Player {
	
	public static int ANIM_MOVE_DOWN = 1;
	public static int ANIM_MOVE_LEFT = 2;
	public static int ANIM_MOVE_RIGHT = 3;
	public static int ANIM_MOVE_UP = 4;
	public static int ANIM_DEAD = 4;
	
	private int maxBombs;
	private int firePower;
	private boolean alive;
	private int bombsLeft;
	private int x;
	private int y;
	private int tileX;
	private int tileY;
	private int color;
	private int currentFrame;
	private int currentAnim;
	
	public Player(int color) {
		this.color = color;
		this.alive = true;
		this.firePower = 2;
		this.maxBombs = 1;
		this.bombsLeft = 1;
		resetPlayer();
	}
	
	public void resetPlayer() {
		this.currentAnim = ANIM_MOVE_DOWN;
		this.currentFrame = 2;
	}
	
	public void setXY(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public BufferedImage getDisplay() {
		BufferedImage image = new BufferedImage(16, 25, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.drawImage(data.NIC.getPlayerFrame(color,currentAnim,currentFrame), 0, 0, 16,25, null);
		
		
		return image;
	}
	
	/**
	 * @return the maxBombs
	 */
	public int getMaxBombs() {
		return maxBombs;
	}

	/**
	 * @param maxBombs the maxBombs to set
	 */
	public void setMaxBombs(int maxBombs) {
		this.maxBombs = maxBombs;
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
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
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
	 * @return the tileX
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * @param tileX the tileX to set
	 */
	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	/**
	 * @return the tileY
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * @param tileY the tileY to set
	 */
	public void setTileY(int tileY) {
		this.tileY = tileY;
	}
	
}

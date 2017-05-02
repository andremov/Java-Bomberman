/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Andres
 */
public class Coordinate {
	
	public static int TYPE_TILE = 0;
	public static int TYPE_REAL = 1;
	
	private int xTile;
	private int yTile;
	private float xReal;
	private float yReal;

	public Coordinate(int type, float x, float y) {
		if (type == TYPE_TILE) {
			this.xTile = (int)x;
			this.yTile = (int)y;
			
			updateReals();
		} else {
			this.xReal = x;
			this.yReal = y;
			
			updateTiles();
		}
	}

	private void updateReals() {
		this.xReal = (this.getxTile() * Handler.TILE_SIZE + (Handler.TILE_SIZE/2));
		this.yReal = (this.getyTile() * Handler.TILE_SIZE + (Handler.TILE_SIZE/2));
	}
	
	private void updateTiles() {
		this.xTile = ((int) Math.floor(this.getxReal() / Handler.TILE_SIZE));
		this.yTile = ((int) Math.floor(this.getyReal() / Handler.TILE_SIZE));
	}

	/**
	 * @return the xTile
	 */
	public int getxTile() {
		return xTile;
	}

	/**
	 * @param xTile the xTile to set
	 */
	public void setxTile(int xTile) {
		this.xTile = xTile;
		updateReals();
	}

	/**
	 * @return the yTile
	 */
	public int getyTile() {
		return yTile;
	}

	/**
	 * @param yTile the yTile to set
	 */
	public void setyTile(int yTile) {
		this.yTile = yTile;
		updateReals();
	}

	/**
	 * @return the xReal
	 */
	public float getxReal() {
		return xReal;
	}

	/**
	 * @param xReal the xReal to set
	 */
	public void setxReal(float xReal) {
		this.xReal = xReal;
		updateTiles();
	}

	/**
	 * @return the yReal
	 */
	public float getyReal() {
		return yReal;
	}

	/**
	 * @param yReal the yReal to set
	 */
	public void setyReal(float yReal) {
		this.yReal = yReal;
		updateTiles();
	}
}

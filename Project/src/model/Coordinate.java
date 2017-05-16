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
	
	private int tileX;
	private int tileY;
	private int realX;
	private int realY;

	public Coordinate(int x, int y, int type) {
		if (type == TYPE_TILE) {
			this.tileX = x;
			this.tileY = y;
			
			updateReals();
		} else {
			this.realX = x;
			this.realY = y;
			
			updateTiles();
		}
	}

	/**
	 * Update real values given the tile values.
	 */
	private void updateReals() {
		int tileSize = bomberman.Bomberman.TILE_SIZE;
		this.realX = (this.getTileX() * tileSize + (tileSize/2));
		this.realY = (this.getTileY() * tileSize + (tileSize/2));
	}
	
	/**
	 * Update tile values given the real values.
	 */
	private void updateTiles() {
		int tileSize = bomberman.Bomberman.TILE_SIZE;
		this.tileX = ((int) Math.floor(this.getRealX() / tileSize));
		this.tileY = ((int) Math.floor(this.getRealY() / tileSize));
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
		updateReals();
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
		updateReals();
	}

	/**
	 * @return the realX
	 */
	public int getRealX() {
		return realX;
	}

	/**
	 * @param realX the realX to set
	 */
	public void setRealX(int realX) {
		this.realX = realX;
		updateTiles();
	}

	/**
	 * @return the realY
	 */
	public int getRealY() {
		return realY;
	}

	/**
	 * @param realY the realY to set
	 */
	public void setRealY(int realY) {
		this.realY = realY;
		updateTiles();
	}

	/**
	 * Checks if the tile values for this and the given coordinate are the same.
	 * @param obj
	 * @return 
	 */
	@Override
	public boolean equals(Object obj) {
		boolean value = false;
		if (obj instanceof Coordinate) {
			Coordinate cd = (Coordinate) obj;
			value = this.getTileX() == cd.getTileX() && this.getTileY() == cd.getTileY();
		}
		return value;
	}
}

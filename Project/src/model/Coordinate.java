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

	private void updateReals() {
		this.realX = (this.getTileX() * Handler.TILE_SIZE + (Handler.TILE_SIZE/2));
		this.realY = (this.getTileY() * Handler.TILE_SIZE + (Handler.TILE_SIZE/2));
	}
	
	private void updateTiles() {
		this.tileX = ((int) Math.floor(this.getRealX() / Handler.TILE_SIZE));
		this.tileY = ((int) Math.floor(this.getRealY() / Handler.TILE_SIZE));
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

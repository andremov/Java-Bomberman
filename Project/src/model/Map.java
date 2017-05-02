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
public class Map {
	
	private Tile[][] tiles;
	
	public Map() {
		tiles = new Tile [data.NIC.SIZE_MAP][data.NIC.SIZE_MAP];
		for (int i = 0; i < data.NIC.SIZE_MAP; i++) {
			for (int j = 0; j < data.NIC.SIZE_MAP; j++) {
				int value = data.NIC.mapTemplate[i][j];
				Tile newTile = new Tile();
				if (value == 0) {
					newTile.setObject(Tile.OBJECT_BLOCK);
				} else if (value == 2) {
					double roll = Math.random();
					if (roll > 0.2) {
						newTile.setObject(Tile.OBJECT_WALL);
					}
				}
				tiles[i][j] = newTile;
			}
		}
	}
	
        public int powerCheck(float x, float y) {
            Coordinate cd = new Coordinate(Coordinate.TYPE_REAL, x, y);
            int i = cd.getxTile();
            int j = cd.getyTile();
            int power = tiles[i][j].getPowerup();
            if (power != 0) {
                tiles[i][j].setObject(Tile.OBJECT_EMPTY);
            }
            return power;
        }
        
	public boolean burn(float x, float y) {
		boolean collided = false;
		
		Coordinate cd = new Coordinate(Coordinate.TYPE_REAL, x, y);
                int i = cd.getxTile();
                int j = cd.getyTile();
                if (tiles[i][j].isBoom()) {
                    collided = true;
                }
		return collided;
	}
	
	public boolean collision(float x, float y, boolean canCollide) {
		// 0 -> no collision
		// 1 -> X collsion
		// 2 -> Y collision
		// 3 -> both
		
//		int x = (int)check.getxTile();
//		int y = (int)check.getyTile();

		boolean collided = false;
		
		Coordinate cd = new Coordinate(Coordinate.TYPE_REAL, x, y);
		for (int i = cd.getxTile()-1; i <= cd.getxTile()+1; i++) {
			for (int j = cd.getyTile()-1; j <= cd.getyTile()+1; j++) {
				try {
//					if (i > 0 && j > 0 && i < tiles.length && j < tiles.length) {
//					if (i != cd.getxTile() && j != cd.getyTile()) {
						if (tiles[i][j].isSolid()) {
							boolean minorX = i*Handler.TILE_SIZE < x && x < (i+1)*Handler.TILE_SIZE;
							boolean majorX = i*Handler.TILE_SIZE < x+Player.COLLIDER_SIZE && x+Player.COLLIDER_SIZE < (i+1)*Handler.TILE_SIZE;

							boolean minorY = j*Handler.TILE_SIZE < y && y < (j+1)*Handler.TILE_SIZE;
							boolean majorY = j*Handler.TILE_SIZE < y+Player.COLLIDER_SIZE && y+Player.COLLIDER_SIZE < (j+1)*Handler.TILE_SIZE;

							if ((minorX || majorX) && (minorY || majorY)) {
//								System.out.println("Colliding with tile ("+i+", "+j+")");
								collided = true;
							}
							if ((minorX && majorX) && (minorY && majorY)) {
								System.out.println("Preventing collision");
								collided = false;
							}
						} else if (tiles[i][j].isBomb()) {
                                                    if (canCollide) {
							boolean minorX = i*Handler.TILE_SIZE < x && x < (i+1)*Handler.TILE_SIZE;
							boolean majorX = i*Handler.TILE_SIZE < x+Player.COLLIDER_SIZE && x+Player.COLLIDER_SIZE < (i+1)*Handler.TILE_SIZE;

							boolean minorY = j*Handler.TILE_SIZE < y && y < (j+1)*Handler.TILE_SIZE;
							boolean majorY = j*Handler.TILE_SIZE < y+Player.COLLIDER_SIZE && y+Player.COLLIDER_SIZE < (j+1)*Handler.TILE_SIZE;

							if ((minorX || majorX) && (minorY || majorY)) {
//								System.out.println("Colliding with tile ("+i+", "+j+")");
								collided = true;
							}
                                                    }
                                                }
//					}
				} catch(Exception e) {
					// doesnt exist
				}
			}
		}
		
		return collided;
	}
	
        public boolean openSpace(float x, float y) {
            boolean collided = false;

            Coordinate cd = new Coordinate(Coordinate.TYPE_REAL, x, y);
            for (int i = cd.getxTile()-1; i <= cd.getxTile()+1; i++) {
                for (int j = cd.getyTile()-1; j <= cd.getyTile()+1; j++) {
                    try {
                        if (tiles[i][j].isBomb()) {
                            boolean minorX = i*Handler.TILE_SIZE < x && x < (i+1)*Handler.TILE_SIZE;
                            boolean majorX = i*Handler.TILE_SIZE < x+Player.COLLIDER_SIZE && x+Player.COLLIDER_SIZE < (i+1)*Handler.TILE_SIZE;

                            boolean minorY = j*Handler.TILE_SIZE < y && y < (j+1)*Handler.TILE_SIZE;
                            boolean majorY = j*Handler.TILE_SIZE < y+Player.COLLIDER_SIZE && y+Player.COLLIDER_SIZE < (j+1)*Handler.TILE_SIZE;

                            if ((minorX || majorX) && (minorY || majorY)) {
                                    collided = true;
                            }
                        }
                    } catch(Exception e) {
                            // doesnt exist
                    }
                }
            }

            return collided;
        }
        
	public BufferedImage getDisplay() {
		int mapSize = Handler.TILE_SIZE*data.NIC.SIZE_MAP;
		BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		for (int i = 0; i < data.NIC.SIZE_MAP; i++) {
			for (int j = 0; j < data.NIC.SIZE_MAP; j++) {
				int posX = i*Handler.TILE_SIZE;
				int posY = j*Handler.TILE_SIZE;
				g.drawImage(getTiles()[i][j].getImage(), posX, posY, null);
			}
		}
		
		return image;
	}

	/**
	 * @return the tiles
	 */
	public Tile[][] getTiles() {
		return tiles;
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scenes;

import control.KeyHandler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import model.Coordinate;
import model.Handler;
import model.Map;
import model.Player;
import model.Tile;

/**
 *
 * @author Andres
 */
public class Game extends Scene {

	private static int BOMB_TIMER = 50;
	
	private ArrayList<Bomb> activeBombs;
	private double globalTick;
	Map gameMap;
	
	public Game(Handler main, boolean full) {
		super(main, "Game", full);
		this.globalTick = 0;
		activeBombs = new ArrayList<>();
		gameMap = new Map();
		
		for (int i = 0; i < Handler.players.length; i++) {
			if (Handler.players[i].isEnabled()) {
				int tileDiff = 12;
				int x = 1 + (i%2)*tileDiff;
				int y = 1 + ((int)Math.floor(i/2))*tileDiff;
				Coordinate tileCd = new Coordinate(Coordinate.TYPE_TILE,x,y);
				
				Handler.players[i].setCoordinate(tileCd);
			}
		}
	}

	public void start() {
		this.globalTick = 0;
		gameMap = new Map();
		activeBombs.clear();

		for (int i = 0; i < Handler.players.length; i++) {
			if (Handler.players[i].isEnabled()) {
				int tileDiff = 12;
				int x = 1 + (i%2)*tileDiff;
				int y = 1 + ((int)Math.floor(i/2))*tileDiff;
				Coordinate tileCd = new Coordinate(Coordinate.TYPE_TILE,x,y);
				Handler.players[i].setAlive(true);
                                Handler.players[i].setBombsLeft(1);
                                Handler.players[i].setFirePower(2);
				Handler.players[i].setCoordinate(tileCd);
			}
		}
	}
        
	public boolean collision(float x, float y, boolean canCollide) {
		return gameMap.collision(x,y,canCollide);
	}

	public boolean openSpace(float x, float y) {
		return gameMap.openSpace(x,y);
	}

	public boolean burn(float x, float y) {
		return gameMap.burn(x,y);
	}
        
	public int powerCheck(float x, float y) {
		return gameMap.powerCheck(x,y);
        }
        
        
	@Override
	public void receiveKeyAction(int actionCode) {
		int player = (int)Math.floor(actionCode/16);
		int state = actionCode%2;
		int action = actionCode-(player*16)-state;
		
		if (action == KeyHandler.ACTION_A && state == KeyHandler.MOD_RELEASE) {
			// BOMB
			if (Handler.players[player].plantBomb()) {
                            
				plantBomb(Handler.players[player]);
//				Handler.players[player].setBombsLeft(Handler.players[player].getBombsLeft()-1);
			}
		} else {
			//MOVEMENT
			Handler.players[player].move(action+state);
		}
	}

	private void plantBomb(Player owner){
		activeBombs.add(new Bomb(owner, (int)(this.globalTick+BOMB_TIMER)));
		gameMap.getTiles()[owner.getCoordinate().getxTile()][owner.getCoordinate().getyTile()].setObject(Tile.OBJECT_BOMB);
	}
	
	private void explode(int indice) {
		int startX = activeBombs.get(indice).x;
		int startY = activeBombs.get(indice).y;
		activeBombs.get(indice).owner.setBombsLeft(activeBombs.get(indice).owner.getBombsLeft()+1);
		int max = activeBombs.get(indice).firePower;
		activeBombs.remove(indice);
		
		gameMap.getTiles()[startX][startY].setObject(Tile.OBJECT_BOOM+Tile.TYPE_CENTER);
		
		flare(max,startX,startY,1,0);
		flare(max,startX,startY,-1,0);
		flare(max,startX,startY,0,1);
		flare(max,startX,startY,0,-1);
	}
	
	private void flare(int fire, int tileX, int tileY, int deltaX, int deltaY) {
		if (fire > 0) {
			if (gameMap.getTiles()[tileX+deltaX][tileY+deltaY].isSolid()) {
				if (gameMap.getTiles()[tileX+deltaX][tileY+deltaY].isBreakable()) {
					gameMap.getTiles()[tileX+deltaX][tileY+deltaY].destroy();
				}
			} else if (gameMap.getTiles()[tileX+deltaX][tileY+deltaY].isBomb()) {
                                for (int i = 0; i < activeBombs.size(); i++) {
                                    if (tileX+deltaX == activeBombs.get(i).x && tileY+deltaY == activeBombs.get(i).y){
                                        explode(i);
                                    }
                                }
			} else if (!gameMap.getTiles()[tileX+deltaX][tileY+deltaY].isCenterBoom()){
				int type = Tile.OBJECT_BOOM;
				if (deltaY > 0) {
					type = type + Tile.TYPE_ROTATE;
				} else if (deltaY < 0) {
					type = type + (Tile.TYPE_ROTATE*3);
				} else if (deltaX < 0) {
					type = type + (Tile.TYPE_ROTATE*2);
				}
				if (fire == 1) {
					type = type + Tile.TYPE_TIP;
				} else {
					type = type + Tile.TYPE_LONG;
				}

				gameMap.getTiles()[tileX+deltaX][tileY+deltaY].setObject(type);
				flare(fire-1,tileX+deltaX,tileY+deltaY,deltaX,deltaY);
			}
		}
	}

	@Override
	public BufferedImage getDisplay() throws IOException {
		int mapSize = Handler.TILE_SIZE*data.NIC.SIZE_MAP;
		BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		this.globalTick++;
//		boolean doCycle = !
		while (!activeBombs.isEmpty() && activeBombs.get(0).explodeTick <= globalTick) {
			explode(0);
		}
		
		g.drawImage(gameMap.getDisplay(), 0, 0, mapSize, mapSize, null);
		
		for (int i = 0; i < Handler.players.length; i++) {
			if (Handler.players[i].isEnabled() && Handler.players[i].isAlive()) {
				Coordinate playerCoordinates = Handler.players[i].getCoordinate();
				int displayX = (int)playerCoordinates.getxReal()-Player.DELTA_CENTER_X;
				int displayY = (int)playerCoordinates.getyReal()-Player.DELTA_CENTER_Y;
				g.drawImage(Handler.players[i].getDisplay(),displayX,displayY,null);
			}
		}
		
		return image;
	}
	
	private class Bomb {
		int firePower;
		int explodeTick;
		int x;
		int y;
		Player owner;
		
		public Bomb(Player owner, int explodeTick) {
			this.firePower = owner.getFirePower();
			this.explodeTick = explodeTick;
			this.x = owner.getCoordinate().getxTile();
			this.y = owner.getCoordinate().getyTile();
			this.owner = owner;
		}
	}
}

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
	
	public static final int COLLISION_NONE = 0;
	public static final int COLLISION_WALL = 1;
	public static final int COLLISION_BOMB = 2;
	public static final int COLLISION_POWERUP_FIRE = 3;
	public static final int COLLISION_POWERUP_BOMB = 4;

	private static final int BOMB_TIMER = 50;
	
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
				Coordinate tileCd = new Coordinate(x,y,Coordinate.TYPE_TILE);
				Handler.players[i].init(tileCd);
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
				Coordinate tileCd = new Coordinate(x,y,Coordinate.TYPE_TILE);
				Handler.players[i].init(tileCd);
			}
		}
	}
	
	public boolean burnTileCheck(int tileX, int tileY) {
		return gameMap.getTile(tileX, tileY).isBoom();
	}
	
	public boolean solidTileCheck(int tileX, int tileY) {
		return gameMap.getTile(tileX, tileY).isBomb() || gameMap.getTile(tileX, tileY).isSolid();
	}
	
	public int powerTileCheck(int tileX, int tileY) {
		return gameMap.getTile(tileX, tileY).takePowerup();
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
			}
		} else {
			//MOVEMENT
			Handler.players[player].receiveAction(action+state);
		}
	}

	private void plantBomb(Player owner){
		activeBombs.add(new Bomb(owner, (int)(this.globalTick+BOMB_TIMER)));
		if (gameMap.getTile(owner.getTileX(),owner.getTileY()).isBoom()) {
			explode(activeBombs.size()-1);
		} else {
			gameMap.getTile(owner.getTileX(),owner.getTileY()).setBomb();
		}	
	}
	
	private void explode(int indice) {
		int startX = activeBombs.get(indice).xTile;
		int startY = activeBombs.get(indice).yTile;
		activeBombs.get(indice).owner.setBombsLeft(activeBombs.get(indice).owner.getBombsLeft()+1);
		int max = activeBombs.get(indice).firePower;
		activeBombs.remove(indice);
		
		gameMap.getTile(startX,startY).setBoom(0,0);
		
		flare(max,startX,startY,1,0);
		flare(max,startX,startY,-1,0);
		flare(max,startX,startY,0,1);
		flare(max,startX,startY,0,-1);
	}
	
	private void flare(int fire, int tileX, int tileY, int deltaX, int deltaY) {
		if (fire > 0) {
			if (gameMap.getTile(tileX+deltaX,tileY+deltaY).isSolid()) {
				if (gameMap.getTile(tileX+deltaX,tileY+deltaY).isBreakable()) {
					gameMap.getTile(tileX+deltaX,tileY+deltaY).destroyWall();
				}
			} else if (gameMap.getTile(tileX+deltaX,tileY+deltaY).isBomb()) {
				for (int i = 0; i < activeBombs.size(); i++) {
					if (tileX+deltaX == activeBombs.get(i).xTile && tileY+deltaY == activeBombs.get(i).yTile){
						explode(i);
					}
				}
			} else {
				int boomX = deltaX;
				int boomY = deltaY;
				if (fire == 1) {
					if (Math.abs(boomX) == 1) {
						boomX = boomX*2;
					} else {
						boomY = boomY*2;
					}
				}
				gameMap.getTile(tileX+deltaX, tileY+deltaY).setBoom(boomX, boomY);
//				gameMap.getTile(tileX+deltaX,tileY+deltaY).setObject(type);
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
		while (!activeBombs.isEmpty() && activeBombs.get(0).explodeTick <= globalTick) {
			explode(0);
		}
		
		g.drawImage(gameMap.getDisplay(), 0, 0, mapSize, mapSize, null);
		
		for (int i = 0; i < Handler.players.length; i++) {
			if (Handler.players[i].isEnabled() && Handler.players[i].isAlive()) {
				Player thisPlayer = Handler.players[i];
				g.drawImage(thisPlayer.getDisplay(),thisPlayer.getImageX(),thisPlayer.getImageY(),null);
			}
		}
		
		return image;
	}
	
	private class Bomb {
		int firePower;
		int explodeTick;
		int xTile;
		int yTile;
		Player owner;
		
		public Bomb(Player owner, int explodeTick) {
			this.firePower = owner.getFirePower();
			this.explodeTick = explodeTick;
			this.xTile = owner.getTileX();
			this.yTile = owner.getTileY();
			this.owner = owner;
		}
	}
}

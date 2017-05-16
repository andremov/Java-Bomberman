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

	public static final int SCENE_ID = 2;
	
	public static final int COLLISION_NONE = 0;
	public static final int COLLISION_WALL = 1;
	public static final int COLLISION_BOMB = 2;
	public static final int COLLISION_POWERUP_FIRE = 3;
	public static final int COLLISION_POWERUP_BOMB = 4;

	private static final int BOMB_TIMER = 50;
	
	private Bomb[] activeBombs;
	private int numBombs;
	private double globalTick;
	Map gameMap;
	
	public Game() {
		super("Game", true);
		this.globalTick = 0;
		activeBombs = new Bomb[100];
		numBombs = 0;
		gameMap = new Map();
		
		int tileDiff = 12;
		int x = 1 + (Handler.playerID%2)*tileDiff;
		int y = 1 + ((int)Math.floor(Handler.playerID/2))*tileDiff;
		Coordinate tileCd = new Coordinate(x,y,Coordinate.TYPE_TILE);
		Handler.getPlayer().init(tileCd);
		
		for (int i = 0; i < Handler.NUM_PLAYERS; i++) {
			if (Handler.players[i].isEnabled()) {
				Handler.players[i].setAlive(true);
			}
		}
	}
	
	public void init(String mapString) {
		for (int i = 0; i < mapString.split(",").length; i++) {
			int x = i%bomberman.Bomberman.SIZE_MAP;
			int y =(int)Math.floor(i/bomberman.Bomberman.SIZE_MAP);
			gameMap.getTile(x, y).setObject(Integer.parseInt(mapString.split(",")[i]));
		}
	}
	
	@Override
	public int getID() {
		return SCENE_ID;
	}
	
	@Override
	public String sceneInit() {
		String mapString = "";
		for (int x = 0; x < bomberman.Bomberman.SIZE_MAP; x++) {
			for (int y = 0; y < bomberman.Bomberman.SIZE_MAP; y++) {
				mapString = mapString + gameMap.getTile(y, x).getObject() + ",";
			}
		}
		return SCENE_ID+":"+mapString;
	}
	
	public void applyMapChanges(String changes) {
		if (!changes.isEmpty()) {
			for (int i = 0; i < changes.split(";").length; i++) {
				String change = changes.split(";")[i];
				int x = Integer.parseInt(change.split("=")[0].split(",")[0]);
				int y = Integer.parseInt(change.split("=")[0].split(",")[1]);
				int obj = Integer.parseInt(change.split("=")[1]);
				gameMap.getTile(x, y).setObject(obj);
			}
		}
	}
	
	public void applyServerMapChanges(String changes) {
		if (!changes.isEmpty()) {
			for (int i = 0; i < changes.split(";").length; i++) {
				String change = changes.split(";")[i];
				int x = Integer.parseInt(change.split("=")[0].split(",")[0]);
				int y = Integer.parseInt(change.split("=")[0].split(",")[1]);
				int obj = Integer.parseInt(change.split("=")[1]);
				gameMap.getTile(x, y).setObject(obj);
				sendChange(x, y);
			}
		}
	}
	
	private void sendChange(int x, int y) {
		String change = x+","+y+"="+gameMap.getTile(x, y).getObject()+";";
		Handler.addMapChange(change);
	}
	
	private void clearBombs() {
		for (int i = 0; i < numBombs; i++) {
			activeBombs[i] = null;
		}
		numBombs = 0;
	}
	
	private void addBomb(Bomb newBomb) {
		activeBombs[numBombs] = newBomb;
		numBombs++;
	}
	
	private void removeBomb(int index) {
		for (int i = 0; i < numBombs; i++) {
			activeBombs[i] = activeBombs[i+1];
		}
		numBombs--;
	}
	
	public void start() {
		this.globalTick = 0;
		gameMap = new Map();
		clearBombs();

		for (int i = 0; i < Handler.NUM_PLAYERS; i++) {
			if (Handler.players[i].isEnabled()) {
				int tileDiff = 12;
				int x = 1 + (i%2)*tileDiff;
				int y = 1 + ((int)Math.floor(i/2))*tileDiff;
				Coordinate tileCd = new Coordinate(x,y,Coordinate.TYPE_TILE);
				((model.RealPlayer)Handler.players[i]).init(tileCd);
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
		int powerup = gameMap.getTile(tileX, tileY).takePowerup();
		sendChange(tileX,tileY);
		return powerup;
	}
        
	@Override
	public void receiveKeyAction(int actionCode) {
		int state = actionCode%2;
		int action = actionCode-state;
		
		if (action == KeyHandler.ACTION_A && state == KeyHandler.MOD_RELEASE) {
			// BOMB
			if (Handler.getPlayer().plantBomb()) {
                
				plantBomb(Handler.getPlayer());
			}
		} else {
			//MOVEMENT
			Handler.getPlayer().receiveAction(action+state);
		}
	}

	private void plantBomb(model.RealPlayer owner){
		addBomb(new Bomb(owner, (int)(this.globalTick+BOMB_TIMER)));
		if (gameMap.getTile(owner.getTileX(),owner.getTileY()).isBoom()) {
			explode(numBombs-1);
		} else {
			gameMap.getTile(owner.getTileX(),owner.getTileY()).setObject(Tile.OBJECT_BOMB);
			sendChange(owner.getTileX(),owner.getTileY());
		}
	}
	
	private void explode(int index) {
		int startX = activeBombs[index].xTile;
		int startY = activeBombs[index].yTile;
		activeBombs[index].owner.setBombsLeft(activeBombs[index].owner.getBombsLeft()+1);
		int max = activeBombs[index].firePower;
		removeBomb(index);
		
		gameMap.getTile(startX,startY).setBoom(0,0);
		sendChange(startX,startY);
		
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
				for (int i = 0; i < numBombs; i++) {
					if (tileX+deltaX == activeBombs[i].xTile && tileY+deltaY == activeBombs[i].yTile){
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
			sendChange(tileX+deltaX,tileY+deltaY);
		}
	}

	@Override
	public BufferedImage getDisplay() throws IOException {
		int mapSize = bomberman.Bomberman.TILE_SIZE*bomberman.Bomberman.SIZE_MAP;
		BufferedImage image = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		this.globalTick++;
		while (numBombs > 0 && activeBombs[0].explodeTick <= globalTick) {
			explode(0);
		}
		
		g.drawImage(gameMap.getDisplay(), 0, 0, mapSize, mapSize, null);
		
		for (int i = 0; i < Handler.NUM_PLAYERS; i++) {
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
		model.RealPlayer owner;
		
		public Bomb(model.RealPlayer owner, int explodeTick) {
			this.firePower = owner.getFirePower();
			this.explodeTick = explodeTick;
			this.xTile = owner.getTileX();
			this.yTile = owner.getTileY();
			this.owner = owner;
		}
	}
}

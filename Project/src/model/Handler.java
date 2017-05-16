/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package model;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import scenes.Connection;
import scenes.Game;
import scenes.Lobby;
import scenes.Scene;

/**
 *
 * @author Andres
 */
public class Handler {

	public static final int NUM_PLAYERS = 4;
	
	public static Scene currentScene;
	public static Player[] players;
	public static Server server;
	public static Client client;
	public static int playerID;
	public static String pendingMapChanges;
	
	public Handler() {
		client = new Client();
		currentScene = (new scenes.Connection());
		pendingMapChanges = "";
	}
	
	/**
	 * Returns the current scene display.
	 * @return
	 * @throws IOException 
	 */
	public static BufferedImage getDisplay() throws IOException {
		int size = bomberman.Bomberman.SCREEN_SIZE;
		BufferedImage img = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		img = currentScene.getDisplay();
		return img;
	}
	
	/**
	 * Returns the current scene cast as Game.
	 * @return 
	 */
	public static scenes.Game getGame() {
		scenes.Game g = null;
		if (currentScene instanceof scenes.Game) {
			g = (scenes.Game)currentScene;
		}
		return g;
	}
	
	/**
	 * Starts game.
	 * @param wins
	 * @param rounds 
	 */
	public static void engageGame() {
		if (server != null) {
                        currentScene = new Game();
//                        System.out.println("GAME");
		}
	}
	
	/**
	 * Checks if game has ended.
	 * If no player is alive, goes to lobby.
	 * If one player is alive, gives that player a win, and goes to lobby.
	 */
	public static void checkGameWin() {
		if (currentScene instanceof Game) {
			int numActivePlayers = 0;
			for (int i = 0; i < players.length; i++) {
				if (players[i].isAlive() && players[i].isEnabled()) {
					numActivePlayers++;
				}
			}
			if (numActivePlayers == 1) {
				int index = 0;
				while (!players[index].isAlive() || !players[index].isEnabled()) {
					index++;
				}
				server.wins[index] = server.wins[index] + 1;
				server.rounds = server.rounds+1;

//				try {
//					Thread.sleep(100);
//				} catch (Exception e) { }

				for (int i = 0; i < NUM_PLAYERS; i++) {
					players[i].defaultAnimation();
				}
				currentScene = new Lobby(server.getAddress(), server.wins, server.rounds);
			} else if (numActivePlayers == 0) {

//				try {
//					Thread.sleep(100);
//				} catch (Exception e) { }

				for (int i = 0; i < NUM_PLAYERS; i++) {
					players[i].defaultAnimation();
				}
				currentScene = new Lobby(server.getAddress(), server.wins, server.rounds);
			}
		}
	}
	
	// PLAYERS
	/**
	 * Initializes all players as ghost players.
	 */
	public static void initPlayers() {
		if (players == null) {
			players = new Player[4];
			for (int i = 0; i < NUM_PLAYERS; i++) {
				players[i] = new GhostPlayer();
			}
//			System.out.println("All players are ghosts.");
		}
	}
	
	/**
	 * Adds a player to the server.
	 * @param socket
	 * @return 
	 */
	public static String addPlayer(java.net.Socket socket) {
		int index = firstAvailableIndex();
		int color = nextColor(1);
		players[index].setColor(color);
		players[index].setEnabled(true);
		players[index].setSocket(socket);
//		System.out.println("Adding player "+index+"...");
		return index+","+color;
	}
	
	/**
	 * Sets the given player to a real player.
	 * @param info 
	 */
	public static void setReal(String info) {
		int index = Integer.parseInt(info.split(",")[0]);
		int color = Integer.parseInt(info.split(",")[1]);
		playerID = index;
		players[index] = new RealPlayer();
		players[index].setColor(color);
		players[index].setEnabled(true);
//		System.out.println("Player "+index+" is real player.");
	}
	
	/**
	 * Sets the given index to a ghost player.
	 * @param index 
	 */
	public static void setGhost(int index) {
		players[index] = new GhostPlayer();
//		System.out.println("Player "+index+"is now ghost.");
	}
	
	/**
	 * Returns this client's real player.
	 * @return 
	 */
	public static RealPlayer getPlayer() {
		return (RealPlayer) players[playerID];
	}
	
	// CHANGES
	/**
	 * Adds a change to the pending changes.
	 * @param change 
	 */
	public static void addMapChange(String change) {
		if (!pendingMapChanges.contains("?")) {
			pendingMapChanges = pendingMapChanges + change;
		}
	}
	
	/**
	 * Method called when a client receives changes from a server.
	 * @param changes 
	 */
	public static void clientReceiveChanges(String changes) {
		
		if (changes.contains("!")) {
			int newScene = Integer.parseInt(changes.split(":")[0].split("!")[1]);
			if (newScene == Lobby.SCENE_ID) {
				String[] initInfo = changes.split(":")[1].split(";");
				int[] wins = new int[NUM_PLAYERS];
				for (int i = 0; i < initInfo[1].split(",").length; i++) {
					wins[i] = Integer.parseInt(initInfo[1].split(",")[i]);
				}
				currentScene = new Lobby(initInfo[0],wins,Integer.parseInt(initInfo[2]));
			} else if (newScene == Game.SCENE_ID) {
				currentScene = new Game();
				getGame().init(changes.split(":")[1]);
			}
		} else {
			int sceneID = Integer.parseInt(changes.split(":")[0]);
			if (sceneID != currentScene.getID()) {
				pendingMapChanges = "?";
			} else {
				String sceneChanges = changes.split(":")[1];
				if (currentScene instanceof Lobby) {
					String[] playerChanges = sceneChanges.split(";");
					for (int i = 0; i < playerChanges.length; i++) {
						if (i != playerID) {
							int colorCode = Integer.parseInt(playerChanges[i].split(",")[0]);
							if (colorCode == 0) {
								Handler.players[i].setEnabled(false);
							} else {
								Handler.players[i].setEnabled(true);
								Handler.players[i].setColor(colorCode);
							}
							boolean ready = playerChanges[i].split(",")[1].compareTo("Y")==0;
							((Lobby)currentScene).setReady(i, ready);
						}
					}
				} else if (currentScene instanceof Game) {
					String mapChanges = sceneChanges.split("#")[0];
					String playerChanges = sceneChanges.split("#")[1];
					getGame().applyMapChanges(mapChanges);
					for (int i = 0; i < NUM_PLAYERS; i++) {
						if (i != playerID) {
							if (playerChanges.split(";")[i].compareTo("OFF")==0) {
								Handler.players[i].setEnabled(false);
							} else if (playerChanges.split(";")[i].compareTo("DEAD")==0) {
								Handler.players[i].setAlive(false);
							} else {
								int x = Integer.parseInt(playerChanges.split(";")[i].split(",")[0]);
								int y = Integer.parseInt(playerChanges.split(";")[i].split(",")[1]);
								int animation = Integer.parseInt(playerChanges.split(";")[i].split(",")[2]);
								boolean moving = playerChanges.split(";")[i].split(",")[3].compareTo("Y") == 0;
								players[i].setRawX(x);
								players[i].setRawY(y);
								players[i].setAnimation(animation);
								players[i].setMoving(moving);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Returns this client's changes.
	 * @return 
	 */
	public static String clientSendChanges() {
		String changes = currentScene.getID()+":";
		
		if (!pendingMapChanges.contains("?")) {
			if (currentScene instanceof Lobby) {
				String ready = "N";
				if (((Lobby)currentScene).getReady(playerID)) {
					ready = "Y";
				}
				changes = changes + players[playerID].getColor() + "," + ready;
			} else if (currentScene instanceof Game) {
				changes = changes+pendingMapChanges;
				pendingMapChanges = "";
				
				String player;
				if (getPlayer().isAlive()) {
					player = getPlayer().getRawX()+","+getPlayer().getRawY()+",";
					String move = getPlayer().isMoving() ? "Y" : "N";
					player = player + getPlayer().getAnimation()+","+move + ";";
				} else {
					player = "DEAD;";
				}
				changes = changes + "#" + player;
			}
		} else {
			pendingMapChanges = "";
			changes = "?";
		}
		
		return changes;
	}
	
	/**
	 * Method called after the server has received all client changes.
	 * @param changes 
	 */
	public static void serverReceiveChanges(String[] changes) {
		
		for (int i = 0; i < changes.length; i++) {
			if (changes[i] == null) {
				changes[i] = "";
			}
			if (!changes[i].isEmpty()) {
				if (Integer.parseInt(changes[i].split(":")[0]) == currentScene.getID()) {
					changes[i] = changes[i].split(":")[1];
				} else {
					changes[i] = "";
				}
			}
		}
		
		if (currentScene instanceof Lobby) {
			for (int i = 0; i < changes.length; i++) {
				if (!changes[i].isEmpty() && i != playerID) {
					int colorCode = Integer.parseInt(changes[i].split(",")[0]);
					Handler.players[i].setColor(colorCode);
					boolean ready = changes[i].split(",")[1].compareTo("Y")==0;
					((Lobby)currentScene).setReady(i, ready);
				}
			}
			((Lobby)currentScene).checkReady();
		} else if (currentScene instanceof Game) {
			for (int i = 0; i < changes.length; i++) {
				if (!changes[i].isEmpty()) {
					String mapChanges = changes[i].split("#")[0];
					String playerChanges = changes[i].split("#")[1];
					getGame().applyServerMapChanges(mapChanges);
					if (i != playerID) {
						if (playerChanges.split(";")[0].compareTo("DEAD")==0) {
							players[i].setAlive(false);
						} else {
							int x = Integer.parseInt(playerChanges.split(";")[0].split(",")[0]);
							int y = Integer.parseInt(playerChanges.split(";")[0].split(",")[1]);
							int animation = Integer.parseInt(playerChanges.split(";")[0].split(",")[2]);
							boolean moving = playerChanges.split(";")[0].split(",")[3].compareTo("Y") == 0;

							players[i].setRawX(x);
							players[i].setRawY(y);
							players[i].setAnimation(animation);
							players[i].setMoving(moving);
						}
					}
				}
			}
			checkGameWin();
		}
	}
	
	/**
	 * Returns the server's changes.
	 * @return 
	 */
	public static String serverSendChanges() {
		String changes = currentScene.getID()+":";
		
		if (currentScene instanceof Lobby) {
			for (int i = 0; i < NUM_PLAYERS; i++) {
				
				String ready = "N";
				if (((Lobby)currentScene).getReady(i)) {
					ready = "Y";
				}
				changes = changes + players[i].getColor() + "," + ready + ";";
				
			}
		} else if (currentScene instanceof Game) {
			changes = changes + pendingMapChanges;
			pendingMapChanges = "";
			changes = changes + "#";
			for (int i = 0; i < NUM_PLAYERS; i++) {
				String player;
				if (players[i].isEnabled()) {
					if (players[i].isAlive()) {
						player = players[i].getRawX()+","+ players[i].getRawY()+",";
						String move =  players[i].isMoving() ? "Y" : "N";
						player = player +  players[i].getAnimation()+","+move+";";
					} else {
						player = "DEAD;";
					}
				} else {
					player = "OFF;";
				}
				changes = changes + player;
			}
		}
		
		return changes;
	}
	
	
	// INPUTS
	/**
	 * Receives a KeyEvent, parses it, and sends it to the non-game current scene.
	 * @param keyCode 
	 */
	public static void otherKey(KeyEvent keyCode) {
		int keyValue = Scene.CODE_INVALID;
		if (currentScene instanceof Connection) {
			if (keyCode.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				keyValue = Connection.CODE_DEL;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_ENTER) {
				keyValue = Connection.CODE_ENTER;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_PERIOD) {
				keyValue = Connection.CODE_PERIOD;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_DOWN || keyCode.getKeyCode() == KeyEvent.VK_UP) {
				keyValue = Connection.CODE_SWITCH;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_0) {
				keyValue = 0;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_1) {
				keyValue = 1;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_2) {
				keyValue = 2;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_3) {
				keyValue = 3;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_4) {
				keyValue = 4;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_5) {
				keyValue = 5;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_6) {
				keyValue = 6;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_7) {
				keyValue = 7;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_8) {
				keyValue = 8;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_9) {
				keyValue = 9;
			}
		} else if (currentScene instanceof Lobby) {
			if (keyCode.getKeyCode() == KeyEvent.VK_DOWN || keyCode.getKeyCode() == KeyEvent.VK_UP) {
				keyValue = Lobby.CODE_SWITCH;
			} else if (keyCode.getKeyCode() == KeyEvent.VK_ENTER) {
				keyValue = Lobby.CODE_ENTER;
			}
		}
		currentScene.receiveKeyAction(keyValue);
	}
	
	/**
	 * Receives a key action code and responds accordingly.
	 * @param actionCode 
	 */
	public static void receiveKeyAction(int actionCode) {
		try {
			if (currentScene instanceof Game) {
				currentScene.receiveKeyAction(actionCode);
			}
		} catch (Exception e) { }
	}
	
	
	// PLAYER MANAGEMENT
	/**
	 * Returns the next available color.
	 * @param startingColor
	 * @return 
	 */
	public static int nextColor(int startingColor) {
		int color = startingColor;
		int searchIndex = 0;
		while (searchIndex < 4) {
			if (Handler.players[searchIndex].getColor() == color) {
				color++;
				searchIndex = 0;
				if (color > 5) {
					color = 1;
				}
			} else {
				searchIndex++;
			}
		}
		return color;
	}
	
	/**
	 * Returns the first available index in the player array.
	 * @return 
	 */
	private static int firstAvailableIndex() {
		int index = 0;
		while (index < NUM_PLAYERS && players[index].isEnabled()) {
			index++;
		}
		return index;
	}
	
	/**
	 * Returns true if there is still space for a new player.
	 * @return 
	 */
	public static boolean spaceAvailable() {
		boolean canAdd = false;
		for (int i = 0; i < NUM_PLAYERS; i++) {
			canAdd = canAdd || !players[i].isEnabled();
		}
		return canAdd;
	}
	
}

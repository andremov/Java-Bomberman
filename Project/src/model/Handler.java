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
import scenes.Scene;

/**
 *
 * @author Andres
 */
public class Handler {

	public static Scene currentScene;
	public static Player[] players;
	public static Server server;
	public static Client client;
	public static int playerID;
	public static String address;
	public static String[] clientChanges;
	
	/**
	 * Build game.
	 */
	public Handler() {
		players = new Player[4];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player();
		}
		client = new Client();
		currentScene = (new scenes.Connection());
	}
	
	public static BufferedImage getDisplay() throws IOException {
		int size = bomberman.Bomberman.SCREEN_SIZE;
		BufferedImage img = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		img = currentScene.getDisplay();
		return img;
	}
	
	public static void saveClientChange(int index, String change) {
		clientChanges[index] = change;
	}
	
	public static void applyClientChanges() {
		for (int i = 0; i < clientChanges.length; i++) {
//			if (clientChanges[i] == null) {
//				clientChanges[i] = "";
//				System.out.println("Player "+i+" null changes.");
//			}
			if (!clientChanges[i].isEmpty()) {
				applyChanges(clientChanges[i]);
				clientChanges[i] = "";
			}
		}
	}
	
	public static String getChanges() {
		String changes = "";
		if (getGame() != null) {
			changes = changes + getGame().getChanges();
			changes = changes + "!PLAYER!";
			changes = changes + playerID+"="+players[playerID].getRawX()+","+players[playerID].getRawX()+";";
		} else {
			changes = changes + "PLAYER!" + playerID +"="+ "COLOR,"+Handler.players[playerID].getColor()+";";
		}
		
		return changes;
	}
	
	public static String getChangesServer() {
		String changes = "";
		if (getGame() != null) {
			changes = changes + getGame().getChanges();
			changes = changes + "!PLAYER!";
			for (int i = 0; i < players.length; i++) {
				changes = changes + i+"=";
				if (players[i].isEnabled()) {
					changes = changes+players[i].getRawX()+","+players[i].getRawX()+";";
				} else {
					changes = changes+"OFF;";
				}
			}
		} else {
			changes = changes + "PLAYER!";
			for (int i = 0; i < players.length; i++) {
				changes = changes + i +"=";
				if (players[i].isEnabled()) {
					changes = changes+ "COLOR,"+Handler.players[i].getColor()+";";
				} else {
					changes = changes+"OFF;";
				}
			}
		}
		
		return changes;
	}
	
	public static void applyChanges(String changes) {
		/*
		CHANGES MODEL:
		MAP!
		0,0=1;
		2,2=3;
		!PLAYER!
		1=0,0;
		*/
		if (changes.split("!")[0].compareTo("MAP") == 0) {
			getGame().applyChanges(changes.split("!")[1]);
			
			String playerChanges = changes.split("!")[3];
			int changeID = Integer.parseInt(playerChanges.split("=")[0]);
			String values = playerChanges.split("=")[1];
			if (playerChanges.contains("COLOR")) {
				Handler.players[changeID].setColor(Integer.parseInt(values.split(",")[1]));
			} else {
				Handler.players[changeID].setRawX(Integer.parseInt(values.split(",")[0]));
				Handler.players[changeID].setRawY(Integer.parseInt(values.split(",")[1]));
			}
		} else if (changes.split("!")[0].compareTo("PLAYER")==0) {
			String playerChanges = changes.split("!")[1];
			for (int i = 0; i < playerChanges.split(";").length; i++) {
				
				int changeID = Integer.parseInt(playerChanges.split(";")[i].split("=")[0]);
				String values = playerChanges.split(";")[i].split("=")[1];
				if (values.compareTo("OFF")== 0) {
					if (Handler.players[changeID].isEnabled()){
						Handler.players[changeID].setEnabled(false);
					}
				} else {
					if (!Handler.players[changeID].isEnabled()){
						Handler.players[changeID].setEnabled(true);
					}
					if (playerChanges.contains("COLOR")) {
						int cc = Integer.parseInt(values.split(",")[1]);
						Handler.players[changeID].setColor(cc);
					} else {
						Handler.players[changeID].setRawX(Integer.parseInt(values.split(",")[0]));
						Handler.players[changeID].setRawY(Integer.parseInt(values.split(",")[1]));
					}
				}
			}
		}
	}
	
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
		} else if (currentScene instanceof scenes.Lobby) {
			if (keyCode.getKeyCode() == KeyEvent.VK_DOWN || keyCode.getKeyCode() == KeyEvent.VK_UP) {
				keyValue = scenes.Lobby.CODE_SWITCH;
			}
		}
		currentScene.receiveKeyAction(keyValue);
	}
	
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
	
	public static void receiveKeyAction(int actionCode) {
		try {
			if (!(currentScene instanceof Connection)) {
				currentScene.receiveKeyAction(actionCode);
			}
		} catch (Exception e) {
			
		}
	}
	
	private static int firstAvailableIndex() {
		int index = 0;
		while (index < players.length && players[index].isEnabled()) {
			index++;
		}
		return index;
	}
	
	public static boolean spaceAvailable() {
		boolean canAdd = false;
		for (int i = 0; i < players.length; i++) {
			canAdd = canAdd || !players[i].isEnabled();
		}
		return canAdd;
	}
	
	public static int addPlayer(java.net.Socket socket) {
		int index = firstAvailableIndex();
		players[index].setColor(nextColor(1));
		players[index].setEnabled(true);
		players[index].setSocket(socket);
		return index;
	}
	public static void removePlayer(int index) {
		players[index] = new Player();
	}
        
	public static model.Game getGame() {
		model.Game g = null;
		if (currentScene instanceof model.Game) {
			g = (model.Game)currentScene;
		}
		return g;
	}
	
}

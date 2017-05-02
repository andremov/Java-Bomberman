/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import model.Handler;

/**
 *
 * @author Andres
 */
public class KeyHandler extends KeyAdapter {
	
	private final static boolean doExitCheck = false;
	
	public final static int ACTION_NONE = -100;
	
	public final static int ACTION_UP = 0;
	public final static int ACTION_DOWN = 2;
	public final static int ACTION_LEFT = 4;
	public final static int ACTION_RIGHT = 6;
	
	public final static int ACTION_A = 8;
	public final static int ACTION_B = 10;
	public final static int ACTION_START = 12;
	public final static int ACTION_BACK = 14;
	
	public final static int MOD_PRESS = 0;
	public final static int MOD_RELEASE = 1;
	
	public final static int MOD_PLAYER = 16;
	
	private void parseKey(KeyEvent key, int state) {
		boolean acceptedKey = false;
		int playerMod = 0;
		int actionMod = ACTION_NONE;
		
		if (key.getKeyCode() == KeyEvent.VK_UP || key.getKeyCode() == KeyEvent.VK_W) {
			acceptedKey = true;
			actionMod = ACTION_UP;
			if (key.getKeyCode() == KeyEvent.VK_UP) {
				playerMod = MOD_PLAYER;
			}
		} else if (key.getKeyCode() == KeyEvent.VK_DOWN || key.getKeyCode() == KeyEvent.VK_S) {
			acceptedKey = true;
			actionMod = ACTION_DOWN;
			if (key.getKeyCode() == KeyEvent.VK_DOWN) {
				playerMod = MOD_PLAYER;
			}
		} else if (key.getKeyCode() == KeyEvent.VK_LEFT || key.getKeyCode() == KeyEvent.VK_A) {
			acceptedKey = true;
			actionMod = ACTION_LEFT;
			if (key.getKeyCode() == KeyEvent.VK_LEFT) {
				playerMod = MOD_PLAYER;
			}
		} else if (key.getKeyCode() == KeyEvent.VK_RIGHT || key.getKeyCode() == KeyEvent.VK_D) {
			acceptedKey = true;
			actionMod = ACTION_RIGHT;
			if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
				playerMod = MOD_PLAYER;
			}
//		} else if (key.getKeyCode() == KeyEvent.VK_K) {
//			acceptedKey = true;
//			sendAction = ACTION_B;
		} else if (key.getKeyCode() == KeyEvent.VK_F || key.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			acceptedKey = true;
			actionMod = ACTION_A;
			if (key.getKeyCode() == KeyEvent.VK_NUMPAD3) {
				playerMod = MOD_PLAYER;
			}
//		} else if (key.getKeyCode() == KeyEvent.VK_ENTER) {
//			acceptedKey = true;
//			sendAction = ACTION_START;
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Handler.getGame().start();
//			acceptedKey = true;
//			sendAction = ACTION_BACK;
//			if (doExitCheck) {
//				String[] options = {"SI","NO"};
//				int n = JOptionPane.showOptionDialog(null,"Desea cerrar?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,"");
//				if(n == 0) {
//					System.exit(0);
//				}
//			} else {
//				System.exit(0);
//			}
		}
		
		if (acceptedKey) {
			Handler.currentScene.receiveKeyAction(state+playerMod+actionMod);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent key) {
		parseKey(key, MOD_RELEASE);

	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		parseKey(key, MOD_PRESS);
	}
}

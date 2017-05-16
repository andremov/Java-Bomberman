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

/**
 *
 * @author Andres
 */
public class KeyHandler extends KeyAdapter {
	
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
		int actionMod = ACTION_NONE;
		
		if (key.getKeyCode() == KeyEvent.VK_W) {
			acceptedKey = true;
			actionMod = ACTION_UP;
		} else if (key.getKeyCode() == KeyEvent.VK_S) {
			acceptedKey = true;
			actionMod = ACTION_DOWN;
		} else if (key.getKeyCode() == KeyEvent.VK_A) {
			acceptedKey = true;
			actionMod = ACTION_LEFT;
		} else if (key.getKeyCode() == KeyEvent.VK_D) {
			acceptedKey = true;
			actionMod = ACTION_RIGHT;
		} else if (key.getKeyCode() == KeyEvent.VK_F) {
			acceptedKey = true;
			actionMod = ACTION_A;
//		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
//			Handler.getGame().start();
		}
		
		if (acceptedKey) {
			model.Handler.receiveKeyAction(state+actionMod);
		} else if (state == MOD_RELEASE) {
			model.Handler.otherKey(key);
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

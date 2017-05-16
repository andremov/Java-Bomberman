/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author Andres
 */
public abstract class Player {
	
	private static final int DELTA_CENTER_X = 8;
	private static final int DELTA_CENTER_Y = 19;
			
	public static int ANIM_MOVE_DOWN = 1;
	public static int ANIM_MOVE_LEFT = 2;
	public static int ANIM_MOVE_RIGHT = 3;
	public static int ANIM_MOVE_UP = 4;
	public static int ANIM_DEAD = 4;
	
	// ANIMATION
	private int animation;
	private boolean moving;
	private int frame;
	
	// PLAYER INFO
	private boolean enabled;
	private boolean alive;
	private int color;
	private Coordinate coordinate;
	
	// SOCKETS
	protected java.net.Socket socket;
    private BufferedReader in;
    private PrintWriter out;
	public int timeOuts;

	public Player() {
		this.setEnabled(false);
		this.timeOuts = 0;
		this.coordinate = new Coordinate(-10,-10,Coordinate.TYPE_REAL);
		defaultAnimation();
	}
	
	public abstract BufferedImage getDisplay();
	
	public void defaultAnimation() {
		this.setAnimation(ANIM_MOVE_DOWN);
		this.setFrame(2);
	}
	
	/**
	 * @param socket the socket to set
	 */
	public void setSocket(java.net.Socket socket) {
		try {
			this.socket = socket;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) { }
	}

	/**
	 * @return the in
	 */
	public BufferedReader getIn() {
		return in;
	}

	/**
	 * @return the out
	 */
	public PrintWriter getOut() {
		return out;
	}
	
	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return the animation
	 */
	public int getAnimation() {
		return animation;
	}

	/**
	 * @param animation the animation to set
	 */
	public void setAnimation(int animation) {
		this.animation = animation;
	}

	/**
	 * @return the moving
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * @param moving the moving to set
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/**
	 * @return the frame
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}
        
	public int getRawX() {
		return this.getCoordinate().getRealX();
	}
	
	public int getRawY() {
		return this.getCoordinate().getRealY();
	}
	
	public void setRawX(int x) {
		this.getCoordinate().setRealX(x);
	}
	
	public void setRawY(int y) {
		this.getCoordinate().setRealY(y);
	}
	
	public int getImageX() {
		return this.getCoordinate().getRealX()-DELTA_CENTER_X;
	}
	
	public int getImageY() {
		return this.getCoordinate().getRealY()-DELTA_CENTER_Y;
	}
	
	public int getTileX() {
		return this.getCoordinate().getTileX();
	}
	
	public int getTileY() {
		return this.getCoordinate().getTileY();
	}

	/**
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate the coordinate to set
	 */
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import data.NIC;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public class Bomb extends TileObject {
	
	public static int MAX_SPRITE = 3;
	
	private int explosionCountdown;
	private int currentFrame;
	private int firePower;
	private Player owner;

	public Bomb(Player owner) {
		this.owner = owner;
		this.firePower = this.owner.getFirePower();
		this.currentFrame = 1;
		this.explosionCountdown = 100;
	}
	
	@Override
	public void tick() {
		currentFrame++;
		if (currentFrame > MAX_SPRITE) {
			currentFrame = 1;
		}
		explosionCountdown--;
	}
	
	@Override
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(Handler.TILE_SIZE, Handler.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.drawImage(NIC.getBombFrame(currentFrame), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
		
		return image;
	}

	/**
	 * @return the explosionCountdown
	 */
	public boolean blewUp() {
		return (explosionCountdown <= 0);
	}

	/**
	 * @return the firePower
	 */
	public int getFirePower() {
		return firePower;
	}

	/**
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}
	
}

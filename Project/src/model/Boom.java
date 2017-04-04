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
public class Boom extends TileObject {

	public static int TYPE_CENTER = 0;
	public static int TYPE_BODY = 1;
	public static int TYPE_TIP = 1;
	
	public static int ROTATION_0 = 0;
	public static int ROTATION_90 = 1;
	
	public static int MAX_SPRITE = 5;
	
	int currentFrame;
	int type;
	int rotation;

	public Boom(int type, int rotation) {
		this.type = type;
		this.rotation = rotation;
		currentFrame = 1;
	}
	
	
	@Override
	public void tick() {
		currentFrame++;
		if (currentFrame > MAX_SPRITE) {
			// remove (?)
		}
	}
	
	@Override
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(Handler.TILE_SIZE, Handler.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		int randomID = (int)(Math.ceil(Math.random()*4));
		
		g.drawImage(NIC.getExplosionFrame(currentFrame, type, rotation, randomID), 0, 0, Handler.TILE_SIZE, Handler.TILE_SIZE, null);
		
		
		return image;
	}
	
}

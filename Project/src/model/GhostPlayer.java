/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public class GhostPlayer extends Player {

	public GhostPlayer() {
		super();
	}
	
	/**
	 * Gets the image representation of this player.
	 * @return 
	 */
	@Override
	public BufferedImage getDisplay() {
		BufferedImage image = new BufferedImage(16, 25, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		if (this.isMoving()) {
			this.setFrame(this.getFrame()+1);
			if (this.getFrame() > 3) {
				this.setFrame(1);
			}
		} else {
			this.setFrame(2);
		}
		
		int trueColor = getColor();
		if (!isEnabled()) {
			trueColor = 0;
		}
		
		g.drawImage(data.NIC.getPlayerFrame(trueColor,this.getAnimation(),this.getFrame()), 0, 0, 16, 25, null);
		
		return image;
	}
        
}

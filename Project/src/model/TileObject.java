/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public abstract class TileObject {
	
	public abstract void tick();
	public abstract BufferedImage getImage();
	
}

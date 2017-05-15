/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

/**
 *
 * @author Andres
 */
public class Bomberman {
	
    public static int PORT = 7100;
	public static final int SCREEN_SIZE = 600;
	public static final int TILE_SIZE = 16;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		data.NIC.loadAllData();
		new model.Handler();
	}
	
}

/*
 *  Pokemon Violet - A University Project by Andres Movilla
 *  Pokemon COPYRIGHT 2002-2016 Pokemon.
 *  COPYRIGHT 1995-2016 Nintendo/Creatures Inc./GAME FREAK inc. TRADEMARK, REGISTERED TRADEMARK
 *  and Pokemon character names are trademarks of Nintendo.
 *  No copyright or trademark infringement is intended in using Pokemon content on Pokemon Violet.
 */
package data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Andres
 */
public abstract class NIC {

	//<editor-fold defaultstate="collapsed" desc="Information Lists">
		/**
		 * Main data for Pokemon.
		 */
		public static List<String> INFO_POKEMON;
		/**
		 * Main data for Items.
		 */
		public static List<String> INFO_ITEMS;
		/**
		 * Main data for Moves.
		 */
		public static List<String> INFO_MOVES;
		/**
		 * Main data for Types.
		 */
		public static List<String> INFO_TYPES;
		/**
		 * Main data for Maps.
		 */
		public static ArrayList<List<String>> INFO_MAPS;
		/**
		 * Main data for TMs.
		 */
		public static List<String> INFO_TM;
		/**
		 * Blank map data.
		 */
		public static List<String> INFO_BLANK_MAP;
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Static Attributes">
		/**
		 * Amount of Maps in X.
		 */
		public static int NUM_MAPS_X = 4;
		/**
		 * Amount of Maps in Y.
		 */
		public static int NUM_MAPS_Y = 4;
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Static Images">
		public static BufferedImage pokemonIcons;
		public static BufferedImage tileset;
		public static BufferedImage gym;
		public static BufferedImage center;
		public static BufferedImage house;
		public static BufferedImage objects;
		public static BufferedImage shop;
		public static BufferedImage house2;
		public static BufferedImage house3;
		public static BufferedImage wstone;
		public static BufferedImage wstone2;
		public static BufferedImage tree;
		public static BufferedImage tree2;
		public static BufferedImage[] objSets;
	//</editor-fold>
		
	public static void loadAllData() {
		
		try {
			loadImages();
		} catch (IOException ex) {
			
		}
		
		try {
			loadInfo();
		} catch (IOException ex) {
			
		}
		
		loadMaps();
	}
	
	private static void loadImages() throws IOException {
		
		pokemonIcons = ImageIO.read(new File("assets/pokemon/pokemonIconsSmall.png"));
		
		tileset = ImageIO.read(new File("assets/map/tileset.png"));

		shop = ImageIO.read(new File("assets/map/shop.png"));

		gym = ImageIO.read(new File("assets/map/gym.png"));

		center = ImageIO.read(new File("assets/map/center.png"));

		objects = ImageIO.read(new File("assets/map/objects.png"));

		house = ImageIO.read(new File("assets/map/house.png"));

		tree = ImageIO.read(new File("assets/map/tree.png"));

		tree2 = ImageIO.read(new File("assets/map/tree2.png"));

		house2 = ImageIO.read(new File("assets/map/house2.png"));

		house3 = ImageIO.read(new File("assets/map/house3.png"));

		wstone = ImageIO.read(new File("assets/map/wstone.png"));

		wstone2 = ImageIO.read(new File("assets/map/wstone2.png"));

		objSets = new BufferedImage[]{objects, house, house2, house3, center, shop, gym, tree, tree2, wstone, wstone2};
		
	}
	
	private static void loadInfo() throws IOException {

		List<String> readInfoP = null;
		List<String> readInfoI = null;
		List<String> readInfoM = null;
		List<String> readInfoT = null;
		List<String> readInfoTM = null;

		File archivo;

		archivo = new File("db/listPokemon.txt");
		readInfoP = Files.readAllLines(archivo.toPath());

		archivo = new File("db/listItems.txt");
		readInfoI = Files.readAllLines(archivo.toPath());

		archivo = new File("db/listMoves.txt");
		readInfoM = Files.readAllLines(archivo.toPath());

		archivo = new File("db/listTypes.txt");
		readInfoT = Files.readAllLines(archivo.toPath());

		archivo = new File("db/listTM.txt");
		readInfoTM = Files.readAllLines(archivo.toPath());

		INFO_ITEMS = readInfoI;
		INFO_POKEMON = readInfoP;
		INFO_MOVES = readInfoM;
		INFO_TYPES = readInfoT;
		INFO_TM = readInfoTM;
		
	}
	
	private static void loadMaps() {

		File archivo;
		List<String> readInfoB = null;
		ArrayList<List<String>> readMap = new ArrayList();
		
		try {
			archivo = new File("db/mapBLANK.txt");
			readInfoB = Files.readAllLines(archivo.toPath());
		} catch (IOException ex) {
			System.err.println("BLANK: Couldn't load files!");
			System.exit(0);
		}

		for (int y = 0; y < NUM_MAPS_Y; y++) {
			for (int x = 0; x < NUM_MAPS_X; x++) {
				List<String> temp = null;
				try {
					archivo = new File("db/mapX" + x + "Y" + y + ".txt");
					temp = Files.readAllLines(archivo.toPath());
				} catch (IOException ex1) {
					temp = readInfoB;
				}
				readMap.add(temp);
			}
		}
		
		INFO_MAPS = readMap;
		INFO_BLANK_MAP = readInfoB;
		
	}
}

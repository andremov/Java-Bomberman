/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.KeyHandler;
import javax.swing.JFrame;

/**
 *
 * @author Andres
 */
public class Window extends JFrame {

	Display canvas;
	Thread canvasThread;
	
	public Window() {
		setLayout(null);
		int size = bomberman.Bomberman.SCREEN_SIZE;
		setSize(size+6,size+29);
		setLocationRelativeTo(null);
		setTitle("Bomberman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
//		setUndecorated(true);
		addKeyListener(new KeyHandler());
		setIconImage(new javax.swing.ImageIcon("assets/icon.png").getImage());
		
		
		canvas = new Display();
		canvas.setSize(size,size);
		add(canvas);
		
		canvasThread = new Thread(canvas);
		
		setVisible(true);
	}
	
	/**
	 * Start canvas thread.
	 */
	public void startCanvas() {
		canvasThread.start();
	}
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.KeyHandler;
import javax.swing.JFrame;
import model.Handler;

/**
 *
 * @author Andres
 */
public class Window extends JFrame {

	Display canvas;
	Thread canvasThread;
	
	public Window(Handler h) {
		setLayout(null);
		setSize(Handler.SCREEN_SIZE,Handler.SCREEN_SIZE);
		setLocationRelativeTo(null);
		setTitle("Bomberman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		addKeyListener(new KeyHandler());
		
		
		canvas = new Display(h);
		canvas.setSize(Handler.SCREEN_SIZE,Handler.SCREEN_SIZE);
		add(canvas);
		
		canvasThread = new Thread(canvas);
		
		setVisible(true);
	}
	
	public void startCanvas() {
		canvasThread.start();
	}
	
}

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
	
	public Window(Handler h) {
		setLayout(null);
		setSize(h.SCREEN_SIZE,h.SCREEN_SIZE);
		setLocationRelativeTo(null);
		setTitle("Adivinador");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		
		addKeyListener(new KeyHandler(h));
		
		canvas = new Display(h);
//		canvas.setSize(h.getCanvasSize(),h.getCanvasSize());
		add(canvas);
		
		setVisible(true);
	}
	
}

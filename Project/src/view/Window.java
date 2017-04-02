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
	
	public Window() {
		setLayout(null);
		setSize(500,500);
		setLocationRelativeTo(null);
		setTitle("Adivinador");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);
		
		addKeyListener(new KeyHandler());
		
		canvas = new Display();
		canvas.setSize(300,300);
		canvas.setSize(1,1);
		add(canvas);
		
		setVisible(true);
	}
	
}

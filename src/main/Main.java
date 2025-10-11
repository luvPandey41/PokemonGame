package main;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {
	public static void main(String [] args) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setName("Pokemon Game");

		GamePanel gp = new GamePanel();
		window.add(gp);
		window.pack();

		gp.setUpGame();
		gp.startGameThread();

		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}

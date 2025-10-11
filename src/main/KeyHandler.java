package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, rPressed;
	public char keyTyped;
	public GamePanel gp;


	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keyTyped = e.getKeyChar();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(gp.gameState == gp.playState) {
			if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
				upPressed = true;
			}
			if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				downPressed = true;
			}
			if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if(code == KeyEvent.VK_P) {
				gp.gameState = gp.pauseState;
			}
			if(code == KeyEvent.VK_SPACE) {
				spacePressed = true;
			}
			if(code == KeyEvent.VK_E) {
				gp.gameState = gp.menuState;
			}
		}

		else if(gp.gameState == gp.pauseState) {
			if(code == KeyEvent.VK_P) {
				gp.gameState = gp.playState;
			}
		}

		else if (gp.gameState ==  gp.cutsceneState) {
			if(code == KeyEvent.VK_SPACE) {
				spacePressed = true;
			}
		}
		
		else if(gp.gameState == gp.dialogueState) {
			
			if(code == KeyEvent.VK_SPACE) {
				spacePressed = true;
			}
			
		}
		
		else if(gp.gameState == gp.menuState) {
			if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
				downPressed = true;
			}
			if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
				upPressed = true;
			}
			if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if(code == KeyEvent.VK_SPACE ) {
				spacePressed = true;
			}
			if(code == KeyEvent.VK_R) {
				 rPressed = true;
			}
		}
		
		else if(gp.gameState == gp.battleState) {
			if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
				downPressed = true;
			}
			if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
				upPressed = true;
			}
			if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if(code == KeyEvent.VK_R) {
				rPressed = true;
			}
			if(code == KeyEvent.VK_SPACE ) {
				spacePressed = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if(code == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}

	}

}
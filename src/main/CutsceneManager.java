package main;

import java.awt.Graphics2D;

import entity.Entity;

public class CutsceneManager {
	GamePanel gp;
	Graphics2D g2;
	public int sceneNum;
	public int scenePhase = 1;
	
	public final int starterSelection = 1;
	
	public CutsceneManager(GamePanel gp) {
		this.gp = gp;
	}
	
//	public void draw(Graphics2D g2) {
//		this.g2 = g2;
//		
//		switch(sceneNum) {
//		case starterSelection: scene_starterSelection(); break;
//		}
//	}

//	private void scene_starterSelection() {
//		gp.gameState = gp.cutsceneState;
//		//scene phase 1: bring rival next to player
//		if(scenePhase == 1) {
//			
//			if((!(Math.abs(gp.npc[2].worldX - gp.player.worldX) < 64 &&  Math.abs(gp.npc[2].worldY - gp.player.worldY) < 16)) || gp.npc[2].collisionOn) {
//				for(Entity i : gp.npc) {
//					if(i != null && !i.name.equals("Rival")) i.sleep = true;
//					gp.npc[2].moveTo(gp.player.worldX + 32, gp.player.worldY - 16);
//				}
//			}
//			else{
//				gp.npc[2].facePlayer();
//				gp.npc[2].sleep = true;
//				scenePhase = 2;
//			}
//			
//		}
//		
//		if(scenePhase == 2) {
//			gp.ui.drawDialogueScreen(new String [] {"Oak: I was a serious pokemon trainer when I was young.", "But now, in my old age I only have these three left.", "You can have one. Go to the machine and pick one.", "Rival: No Fair! What about Me?", "Oak: Don't be impatient. You can have one too."});
//			if(gp.ui.yapDone) scenePhase ++;
//		}
//		if(scenePhase == 3) {
//			gp.player.moveTo(320, 258);
//			if(gp.player.worldX < 314 && gp.player.worldY < 242) {
//				gp.player.noMovement();
//				gp.player.direction = "up";
//				gp.player.spriteNum = 2;
//				sceneNum = 0;
//				scenePhase =  1;
//				gp.gameState = gp.playState;
//			}
//		}
//		//scene phase 2: the fellas yap
//		//scene phase 3: the player is automatically moved to the starter selection machine
//		//throuhgout entire process, the player cant do anything but continue the text;
//		
//	}
	
}
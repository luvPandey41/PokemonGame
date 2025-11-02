package entity;
//gigigitygigitogooo
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;

import javax.imageio.ImageIO;

import entity.Entity;
import item.Item;
import main.GamePanel;
import pokemon.Pokemon;

public class NPC extends Entity {

    public ArrayList<String> lines;
    public ArrayList<String> preBattleDialogue;
    
    public boolean isTrainer;
    public boolean defeated = false;
    public boolean movingBattler = true;
    //public int moneyReward; --> implement later.
    public Pokemon[] heldPokemon = new Pokemon[6];
    
    private int moveDelay = 5; // Number of frames between steps
    private int moveTimer = 0;  // Frame counter
    
    public boolean longRangeInteractor = false;
    
    private long lastRunTime = 0;
    private double lastWorldX, lastWorldY;
    private double lastChangeX, lastChangeY;
    private final double ACTION_LOCK_DISTANCE = 15;
    
    public int ID;

    
    public NPC(GamePanel gp) {
        super(gp);
        lines = new ArrayList<>();
        this.speed = 0; // default NPCs don't move unless scripted
    }

    public void setDialogue(ArrayList<String> lines) {
        lines.clear();
        lines.addAll(lines);
    }
	
    public void update() {
		 
    	if (isTrainer && !defeated && isInEventLine(32, 2)) {
    		if(direction.equals("up") || direction.equals("down")) {
	    		if(Math.abs(worldX - gp.player.worldX) > 2 || Math.abs(worldY - gp.player.worldY) > 8)  {
	    			gp.player.noMovement();
	    			gp.gameState = gp.cutsceneState;
	    			gp.ui.isDone = false; //processing these two lines every time is kind of dumb, but it works.
	    			moveTo((int) gp.player.worldX, (int) gp.player.worldY);
	    		}
	    		else {
	    			speed = 0;
	    			gp.ui.displayText(preBattleDialogue);
	    			if(gp.ui.isDone) {
	    				gp.bHandler.startNewBattle(this);
	    			}

	    		}
    		
    		}
    		else {
    			if(Math.abs(worldY - gp.player.worldY) > 2 || Math.abs(worldX - gp.player.worldX) > 8) {
    				gp.player.noMovement();
	    			gp.gameState = gp.cutsceneState;
	    			gp.ui.isDone = false; 
	    			moveTo((int) gp.player.worldX, (int) gp.player.worldY);
	    		}
    			else {
	    			speed = 0;
	    			gp.ui.displayText(preBattleDialogue);
	    			gp.bHandler.startNewBattle(this);
	    		}
    		}
    	}
    	
    	//this is the area to denote the extended range npcs 
    	if(longRangeInteractor && gp.keyH.spacePressed && isInEventRadius(24)) {
    		interact(); 
    		gp.keyH.spacePressed = false;
    	}
    	
    	else if(gp.keyH.spacePressed && isInEventRadius(8)) {
    			interact(); 
    			gp.keyH.spacePressed = false;
    	}
    	
		//setAction();
		
		collisionOn = false;
		
		if(isTrainer && !defeated) {
			gp.cChecker.checkTile(this);
			gp.cChecker.checkEntity(this, gp.currentMap.npcList);
		}
		else {
			gp.cChecker.checkTile(this);
			gp.cChecker.checkPlayer(this);
			gp.cChecker.checkEntity(this, gp.currentMap.npcList);
		}
		//fix this push off to later strategem in the future.
		
		
		if(!collisionOn) {
			switch(direction) {
			case "up":
				worldY -= speed;
				break;
			case "down":
				worldY += speed;
				break;
			case "right":
				worldX += speed;
				break;
			case "left":
				worldX -= speed;
				break;
			}
		}

		spriteCounter ++;
		if(spriteCounter > 12 && speed != 0) {
			if(spriteNum == 1) {
				spriteNum = 2;
			}
			else if(spriteNum == 2) {
				spriteNum = 3;
			}
			else if(spriteNum ==3) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
	}
    
	 public void moveTo(int x, int y) {
	    	
	    	speed = .2;
	    	
	    	int dx = (int) (worldX - x); //+ if player is right of target, needs to move left
	    	int dy = (int) (worldY - y); //+ if player is above target, needs to move down.
	    	
	    	if(Math.abs(dx) > Math.abs(dy)) {
	    		if(dx > 0) {
	    			direction = "left";
	    		}
	    		else {
	    			direction = "right";
	    		}
	    	}
	    	else {
	    		if(dy > 0) {
	    			direction = "up";
	    		}
	    		else {
	    			direction = "down";
	    		}
	    	}
	    }
    
 
    public void speak() {
    	facePlayer();
    	gp.ui.displayText(lines);
    }
    
    public void speak(ArrayList<String> x) {
    	facePlayer();
    	gp.ui.displayText(x);
    }

    public boolean isWalkable(int x, int y) {
    	return !(new Color(gp.currentMap.collisionMapImage.getRGB(x,y), true).equals(Color.black));
    }
    
    public void facePlayer() {
    	
    	/*switch(gp.player.direction) {
    	case "up": direction = "down"; break;
    	case "down": direction = "up"; break;
    	case "left": direction = "right"; break;
    	case "right": direction = "left"; break;
    	}*/
    	
    	
		int dx = (int) (worldX - gp.player.worldX); // + if right of player
		int dy = (int) (worldY - gp.player.worldY); // + if higher than player.

		if(dy > dx) {
			if(dy > 0) {
				direction = "up";
			} else {
				direction = "down";
			}
		}
		else {
			if(dx > 0) {
				direction = "left";
			} else {
				direction = "right";
			}
		} 
	}
    
    public void loadSprites(String spriteBase) {
        down1 = loadSprite(spriteBase, "npc", "down_1", "down");
        down2 = loadSprite(spriteBase, "npc", "down_2", "down");
        down3 = loadSprite(spriteBase, "npc", "down_3", "down");

        up1 = loadSprite(spriteBase, "npc", "up_1", "up");
        up2 = loadSprite(spriteBase, "npc", "up_2", "up");
        up3 = loadSprite(spriteBase, "npc", "up_3", "up");

        left1 = loadSprite(spriteBase, "npc", "left_1", "left");
        left2 = loadSprite(spriteBase, "npc", "left_2", "left");
        left3 = loadSprite(spriteBase, "npc", "left_3", "left");

        right1 = loadSprite(spriteBase, "npc", "right_1", "right");
        right2 = loadSprite(spriteBase, "npc", "right_2", "right");
        right3 = loadSprite(spriteBase, "npc", "right_3", "right");

        battleSprite = loadSprite(spriteBase, "battleSprites", "battleSprite", null);
        solidAreas = new Rectangle[4];
        solidAreas[0] = Entity.generateSolidArea(up1);
        solidAreas[1] = Entity.generateSolidArea(left1);
        solidAreas[2] = Entity.generateSolidArea(down1);
        solidAreas[3] = Entity.generateSolidArea(right1);
    }

    private BufferedImage loadSprite(String base, String packageName, String primaryName, String fallbackName) {
        String basePath = "/" + packageName + "/";
        BufferedImage img = tryLoad(basePath + base + "_" + primaryName + ".png");
        
        if (img == null && fallbackName != null) {
            // Fallback to single frame (e.g., up.png)
            img = tryLoad(basePath + base + "_" + fallbackName + ".png");
        }
        
        return img;
    }

    private BufferedImage tryLoad(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) return ImageIO.read(is);
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println(path + " did not load.");
            // Optional: log missing sprite if needed
        }
        return null;
    }

    public boolean isInEventLine(int distance, int tolerance) {
    	
    	//distance = how far away the event triggers from infront of NPC
    	//tolerance = how far away on the minor axis the event can trigger   	
    	//In general, for battle the setup up is (24/2), and for talking it is (2/2).
    	
        int dx = (int) (worldX - gp.player.worldX); // if +, player is right of entity
        int dy = (int) (worldY - gp.player.worldY); // if +, player is above entity
  
        switch (direction) {
            case "up":
                return (Math.abs(dx) < tolerance) && (dy > 0) && (Math.abs(dy) <= distance);
            case "down":
                return (Math.abs(dx) < tolerance) && (dy < 0) && (Math.abs(dy) <= distance);
            case "left":
                return (Math.abs(dy) < tolerance) && (dx > 0) && (Math.abs(dx) <= distance);
            case "right":
                return (Math.abs(dy) < tolerance) && (dx < 0) && (Math.abs(dx) <= distance);
        }
        return false;
        
    }
    
    public boolean isInEventRadius(int distance) {
    	int dx = (int) (worldX - gp.player.worldX); 
        int dy = (int) (worldY - gp.player.worldY); 
        
        
        return Math.abs(dx) < distance && Math.abs(dy) < distance;
        /*
        switch(gp.player.direction) {
	        case "up":  {
	        	if(dy < 0 && dy >= -distance && Math.abs(dx) <= 5) {
	        		return true;
	        	}
        		break;
        	}
	        case "down": {
	        	if(dy > 0 && dy <= distance + 4 && Math.abs(dx) <= 5) {
	        		return true;
	        	}
        		break;
	        }
        	case "left": {
        		if(dx < 0 && dx > -distance && Math.abs(dy) <= 5) {
	        		return true;
	        	}
        		break;
        	}
	        case "right": {
	        	if(dx > 0 && dx <= distance + 1 && Math.abs(dy) <= 5) {
	        		return true;
	        	}
        		break;
	        }
        }
        
        return false; */
    }

    public void setAction() {
        // optionally override with wandering or AI
    }
    
    public void interact() {
    	switch(ID) {
	    	case (9): {	
	        	for(Pokemon i: gp.player.heldPokemon) {
	        		if (i!= null) i.hp = i.hlt;
	        	}
	        	speak();
	        	break;
	    	}
	    	case (6): {
	    		Item.addItem("potion", 1, gp);
	    		speak();
	    		break;
	    	}
	    	default: {
	    		speak();
	    		break;
	    	}
    	}
    }
}
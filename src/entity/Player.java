package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import item.Item;
import main.GamePanel;
import main.KeyHandler;
import pokemon.Pokemon;

public class Player extends Entity{
	public GamePanel gp;
	KeyHandler keyH;
	public final int screenX;
	public final int screenY;
	public ArrayList<Item> inventory = new ArrayList<>();
	public ArrayList<Item> machines = new ArrayList<>();
	public ArrayList<Item> keyItems = new ArrayList<>();
	public ArrayList<Item> medicine = new ArrayList<>();
	public ArrayList<Item> berries = new ArrayList<>();
	public final int maxInventorySize = 10;
	public boolean onBike = false;
	public Pokemon[] heldPokemon = new Pokemon[6];
	public ArrayList<Pokemon> pcPokemon = new ArrayList<>();
	public int badgesEarned = 0, pokedexNum = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		this.gp = gp;
		this.keyH = keyH;
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		setDefaultValues();
		getPlayerImage();
		
		 try {   
			 battleSprite = ImageIO.read(getClass().getResourceAsStream("/battleSprites/player_battleSprite.png"));  
		 } catch (IOException e) {    
			 e.printStackTrace();   
		 }
	}

	public void setDefaultValues() {
		worldX = 30;
		worldY = 30;
		direction = "down";
	}
	
	public void noMovement() {
		gp.keyH.upPressed = false;
		gp.keyH.downPressed = false;
		gp.keyH.leftPressed = false;
		gp.keyH.rightPressed = false;	
	}
	public int  itemIndex(String name) {
		name = name.toLowerCase().replace(" ", "-");
		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).name.equals(name)) return i;
		}
		return -1;
	}
	public void giveItem(String name, int quantity) {
		Item.addItem(name, quantity, gp);
	}
	public void selectItem(Item item) {
		switch(item.name) {
			case "Acro Bike":
				if(onBike) {
					gp.ui.displayText("Stopped using the bike.");
				}
				else {
					gp.ui.displayText("Started using the bike.");
				}
				onBike = !onBike;
				break;
		}
	}
	public void getPlayerImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_up_2.png"));
			up3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_up_3.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_down_2.png"));
			down3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_down_3.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_left_2.png"));
			left3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_left_3.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_right_2.png"));
			right3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_walk_right_3.png"));
			bup1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_up_1.png"));
			bup2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_up_2.png"));
			bup3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_up_3.png"));
			bdown1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_down_1.png"));
			bdown2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_down_2.png"));
			bdown3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_down_3.png"));
			bleft1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_left_1.png"));
			bleft2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_left_2.png"));
			bleft3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_left_3.png"));
			bright1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_right_1.png"));
			bright2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_right_2.png"));
			bright3 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_bike_right_3.png"));
			sup1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_up_1.png"));
			sup2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_up_2.png"));
			sdown1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_down_1.png"));
			sdown2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_down_2.png"));
			sleft1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_left_1.png"));
			sleft2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_left_2.png"));
			sright1 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_right_1.png"));
			sright2 = ImageIO.read(getClass().getResourceAsStream("/player/trainer_surf_right_2.png"));
			solidAreas = new Rectangle[12];
			solidAreas[0] = Entity.generateSolidArea(up1);
		    solidAreas[1] = Entity.generateSolidArea(left1);
		    solidAreas[2] = Entity.generateSolidArea(down1);
		    solidAreas[3] = Entity.generateSolidArea(right1);
		    solidAreas[4] = Entity.generateSolidArea(bup1);
	        solidAreas[5] = Entity.generateSolidArea(bleft1);
	        solidAreas[6] = Entity.generateSolidArea(bdown1);
	        solidAreas[7] = Entity.generateSolidArea(bright1);
	        solidAreas[8] = Entity.generateSolidArea(sup1);
	        solidAreas[9] = Entity.generateSolidArea(sleft1);
	        solidAreas[10] = Entity.generateSolidArea(sdown1);
	        solidAreas[11] = Entity.generateSolidArea(sright1);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		
		if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
			if(keyH.upPressed) {
				direction = "up";
			}
			if(keyH.downPressed) {
				direction = "down";
			}
			if(keyH.leftPressed) {
				direction = "left";
			}
			if(keyH.rightPressed) {
				direction = "right";
			}
			if(keyH.spacePressed) {
				if(!surfing) attemptToSurf();
				else attemptToLand();
			}
			if(onBike) speed = 1.5;
			else if(gp.gameState == gp.cutsceneState) speed = .5;
			else speed = .75;
			//Check tile collision
			gp.cChecker.resolveStuckState(this);
			collisionOn = false; 
			gp.cChecker.checkTile(this);
			//Check npc collision
			gp.cChecker.checkEntity(this, gp.currentMap.npcList);
			
			gp.cChecker.checkSpecialBlocks(this);

			gp.eHandler.checkEvent();
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
			if(spriteCounter > 12) {
				if(!surfing) {
					if(spriteNum == 1) {
						spriteNum = 2;
					}
					else if(spriteNum == 2) {
						spriteNum = 3;
					}
					else if(spriteNum ==3) {
						spriteNum = 1;
					}
				}
				else {
					if(spriteNum == 1) spriteNum = 2;
					else spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
	}

//	public void interactNPC(int i) {
//		if(i != 999) {
//			if(gp.keyH.spacePressed) {
//				gp.npc[i].speak();
//			}
//		}
//		//gp.keyH.spacePressed = false;
//	}
	
	@Override
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		if(!onBike && !surfing) {
		switch(direction) {
		case "up":
			solidArea = solidAreas[0];
			if(spriteNum == 1) {
				image = up1;
			}
			if(spriteNum == 2) {
				image = up2;
			}
			if(spriteNum == 3) {
				image = up3;
			}
			break;
		case "down":
			solidArea = solidAreas[2];
			if(spriteNum == 1) {
				image = down1;
			}
			if(spriteNum == 2) {
				image = down2;
			}
			if(spriteNum == 3) {
				image = down3;
			}
			break;
		case "left":
			solidArea = solidAreas[1];
			if(spriteNum == 1) {
				image = left1;
			}
			if(spriteNum == 2) {
				image = left2;
			}
			if(spriteNum == 3) {
				image = left3;
			}
			break;
		case "right":
			solidArea = solidAreas[3];
			if(spriteNum == 1) {
				image = right1;
			}
			if(spriteNum == 2) {
				image = right2;
			}
			if(spriteNum == 3) {
				image = right3;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		solidAreaXScale = 1.0*gp.tileSize/image.getWidth();
		solidAreaYScale = 1.0*gp.tileSize/image.getHeight();
		}
		else if (onBike){
		switch(direction) {
		case "up":
			solidArea = solidAreas[4];
			if(spriteNum == 1) {
				image = bup1;
			}
			if(spriteNum == 2) {
				image = bup2;
			}
			if(spriteNum == 3) {
				image = bup3;
			}
			break;
		case "down":
			solidArea = solidAreas[6];
			if(spriteNum == 1) {
				image = bdown1;
			}
			if(spriteNum == 2) {
				image = bdown2;
			}
			if(spriteNum == 3) {
				image = bdown3;
			}
			break;
		case "left":
			solidArea = solidAreas[5];
			if(spriteNum == 1) {
				image = bleft1;
			}
			if(spriteNum == 2) {
				image = bleft2;
			}
			if(spriteNum == 3) {
				image = bleft3;
			}
			break;
		case "right":
			solidArea = solidAreas[7];
			if(spriteNum == 1) {
				image = bright1;
			}
			if(spriteNum == 2) {
				image = bright2;
			}
			if(spriteNum == 3) {
				image = bright3;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize*2, gp.tileSize, null);
		}
		else if(surfing) {
			switch(direction) {
			case "up":
				solidArea = solidAreas[8];
				if(spriteNum == 1) {
					image = sup1;
				}
				if(spriteNum == 2) {
					image = sup2;
				}
				break;
			case "down":
				solidArea = solidAreas[10];
				if(spriteNum == 1) {
					image = sdown1;
				}
				if(spriteNum == 2) {
					image = sdown2;
				}
				break;
			case "left":
				solidArea = solidAreas[9];
				if(spriteNum == 1) {
					image = sleft1;
				}
				if(spriteNum == 2) {
					image = sleft2;
				}
				break;
			case "right":
				solidArea = solidAreas[11];
				if(spriteNum == 1) {
					image = sright1;
				}
				if(spriteNum == 2) {
					image = sright2;
				}
				break;
			}
			g2.drawImage(image, screenX, screenY, gp.tileSize*7/4, gp.tileSize*7/4, null);
			solidAreaXScale = gp.tileSize*1.75/image.getWidth();
			solidAreaYScale = gp.tileSize*1.75/image.getHeight();
		}
		g2.setColor(Color.white);
		g2.drawString(worldX + " " + worldY + " " + direction + " " + gp.keyH.leftPressed + " " + gp.keyH.rightPressed + " " + gp.keyH.upPressed + " " + gp.keyH.downPressed, screenX, screenY);
	}
	
	public boolean attemptToSurf() {
		int pushDistance = 0; 
		int maxPush = gp.tileSize;
		int dirX = 0, dirY = 0;
		switch(direction) {
			case "up": dirY = -1; break;
			case "down": dirY = 1; break;
			case "left": dirX = -1; break;
			case "right": dirX = 1; break;
		}
		while(pushDistance <= maxPush) {
			if(surfable(worldX + dirX*pushDistance, worldY + dirY*pushDistance)) {
				worldX += pushDistance * dirX;
				worldY += pushDistance * dirY;
				surfing = true;
				keyH.spacePressed = false;
				System.out.println("surf: " + worldY);
				return true;
			}
			pushDistance++;
		}
		System.out.println("Failed");
		return false;
	}
	
	public boolean surfable(double worldX, double worldY) {
		Color blue = new Color(0, 0, 255);
		double futureLeftX = (worldX*2) + (solidArea.x - solidArea.width/2)/2.0 - 4;
		double futureTopY = (worldY*1.5) + (solidArea.y - solidArea.height/2)/1.5 - 2;
		for(int k = (int) (futureLeftX*gp.currentMap.scale); k < (int) (futureLeftX*gp.currentMap.scale + solidArea.width*2*100/gp.screenWidth*gp.currentMap.scale*5); k++) {
			for(int j =  (int) (futureTopY*gp.currentMap.scale); j <  (int) (futureTopY*gp.currentMap.scale) + solidArea.height*150/gp.screenHeight*gp.currentMap.scale*2; j++) {
				if(k<0||j<0||k>=gp.currentMap.collisionMapImage.getWidth()|| j>=gp.currentMap.collisionMapImage.getHeight()) {
					return false;
				}
				if(!new Color(gp.currentMap.collisionMapImage.getRGB(k,j), true).equals(blue))  return false;	
			}
		}
		return true;
	}
	public boolean attemptToLand() {
		int pushDistance = 0; 
		int maxPush = gp.tileSize;
		int dirX = 0, dirY = 0;
		switch(direction) {
			case "up": dirY = -1; break;
			case "down": dirY = 1; break;
			case "left": dirX = -1; break;
			case "right": dirX = 1; break;
		}
		while(pushDistance <= maxPush) {
			if(walkable(worldX + dirX*pushDistance, worldY + dirY*pushDistance)) {
				worldX += pushDistance * dirX;
				worldY += pushDistance * dirY;
				surfing = false;
				keyH.spacePressed = false;
				System.out.println("walk: " + worldY);
				return true;
			}
			pushDistance++;
		}
		System.out.println("Failed");
		return false;
	}
	public boolean walkable(double worldX, double worldY) {
		Color white = new Color(255, 255, 255);
		double futureLeftX = (worldX*2) + (solidArea.x - solidArea.width/2)/2.0 - 4;
		double futureTopY = (worldY*1.5) + (solidArea.y - solidArea.height/2)/1.5 - 2;
		for(int k = (int) (futureLeftX*gp.currentMap.scale); k < (int) (futureLeftX*gp.currentMap.scale + solidArea.width*2*100/gp.screenWidth*gp.currentMap.scale*5); k++) {
			for(int j =  (int) (futureTopY*gp.currentMap.scale); j <  (int) (futureTopY*gp.currentMap.scale) + solidArea.height*150/gp.screenHeight*gp.currentMap.scale*2; j++) {
				if(k<0||j<0||k>=gp.currentMap.collisionMapImage.getWidth()|| j>=gp.currentMap.collisionMapImage.getHeight()) {
					return false;
				}
				if(!new Color(gp.currentMap.collisionMapImage.getRGB(k,j), true).equals(white))  return false;	
			}
		}
		return true;
	}
}
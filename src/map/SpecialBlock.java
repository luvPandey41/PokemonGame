package map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import javax.imageio.ImageIO;

import entity.Entity;
import item.Item;
import main.GamePanel;
import pokemon.Pokemon;

public class SpecialBlock {
	public String name;
	public BufferedImage[] images;
	public double worldX, worldY;
	public Pokemon mon;
	public GamePanel gp;
	public String animationCause;
	public String map;
	public String interactText;
	public boolean collision;
	public static BufferedImage[] grassImages = new BufferedImage[2];
	public static BufferedImage[] flowers = new BufferedImage[5];
	public static BufferedImage[] complexGrassImages = new BufferedImage[2];
	//fix later to add cut animation
	public static BufferedImage[] cut = new BufferedImage[1];
	public static BufferedImage post, sign, item_ball, tall_grass_1, tall_grass_2, tall_grass_3, tall_grass_4, tall_grass_5, tall_grass_6, 
	tall_grass_7, tall_grass_8, tall_grass_9, red_mailbox, orange_mailbox, yellow_mailbox, green_mailbox, blue_mailbox, pink_mailbox, purple_mailbox, indigo_mailbox, black_mailbox, selection_ball;
	public double spawnPercentage;
	public boolean wasStoodOn = false;
	public boolean wasInteractedWith, interactedWith, disappear;
	public String interactItem;
	public int itemQuantity;
	public boolean overlay;
	public int width, height;
	
	static {
		try {
			grassImages[0] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_up.png"));
			grassImages[1] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_down.png"));
			flowers[0] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/flower_1.png"));
			flowers[1] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/flower_2.png"));
			flowers[2] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/flower_3.png"));
			flowers[3] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/flower_4.png"));
			flowers[4] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/flower_5.png"));
			cut[0] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/cut.png"));
			post = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/post.png"));
			sign = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/sign.png"));
			item_ball =  ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/item_ball.png"));
			tall_grass_1 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_1.png"));
			tall_grass_2 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_2.png"));
			tall_grass_3 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_3.png"));
			tall_grass_4 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_4.png"));
			tall_grass_5 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_5.png"));
			tall_grass_6 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_6.png"));
			tall_grass_7 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_7.png"));
			tall_grass_8 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_8.png"));
			tall_grass_9 = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/grass_9.png"));
			red_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/red_mailbox.png"));
			orange_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/orange_mailbox.png"));
			yellow_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/yellow_mailbox.png"));
			green_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/green_mailbox.png"));
			blue_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/blue_mailbox.png"));
			pink_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/pink_mailbox.png"));
			purple_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/purple_mailbox.png"));
			indigo_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/indigo_mailbox.png"));
			black_mailbox = ImageIO.read(SpecialBlock.class.getResourceAsStream("/specialBlocks/black_mailbox.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public SpecialBlock() {
		
	}
	
	public void draw(Graphics2D g2) {
		int screenX = (int) ((worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX);
		int screenY = (int) ((worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY);
		if(screenX + width < 0 || screenX > gp.screenWidth || screenY + height < 0 || screenY > gp.screenHeight) return;
		boolean currentlyStoodOn = stoodOn(worldX, worldY);
		if(animationCause.equals("time")) {
			g2.drawImage(images[(gp.animationInt/50)%images.length], screenX, screenY, width, height, null);
		}
		else if(animationCause.equals("stood-on")) {
			if(currentlyStoodOn) g2.drawImage(images[1], screenX, screenY, width, height, null);
			else g2.drawImage(images[0], screenX, screenY, width, height, null);
		}
		else if(animationCause.equals("interact")) {
			if(!disappear) {
				g2.drawImage(images[0], screenX, screenY, width, height, null);
			}
			if(gp.keyH.spacePressed && interactable()) {
				collision = false;
				disappear = true;
			}
		}
		else if(interactText!=null){
			g2.drawImage(images[0], screenX, screenY, width, height, null);
			if(gp.keyH.spacePressed && interactable()) {
				gp.ui.displayText(interactText);
				interactedWith = true;
				gp.keyH.spacePressed = false;
			}
		}
		else if(interactItem!=null) {
			if(!disappear) {
				g2.drawImage(images[0], screenX, screenY, width, height, null);
				if(gp.keyH.spacePressed && interactable()) {
					Item.addItem(interactItem, itemQuantity, gp);
//					if(itemQuantity == 1) gp.ui.displayBasicText(gp.player.name + " found 1 " + interactItem);
//					else gp.ui.displayBasicText(gp.player.name + " found " + itemQuantity + " " + interactItem + "s");
					disappear = true;
					collision = false;
				}
			}
		}
		else {
			g2.drawImage(images[0], screenX, screenY, width, height, null);
		}
		if(spawnPercentage>0) {
			if(currentlyStoodOn && !wasStoodOn) {
				int random = (int) (Math.random() * (100.0 / spawnPercentage));
				if(random==0) {
					gp.bHandler.startWildBattle(gp.currentMap.spawnsheet.grassEncounter());
				}
			}
		}
		wasStoodOn = currentlyStoodOn;
		
	}
	public void drawOverlay(Graphics2D g2) {
		if(!overlay) return;
		int screenX = (int) ((worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX);
		int screenY = (int) ((worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY);
		if(screenX + width < 0 || screenX > gp.screenWidth || screenY + height < 0 || screenY > gp.screenHeight) return;
		if(screenY < gp.player.screenY + 16) return;
		//draw a portion of the image relative to worldY of the player
		g2.drawImage(images[0], screenX, screenY + height/2, screenX + gp.tileSize, screenY + height, 0, 16, 32, 32, null);
	}
	public boolean interactable(){
		if(interactedWith) {
			interactedWith = false;
			wasInteractedWith = true;
			return false;
		}
    	int dx = (int) (worldX - gp.player.worldX); 
        int dy = (int) (worldY - gp.player.worldY); 
        int distance = 8;
        switch(gp.player.direction) {
	        case "up":  {
	        	if(dy < 0 && dy >= -distance -4&& Math.abs(dx) <= 5) {
	        		return true;
	        	}
        		break;
        	}
	        case "down": {
	        	if(dy > 0 && dy <= distance + 4 && Math.abs(dx) <= 5) {
	        		return true;
	        	}
	        	if(name.equals("Bottom Mailbox") && dy <= distance + 8 && Math.abs(dx) <= 5) return true;
        		break;
	        }
        	case "left": {
        		if(dx < 0 && dx > -distance -4 && Math.abs(dy) <= 5) {
	        		return true;
	        	}
        		break;
        	}
	        case "right": {
	        	if(dx > 0 && dx <= distance + 4 && Math.abs(dy) <= 5) {
	        		return true;
	        	}
        		break;
	        }
        }
        wasInteractedWith = interactedWith;
        return false;
		
	}
	public boolean stoodOn(double worldX, double worldY) {
		Rectangle blockRect = new Rectangle((int) ((worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX), (int) ((worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY), gp.tileSize, gp.tileSize);
		if(blockRect.intersects(gp.player.getSolidArea())) return true;
		for(Entity entity:gp.currentMap.npcList) if(entity!=null) if(blockRect.intersects(entity.getSolidArea())) return true;
		return false;
	}
	public static void addGrass(double worldXStart, double worldYStart, double width, double height, String mapName, GamePanel gp) {
		for(double o = worldXStart; o <= worldXStart + width; o+=gp.tileSize*100/gp.screenWidth) {
			for(double i = worldYStart; i <= worldYStart + height; i+=gp.tileSize*100/gp.screenHeight) {
				SpecialBlock block = new SpecialBlock();
				block.name = "Grass";
				block.worldX = o;
				block.worldY = i;
				block.animationCause = "stood-on";
				block.collision = false;
				block.map = mapName;
				block.gp = gp;
				block.height = gp.tileSize;
				block.width = gp.tileSize;
				block.images = grassImages;
				block.spawnPercentage = 5;
				gp.specialBlocks.add(block);
			}
		}
	}
	public static void addSelectionBall(double worldX, double worldY, String mapName, String pMon, int level, GamePanel gp) {
		SpecialBlock block = new SpecialBlock();
		block.name = "selection_ball";
		block.worldX = worldX;
		block.worldY = worldY;
		block.animationCause = "none";
		block.collision = true;
		block.map = mapName;
		block.images = new BufferedImage [] {selection_ball};
		block.gp = gp;
		block.height = 48;
		block.width = 48;
		block.mon = new Pokemon(gp, pMon, level);
		gp.specialBlocks.add(block);
	}

	public static void addMailbox(double worldX, double worldY, int type, String mapName, String interactText, GamePanel gp) {
		BufferedImage[] imagesArr = new BufferedImage[1];
		switch(type) {
			case 0:
				imagesArr[0] = red_mailbox;
				break;
			case 1:
				imagesArr[0] = orange_mailbox;
				break;
			case 2:
				imagesArr[0] = yellow_mailbox;
				break;
			case 3:
				imagesArr[0] = green_mailbox;
				break;
			case 4:
				imagesArr[0] = blue_mailbox;
				break;
			case 5:
				imagesArr[0] = pink_mailbox;
				break;
			case 6:
				imagesArr[0] = purple_mailbox;
				break;
			case 7:
				imagesArr[0] = indigo_mailbox;
				break;
			case 8:
				imagesArr[0] = black_mailbox;
				break;	
		}
		SpecialBlock block = new SpecialBlock();
		block.name = "Mailbox";
		block.worldX = worldX;
		block.worldY = worldY;
		block.animationCause = "none";
		block.collision = true;
		block.map = mapName;
		block.images = imagesArr;
		block.gp = gp;
		block.height = gp.tileSize * 3 / 2;
		block.width = gp.tileSize * 3 / 4;
		block.interactText = interactText;	
		gp.specialBlocks.add(block);
	}
//	public static void addTallGrass(double worldXStart, double worldYStart, double width, double height, String mapName, GamePanel gp) {
//		for(double o = worldXStart; o <= worldXStart + width; o+=gp.tileSize*100/gp.screenWidth) {
//			for(double i = worldYStart; i <= worldYStart + height; i+=gp.tileSize*100/gp.screenHeight) {
//				if(o == worldXStart && i == worldYStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_1, gp));
//				else if(o == worldXStart && i > worldYStart + height - gp.tileSize*100/gp.screenHeight) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_7, gp));
//				else if(o == worldXStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_4, gp));
//				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth && i == worldYStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_3, gp));
//				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth && i > worldYStart + height - gp.tileSize*100/gp.screenHeight) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_9, gp));
//				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_6, gp));
//				else if(i == worldYStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_2, gp));
//				else if(i > worldYStart + height - gp.tileSize*100/gp.screenHeight) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_8, gp));
//				else gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_5, gp));
//			}
//		}
//	}
//	public static void addFlower(double worldX, double worldY, String mapName, GamePanel gp) {
//		gp.specialBlocks.add(new SpecialBlock("Flower", worldX, worldY, false, "time", 0, mapName, flowers, gp));
//	}
//	public static void addPost(double worldX, double worldY, String mapName, String interactText, GamePanel gp) {
//		gp.specialBlocks.add(new SpecialBlock("Post", worldX, worldY, true, interactText, mapName, post, gp));
//	}
//	public static void addSign(double worldX, double worldY, String mapName, String interactText, GamePanel gp) {
//		gp.specialBlocks.add(new SpecialBlock("Sign", worldX, worldY, true, interactText, mapName, sign, gp));
//	}
//	public static void addMailbox(double worldX, double bottomWorldY, String mapName, String interactText, GamePanel gp) {
//		gp.specialBlocks.add(new SpecialBlock("Bottom Mailbox", worldX, bottomWorldY, true, interactText, mapName, mailbox_bottom, gp));
//		gp.specialBlocks.add(new SpecialBlock("Top Mailbox", worldX, bottomWorldY - gp.tileSize*100/gp.screenHeight, true, mapName, mailbox_top, gp));
//	}
//	
//	public static void addItemBall(double worldX, double worldY, String mapName, String itemName, int itemQuantity, GamePanel gp) {
//		gp.specialBlocks.add(new SpecialBlock("Item Ball", worldX, worldY, true, mapName, itemName, itemQuantity, item_ball, gp));
//	}
//	public static void addCut(double worldX, double worldY, String mapName, GamePanel gp) {
//		gp.specialBlocks.add(new SpecialBlock("Cut", worldX, worldY, true, "interact", mapName, cut, gp));
//	}

	public static void addTallGrass(double worldXStart, double worldYStart, double width, double height, String mapName, GamePanel gp) {
		for(double o = worldXStart; o <= worldXStart + width; o+=gp.tileSize*100/gp.screenWidth) {
			for(double i = worldYStart; i <= worldYStart + height; i+=gp.tileSize*100/gp.screenHeight) {
				SpecialBlock block = new SpecialBlock();
				block.name = "Tall Grass";
				block.worldX = o;
				block.worldY = i;
				block.animationCause = "none";
				block.collision = false;
				block.map = mapName;
				block.images = new BufferedImage[1];
				if(o == worldXStart && i == worldYStart) block.images[0] = tall_grass_1;
				else if(o == worldXStart && i > worldYStart + height - gp.tileSize*100/gp.screenHeight) block.images[0] = tall_grass_7;
				else if(o == worldXStart) block.images[0] = tall_grass_4;
				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth && i == worldYStart) block.images[0] = tall_grass_3;
				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth && i > worldYStart + height - gp.tileSize*100/gp.screenHeight) block.images[0] = tall_grass_9;
				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth) block.images[0] = tall_grass_6;
				else if(i == worldYStart) block.images[0] = tall_grass_2;
				else if(i > worldYStart + height - gp.tileSize*100/gp.screenHeight) block.images[0] = tall_grass_8;
				else block.images[0] = tall_grass_5;
				block.gp = gp;
				block.height = gp.tileSize;
				block.width = gp.tileSize;
				block.overlay = true;
				gp.specialBlocks.add(block);
			}
		}
		//for(double o = worldXStart; o <= worldXStart + width; o+=gp.tileSize*100/gp.screenWidth) {
//			for(double i = worldYStart; i <= worldYStart + height; i+=gp.tileSize*100/gp.screenHeight) {
//				if(o == worldXStart && i == worldYStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_1, gp));
//				else if(o == worldXStart && i > worldYStart + height - gp.tileSize*100/gp.screenHeight) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_7, gp));
//				else if(o == worldXStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_4, gp));
//				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth && i == worldYStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_3, gp));
//				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth && i > worldYStart + height - gp.tileSize*100/gp.screenHeight) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_9, gp));
//				else if(o > worldXStart + width - gp.tileSize*100/gp.screenWidth) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_6, gp));
//				else if(i == worldYStart) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_2, gp));
//				else if(i > worldYStart + height - gp.tileSize*100/gp.screenHeight) gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_8, gp));
//				else gp.specialBlocks.add(new SpecialBlock("Grass", o, i, false, true, mapName, tall_grass_5, gp));
//			}
//		}
		
	}
}
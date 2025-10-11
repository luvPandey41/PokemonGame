package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;
import pokemon.Pokemon;

public class Entity {

	GamePanel gp;

	public double worldX, worldY;
	public double speed;

	public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
	public BufferedImage bup1, bup2, bup3, bdown1, bdown2, bdown3, bleft1, bleft2, bleft3, bright1, bright2, bright3;
	public BufferedImage sup1, sup2, sdown1, sdown2, sleft1, sleft2, sright1, sright2;
	public BufferedImage battleSprite;
	public Rectangle[] solidAreas;
	public double solidAreaXScale;
	public double solidAreaYScale;
	
	public String direction;

	public int spriteCounter = 0;
	public int spriteNum = 2;

	public Rectangle solidArea = new Rectangle();
	public boolean collisionOn = false;

	public int actionLockCounter = 0;

	public String dialogues[] = new String[20];
	public int dialogueIndex = 0;
	public boolean sleep = false;
	public boolean surfing = false;
	public String spawnMap;
	public String name;

	public Pokemon[] heldPokemon;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}

	
	public void update() {

		gp.cChecker.resolveStuckState(this);
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkPlayer(this);
		gp.cChecker.checkEntity(this, gp.currentMap.npcList);
		gp.cChecker.checkSpecialBlocks(this);

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

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int screenX = (int) ((worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX);
		int screenY = (int) ((worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY);
		
		if(true) {

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
			//System.out.println(this.name  + " " + (image == null));
		}
	}
	public static Rectangle generateSolidArea(BufferedImage sprite) {
		int width = sprite.getWidth();
		int height = sprite.getHeight();
		int minX = width, minY = height, maxX = -1, maxY = -1;
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				Color color = new Color(sprite.getRGB(x, y), true);
				if(color.getAlpha() > 0) {
					if(x < minX) minX = x;
					if(y < minY) minY = y;
					if(x > maxX) maxX = x;
					if(y > maxY) maxY = y;
				}
			}
		}
		if(maxX == -1) return new Rectangle(0, 0, 0, 0);
		return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
	}
	public Rectangle getSolidArea() {
		int screenX = (int) ((worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX);
		int screenY = (int) ((worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY);
		return new Rectangle(screenX + (int) (solidArea.x*solidAreaXScale), screenY + (int) (solidArea.y*solidAreaYScale), (int) (solidArea.width*solidAreaXScale), (int) (solidArea.height*solidAreaYScale));
	}
}
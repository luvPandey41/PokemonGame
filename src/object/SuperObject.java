package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class SuperObject {
	public BufferedImage image;
	public String name; 
	public boolean collision = false;
	public int worldX, worldY;
	public String map;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX = 0;
	public int solidAreaDefaultY = 0;
	public String description;
	public int quantity;
	public void draw(Graphics2D g2, GamePanel gp) {
         if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
        //	 g2.drawImage(image, (int) worldX - gp.player.worldX + gp.player.screenX, (int) worldY - gp.player.worldY + gp.player.screenY, gp.tileSize, gp.tileSize, null);
         }
	}
}
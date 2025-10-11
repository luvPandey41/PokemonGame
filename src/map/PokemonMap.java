package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.NPC;
import entity.NPCFactory;
import main.GamePanel;

public class PokemonMap {
	public String mapName;
	public BufferedImage mapImage;
	public BufferedImage collisionMapImage;
	public BufferedImage areaIconImage;
	public GamePanel gp;
	public SpawnSheet spawnsheet;
	public double scale;
	
	public ArrayList<NPC> npcList;
	
	
	public PokemonMap(String mapName, String path, String collisionMapPath, String areaIconPath, GamePanel gp) {
		this.mapName = mapName;
		this.gp = gp;
		try {
			mapImage = ImageIO.read(getClass().getResourceAsStream(path));
			collisionMapImage = ImageIO.read(getClass().getResourceAsStream(collisionMapPath));
			areaIconImage = ImageIO.read(getClass().getResourceAsStream(areaIconPath));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		spawnsheet = new SpawnSheet(gp); 
		scale = 1;
		
		try {
			npcList = NPCFactory.loadNPCs("res/npc_data/" + path.substring(5, path.length() - 4) + "_npcs", gp);
		} catch (IOException e) {
			System.out.println("Npc data file didnt load properly for map " + mapName + ".");
			e.printStackTrace();
		}
	}
	public PokemonMap(String mapName, String path, String collisionMapPath, String areaIconPath, double scale, GamePanel gp) {
		this.mapName = mapName;
		this.gp = gp;
		try {
			mapImage = ImageIO.read(getClass().getResourceAsStream(path));
			collisionMapImage = ImageIO.read(getClass().getResourceAsStream(collisionMapPath));
			areaIconImage = ImageIO.read(getClass().getResourceAsStream(areaIconPath));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		spawnsheet = new SpawnSheet(gp); 
		this.scale = scale;
		
		try {
			npcList = NPCFactory.loadNPCs("res/npc_data/" + path.substring(5, path.length() - 4) + "_npcs", gp);
		} catch (IOException e) {
			System.out.println("Npc data file didnt load properly for map " + mapName + ".");
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		double a = 2.0;
		double b = 1.5;
		g.drawImage(mapImage, 0, 0, gp.screenWidth, gp.screenHeight, (int) ((gp.player.worldX - 50) *  a*scale), (int) ((gp.player.worldY-50) * b * scale) , (int)((gp.player.worldX + 50) * a * scale), (int)((gp.player.worldY + 50) * b * scale), null);
	}
}
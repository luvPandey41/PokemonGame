package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import entity.Entity;
import entity.NPC;
import entity.Player;
import map.SpecialBlock;

public class CollisionChecker {
	//UPDATE BIKE COLLISION
	
	GamePanel gp;
	public CollisionChecker (GamePanel gp) {
		this.gp = gp;
	}
	
	
	public void checkTile(Entity entity) {
//		if(entity.surfing) {
//			switch(entity.direction){
//				case "up":
//					checkColor(entity, new Color[] {new Color(0, 0, 0), new Color(150, 75, 0)});
//					break;
//				case "down":
//					checkColor(entity, new Color[] {new Color(0, 0, 0)});
//					break;
//				case "left":
//					checkColor(entity, new Color[] {new Color(0, 0, 0), new Color(150, 75, 0)});
//					break;
//				case "right":
//					checkColor(entity, new Color[] {new Color(0, 0, 0), new Color(150, 75, 0)});
//					break;
//			}
//			return;
//		}
		switch(entity.direction){
			case "up":
				checkColor(entity, new Color[] {new Color(0, 0, 255), new Color(0, 0, 0), new Color(150, 75, 0)});
				break;
			case "down":
				checkColor(entity, new Color[] {new Color(0, 0, 255), new Color(0, 0, 0)});
				break;
			case "left":
				checkColor(entity, new Color[] {new Color(0, 0, 255), new Color(0, 0, 0), new Color(150, 75, 0)});
				break;
			case "right":
				checkColor(entity, new Color[] {new Color(0, 0, 255), new Color(0, 0, 0), new Color(150, 75, 0)});
				break;
		}

	}
	
	
	public void checkColor(Entity entity, Color[] colors) {
		int dirX = 0, dirY = 0;
		switch(entity.direction) {
			case "up": dirY = -1; break;
			case "down": dirY = 1; break;
			case "left": dirX = -1; break;
			case "right": dirX = 1; break;
		}
		double futureLeftX = (entity.worldX*2 + dirX * entity.speed * 2) + ((entity.solidArea.x - entity.solidArea.width/2)/2.0) - 4;
		double futureTopY = (entity.worldY*1.5 + dirY * entity.speed * 1.5) + ((entity.solidArea.y - entity.solidArea.height/2)/1.5) - 2;
		if(containsColorOrOutOfBounds(new Rectangle((int) (futureLeftX*gp.currentMap.scale), (int) (futureTopY*gp.currentMap.scale), (int) (entity.solidArea.width*2*100/gp.screenWidth*gp.currentMap.scale*5), (int) (entity.solidArea.height*150/gp.screenHeight*gp.currentMap.scale*2)), colors)) entity.collisionOn = true;
	}
	
	public boolean containsColorOrOutOfBounds(Rectangle rect, Color[] colors) {
		for(int k = rect.x; k < rect.x + rect.width; k++) {
			for(int j = rect.y; j < rect.y + rect.height; j++) {
				if(k<0||j<0||k>=gp.currentMap.collisionMapImage.getWidth()|| j>=gp.currentMap.collisionMapImage.getHeight()) {
					return true;
				}
				for(Color color : colors) {
					if(new Color(gp.currentMap.collisionMapImage.getRGB(k,j), true).equals(color)) { 
						return true;	
					}
				}
			}
		}
		return false;
	}
	
	
	public void checkEntity(Entity entity, ArrayList<NPC> npcList) {
		Rectangle eRect = new Rectangle(entity.getSolidArea());
		switch (entity.direction) {
			case "up": eRect.y -= entity.speed*2; break;
	     	case "down": eRect.y += entity.speed*2; break;
	        case "left": eRect.x -= entity.speed*2; break;
	        case "right": eRect.x +=entity.speed*2; break;
		}
	    for (int i = 0; i < npcList.size(); i++) {
	        NPC target = npcList.get(i);
	        if (target != null && !target.name.equals(entity.name)) {
	        	Rectangle tRect = new Rectangle(target.getSolidArea());
	        	switch (target.direction) {
					case "up": tRect.y -= target.speed*2; break;
			     	case "down": tRect.y += target.speed*2; break;
			        case "left": tRect.x -= target.speed*2; break;
			        case "right": tRect.x += target.speed*2; break;
	        	}
	        	if(eRect.intersects(tRect)) entity.collisionOn = true;
	        }
	    }
	}

	
	public void checkPlayer(Entity entity) {
		Rectangle eRect = new Rectangle(entity.getSolidArea());
		switch(entity.direction) {
			case "up":
				eRect.y -= entity.speed;
				break;
			case "down":
				eRect.y += entity.speed;
				break;
			case "left":
				eRect.x -= entity.speed;
				break;
			case "right":
				eRect.x += entity.speed;
				break;
		}
		if(eRect.intersects(gp.player.getSolidArea())) entity.collisionOn = true;
	}
	
	public void checkSpecialBlocks(Entity entity) {
		Rectangle entityRect = entity.getSolidArea();
		switch(entity.direction) {
		case "up":
			entityRect.y-= gp.screenHeight/100*entity.speed;
			break;
		case "down":
			entityRect.y += gp.screenHeight/100*entity.speed;
			break;
		case "left":
			entityRect.x -= gp.screenWidth/100*entity.speed;
			break;
		case "right":
			entityRect.x += gp.screenWidth/100*entity.speed;
			break;
		}
		for(SpecialBlock block : gp.specialBlocks) {
			if(!block.map.equals(gp.currentMap.mapName)) continue;
			if(!block.collision) continue;
			Rectangle blockRect = new Rectangle((int) ((block.worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX), (int) ((block.worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY), gp.tileSize, gp.tileSize);
			if(block.name.equals("Top Mailbox")) blockRect = new Rectangle((int) ((block.worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX), (int) ((block.worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY + 2*gp.tileSize/3), gp.tileSize, gp.tileSize/3);
			if(blockRect.intersects(entityRect)) entity.collisionOn = true;
		}
	}
	
	public void draw(Graphics2D g) {
		boolean showCollisionMap = false;
		boolean showEntityHitboxes = false;
		boolean showSpecialBlocks = false;
		if(!showCollisionMap && !showEntityHitboxes && !showSpecialBlocks) return;
		
		if(showCollisionMap) {
			double a = 2.0;
			double b = 1.5;	
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
			g.drawImage(gp.currentMap.collisionMapImage, 0, 0, gp.screenWidth, gp.screenHeight, (int) ((gp.player.worldX - 50) *  a*gp.currentMap.scale), (int) ((gp.player.worldY-50) * b * gp.currentMap.scale) , (int)((gp.player.worldX + 50) * a * gp.currentMap.scale), (int)((gp.player.worldY + 50) * b * gp.currentMap.scale), null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
		if(showEntityHitboxes) {
			for(Entity entity : gp.currentMap.npcList) {
				Rectangle rect = gp.player.getSolidArea();
				Rectangle eRect = entity.getSolidArea();
				g.draw(rect);
				g.draw(eRect);
			}
		}
		if(showSpecialBlocks) {
			for(SpecialBlock block : gp.specialBlocks) {
				if(block.map.equals(gp.currentMap.mapName)) {
					Rectangle blockRect = new Rectangle((int) ((block.worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX), (int) ((block.worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY), gp.tileSize, gp.tileSize);
					g.draw(blockRect);
				}
			}
		}
	}
	
	public void resolveStuckState(Entity entity) {
		if(!isAreaOccupied(entity.worldX, entity.worldY, entity.solidArea, entity)) return;
		int pushDistance = 1;
		int maxPush = gp.tileSize/2;
		while(pushDistance <= maxPush) {
			if(!isAreaOccupied(entity.worldX  + pushDistance, entity.worldY, entity.solidArea, entity)) {
				entity.worldX = entity.worldX + pushDistance;
				return;
			}
			if(!isAreaOccupied(entity.worldX - pushDistance, entity.worldY, entity.solidArea, entity)) {
				entity.worldX = entity.worldX - pushDistance;
				return;
			}
			if(!isAreaOccupied(entity.worldX, entity.worldY + pushDistance, entity.solidArea, entity)) {
				entity.worldY = entity.worldY + pushDistance;
				return;
			}
			if(!isAreaOccupied(entity.worldX, entity.worldY - pushDistance, entity.solidArea, entity)) {
				entity.worldY = entity.worldY - pushDistance;
				return;
			}
			pushDistance++;
		}
		System.out.println("Failed");
	}
	
	public boolean isAreaOccupied(double worldX, double worldY, Rectangle solidArea, Entity entity) {
		Color[] colors = new Color[] {new Color(0, 0, 255), new Color(0, 0, 0), new Color(150, 75, 0)};
		double rectX = worldX * 2  + ((solidArea.x - solidArea.width / 2.0) / 2.0) - 4;
		double rectY = worldY * 1.5 + ((solidArea.y - solidArea.height / 2.0) / 1.5) - 2;
		int rectW = (int) (solidArea.width * 2 * 100 / gp.screenWidth * gp.currentMap.scale * 5);
		int rectH = (int) (solidArea.height * 150 / gp.screenHeight * gp.currentMap.scale * 2);
		Rectangle cRect = new Rectangle ((int) (rectX * gp.currentMap.scale), (int) (rectY*gp.currentMap.scale), rectW, rectH);
		if(containsColorOrOutOfBounds(cRect, colors)) return true;
		int screenX = (int) ((worldX - gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX);
		int screenY = (int) ((worldY - gp.player.worldY)*gp.screenHeight/100 + gp.player.screenY);
		Rectangle eRect = new Rectangle(screenX + (int) (solidArea.x*entity.solidAreaXScale), screenY + (int) (solidArea.y*entity.solidAreaYScale), (int) (solidArea.width*entity.solidAreaXScale), (int) (solidArea.height*entity.solidAreaYScale));
		if(!entity.name.equals(gp.player.name)) if(eRect.intersects(gp.player.getSolidArea())) return true;
		for(Entity npc : gp.currentMap.npcList) if(!entity.name.equals(npc.name)) if(eRect.intersects(npc.getSolidArea())) return true;
		return false;
	}

}
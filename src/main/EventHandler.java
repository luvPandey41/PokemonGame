package main;

import java.awt.Rectangle;

import map.PokemonMap;

public class EventHandler {
	
	GamePanel gp;
	Rectangle eventRect;
	int eventRectDefaultX, eventRectDefaultY;
	int switchX, switchY;
	boolean leftSwitchTile = true;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		
		eventRect = new Rectangle();
		eventRect.x = 23;
		eventRect.y = 23;
		eventRect.width = 2;
		eventRect.height = 2;
		eventRectDefaultX = eventRect.x;
		eventRectDefaultY = eventRect.y;
	}
	
	public void checkEvent() {
		if(Math.abs(switchX - gp.player.worldX) > 10 || Math.abs(switchY - gp.player.worldY) > 10 ) leftSwitchTile = true;
		if(hit(80, 30, "right", gp.player.name+"'s Room", 10, 0)) {switchMap(76, 27, gp.player.name+"'s House");}
		if(hit(76, 27, "right", gp.player.name+"'s House", 2, 10)) {switchMap(80, 30, gp.player.name+"'s Room");}
		if(hit(40, 93, "down", gp.player.name+"'s House", 2, 10)) {switchMap(63, 96, "Maplewood Town");}
		if(hit(63, 96, "up", "Maplewood Town", 10, 8)) {switchMap(40, 93, gp.player.name+"'s House");}
		if(hit(145, 209, "up", "Maplewood Town", 10, 8)) {switchMap(65, 145, "Pokemon Lab");}
		if(hit(65, 145, "down", "Pokemon Lab", 20, 0)) {switchMap(145, 209, "Maplewood Town");}
		if(hit(120, 5, "up", "Maplewood Town", 30, 8)) {switchMap(76, 420, "Route 1");}
		if(hit(76, 420, "down", "Route 1", 30, 8)) {switchMap(120, 5, "Maplewood Town");}
		if(hit(75, 73, "up", "Route 1", 23, 0)) {switchMap(130, 418, "Moonveil City");}
		if(hit(130, 420, "down", "Moonveil City", 30, 0)) {switchMap(75, 75, "Route 1");}
		if(hit(157, 261, "up", "Moonveil City", 10, 0)) {switchMap(60, 91, "Moonveil Pokecenter");}
		if(hit(252, 261, "up", "Moonveil City", 10, 0)) {switchMap(36, 80, "Moonveil Pokemart");}
		if(hit(10, 68, "left", "Moonveil Pokecenter", 10, 5)) {switchMap(10, 68, "Moonveil Pokecenter, Floor 2");}
		if(hit(10, 68, "right", "Moonveil Pokecenter, Floor 2", 10, 5)) {switchMap(10, 68, "Moonveil Pokecenter");}
		if(hit(60, 91, "down", "Moonveil Pokecenter", 15, 0)) {switchMap(157, 261, "Moonveil City");}
		if(hit(36, 80, "down", "Moonveil Pokemart", 15, 0)) {switchMap(252, 260, "Moonveil City");}
		if(hit(116, 5, "up", "Moonveil City", 30, 8)) {switchMap(52, 687, "Route 2");}
		if(hit(52, 687, "down", "Route 2", 30, 8)) {switchMap(116, 5, "Moonveil City");}
		if(hit(6, 582, "left", "Route 2", 8, 30)) {switchMap(236, 255, "Sandrift Forest");}
		if(hit(235, 255, "right", "Sandrift Forest", 8, 30)) {switchMap(6, 582, "Route 2");}
		if(hit(231, 184, "right", "Sandrift Forest", 8, 30)) {switchMap(7, 109, "Route 2");}
		if(hit(7, 109, "left", "Route 2", 8, 30)) {switchMap(231, 184, "Sandrift Forest");}
		if(hit(46, 5, "up", "Route 2", 30, 8)) {switchMap(93, 310, "Sandrift City");}
		if(hit(94, 310, "down", "Sandrift City", 30, 8)) {switchMap(46, 5, "Route 2");}
		if(hit(42, 172, "left", "Sandrift City", 8, 30)) {switchMap(286, 150, "Route 3");}
		if(hit(210, 12, "up", "Route 3", 8, 10)) {switchMap(84, 290, "Slitstone Grotto Entrance");}
		if(hit(84, 294, "down", "Slitstone Grotto Entrance", 8, 10)) {switchMap(210, 24, "Route 3");}
		if(hit(70, 74, "up", "Slitstone Grotto Entrance", 8, 10)) {switchMap(84, 294, "Slitstone Grotto, Floor 2");}
		if(hit(40, 44, "up", "Pokemon Lab", 15, 8)) {starterSelection();}
	}
	
	public boolean hit(int eventX, int eventY, String reqDirection, String map, int xRad, int yRad) {
		boolean hit = false;
		
		//dont wanna mix it up later 
		int originalEventWidth = eventRect.width;
		int originalEventHeight = eventRect.height;
		Rectangle playerRect = new Rectangle(gp.player.screenX, gp.player.screenY + 15, gp.player.solidArea.width, gp.player.solidArea.height);
		eventRect.width += xRad*gp.screenWidth/100;
		eventRect.height += yRad*gp.screenHeight/100;
		eventRect.x = (int) ((eventX -  gp.player.worldX)*gp.screenWidth/100 + gp.player.screenX) - eventRect.width/2;
		eventRect.y = (int) ((eventY - gp.player.worldY + 2)*gp.screenHeight/100 + gp.player.screenY) - eventRect.height/2;
		
		if(gp.currentMap.mapName.equals(map)) {
			if(playerRect.intersects(eventRect)) {
				if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
					hit = true;
				}
			}
		}
//		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
//		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect.x = eventRectDefaultX;
		eventRect.y = eventRectDefaultY;
		eventRect.width = originalEventWidth; 
		eventRect.height = originalEventHeight;
		
		return hit;
	}
	
	public void starterSelection() {
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			gp.gameState = gp.menuState;
		}
	}
	public void switchMap(int newX, int newY, String newMap) {
		if(leftSwitchTile) {
			for(PokemonMap i : gp.maps) {
				if(i.mapName.equals(newMap)) {
					gp.currentMap = i;
				}
			}
			gp.player.worldX = newX;
			gp.player.worldY = newY;
			leftSwitchTile = false;
			switchX = newX;
	    	switchY = newY;
	    	gp.ui.switchTime = System.currentTimeMillis();
		}
	}
	
}
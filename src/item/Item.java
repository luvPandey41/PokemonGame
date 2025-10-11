package item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Item {
	public String name, description, category, pocket, effectDescription, moveName;
	public long cost;
	public boolean stackable = false, countable = false, consumable = false, usableOverworld = false, usableInBattle = false, holdable = false, holdablePassive = false, holdableActive = false, underground = false;
	public BufferedImage image;
	public int quantity, flingPower;
	public Item(String name) {
		try {
			Scanner fileScanner =  new Scanner(new File("res/item_data/" + name.toLowerCase().replace(" ", "-") + ".txt"));
			String str = fileScanner.nextLine(); 
			this.name = formatName(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.description = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.cost = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.category = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.pocket = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			String[] attributes = str.substring(str.indexOf(":")+2).split("\\|");
			for(String attribute : attributes) {
				switch(attribute.trim()) {
				case "countable":
					countable = true;
					break;
				case "consumable":
					consumable = true;
					break;
				case "usable-overworld":
					usableOverworld = true;
					break;
				case "usable-in-battle":
					usableInBattle = true;
					break;
				case "holdable-passive":
					holdablePassive = true;
					break;
				case "holdable-active":
					holdableActive = true;
					break;
				case "underground":
					underground = true;
					break;
				}
			}
			str = fileScanner.nextLine();
			this.holdable = str.substring(str.indexOf(":")+2).trim().equals("yes") ? true : false;
			str = fileScanner.nextLine();
			this.stackable = str.substring(str.indexOf(":")+2).trim().equals("yes") ? true : false;
			str = fileScanner.nextLine();
			this.effectDescription = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.flingPower = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.consumable = str.substring(str.indexOf(":")+2).trim().equals("yes") ? true : false;
			str = fileScanner.nextLine();
			this.moveName = str.substring(str.indexOf(":")+2);
			fileScanner.close();
			image = ImageIO.read(new File("res/item_images/" + name.toLowerCase().replace(" ", "-") + ".png"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(pocket.equals("machines")) name = moveName;
	}
	public static String formatName(String name) {
		if(name == null || name.isEmpty()) return name;
		name = name.replace("-", " ");
		name = (name.charAt(0)+"").toUpperCase() + name.substring(1);
		for(int i = 1; i < name.length(); i++) {
			if(name.charAt(i-1)==' ') {
				name = name.substring(0, i) + (name.charAt(i)+"").toUpperCase() + name.substring(i+1);
				i++;
			}
		}
		return name;
	}
	public static void addItem(String name, int quantity, GamePanel gp) {
		Item item = new Item(name);
		item.quantity = quantity;
		gp.player.inventory.add(item);
		switch(item.pocket) {
		case "medicine": 
			gp.player.medicine.add(item);
			break;
		case "berries":
			gp.player.berries.add(item);
			break;
		case "key":
			gp.player.keyItems.add(item);
			break;
		case "machines":
			gp.player.machines.add(item);
		}
		
	}
	
}
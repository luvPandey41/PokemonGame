package main;

import java.io.FileWriter;
import java.util.Scanner;

import item.Item;
import map.PokemonMap;
import pokemon.Pokemon;

import java.io.File;
import java.io.FileNotFoundException;

public class SaveManager {
	public GamePanel gp;
	public SaveManager(GamePanel gp) {
		this.gp = gp;
	}
	public void save() {
		String filePathStart = "res/save_states/";
		int i = 1;
		while(true) {
			if(!new File(filePathStart+i).exists()) {
				save(i);
				return;
			}
			i++;
		}
	}
	public void save(int num) {
		String filePath = "res/save_states/" + num + ".txt";
		try(FileWriter writer = new FileWriter(filePath, false)){
			writer.write(gp.player.name+"\n");
			writer.write(gp.timePlayed+"\n");
			writer.write(gp.player.worldX + "|" + gp.player.worldY+"\n");
			writer.write(gp.currentMap.mapName+"\n");
			String itemNames = "", quantities = "";
			for(int i = 0; i < gp.player.inventory.size(); i++) {
				if(i>0){
					itemNames += "|";
					quantities += "|";
				}
				itemNames += gp.player.inventory.get(i).name;
				quantities += gp.player.inventory.get(i).quantity;
			}
			writer.write(itemNames + "\n");
			writer.write(quantities + "\n");
			String names = "", abilities = "", abilityDescriptions = "", lvls = "", areShinies = "", spawnLocations = "", 
			originalTrainers = "", genders = "", spawnLevels = "", IDNos = "", xps = "", firstMoves = "", secondMoves = "", thirdMoves = "", fourthMoves = "";
			for(int i = 0; i < gp.player.heldPokemon.length; i++) {
				Pokemon mon = gp.player.heldPokemon[i];
				if(i>0) {
					names += "|";
					abilities += "|";
					abilityDescriptions += "|";
					lvls += "|";
					areShinies += "|";
					spawnLocations += "|";
					originalTrainers += "|";
					genders += "|";
					spawnLevels += "|";
					IDNos += "|";
					xps += "|";
					firstMoves += "|";
					secondMoves += "|";
					thirdMoves += "|";
					fourthMoves += "|";
				}
				if(mon!=null) {
					names += mon.name;
					abilities += mon.ability;
					abilityDescriptions += mon.abilityDescription;
					lvls += mon.lvl;
					areShinies += mon.isShiny;
					spawnLocations += mon.spawnLocation;
					originalTrainers += mon.originalTrainer;
					genders += mon.gender;
					spawnLevels += mon.spawnLevel;
					IDNos += mon.IDNo;
					xps += mon.xp;
					firstMoves += (mon.currentMoves[0] == null ? "" : mon.currentMoves[0].name);
					secondMoves += (mon.currentMoves[1] == null ? "" : mon.currentMoves[1].name);
					thirdMoves += (mon.currentMoves[2] == null ? "" : mon.currentMoves[2].name);
					fourthMoves += (mon.currentMoves[3] == null ? "" : mon.currentMoves[3].name);
				}
			}
			writer.write(names + "\n");
			writer.write(abilities + "\n");
			writer.write(abilityDescriptions + "\n");
			writer.write(lvls + "\n");
			writer.write(areShinies + "\n");
			writer.write(spawnLocations + "\n");
			writer.write(originalTrainers + "\n");
			writer.write(genders + "\n");
			writer.write(spawnLevels + "\n");
			writer.write(IDNos + "\n");
			writer.write(xps + "\n");
			writer.write(firstMoves + "\n");
			writer.write(secondMoves + "\n");
			writer.write(thirdMoves + "\n");
			writer.write(fourthMoves + "\n");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void load(int num) {
		String filePath = "res/save_states/" + num + ".txt";
		try {
			Scanner saveScanner = new Scanner(new File(filePath));
			gp.player.name = saveScanner.nextLine();
			gp.timePlayed = Long.parseLong(saveScanner.nextLine());
			gp.sessionStartTime = System.currentTimeMillis();
			String[] coords = saveScanner.nextLine().split("\\|");
			gp.player.worldX = Double.parseDouble(coords[0]);
			gp.player.worldY = Double.parseDouble(coords[1]);
			String mapName = saveScanner.nextLine();
			for(PokemonMap map : gp.maps) {
				if(map.mapName.equals(mapName)) {
					gp.currentMap = map;
				}
			}
			String[] itemNames = saveScanner.nextLine().split("\\|");
			String[] itemQuantities = saveScanner.nextLine().split("\\|");
			for(int i = 0; i < itemNames.length; i++) {
				Item.addItem(itemNames[i], Integer.parseInt(itemQuantities[i]), gp);
			}
			String[] names = saveScanner.nextLine().split("\\|");
			String[] abilities = saveScanner.nextLine().split("\\|");
			String[] abilityDescriptions = saveScanner.nextLine().split("\\|");
			String[] lvls = saveScanner.nextLine().split("\\|");
			String[] areShinies = saveScanner.nextLine().split("\\|");
			String[] spawnLocations = saveScanner.nextLine().split("\\|");
			String[] originalTrainers = saveScanner.nextLine().split("\\|");
			String[] genders = saveScanner.nextLine().split("\\|");
			String[] spawnLevels = saveScanner.nextLine().split("\\|");
			String[] IDNos = saveScanner.nextLine().split("\\|");
			String[] xps = saveScanner.nextLine().split("\\|");
			String[] firstMoves = saveScanner.nextLine().split("\\|");
			String[] secondMoves = saveScanner.nextLine().split("\\|");
			String[] thirdMoves = saveScanner.nextLine().split("\\|");
			String[] fourthMoves = saveScanner.nextLine().split("\\|");
			for(int i = 0; i < names.length; i++) {
				Pokemon.addPokemon(names[i], abilities[i], abilityDescriptions[i], Integer.parseInt(lvls[i]), Boolean.getBoolean(areShinies[i]), spawnLocations[i], originalTrainers[i],
				genders[i], Integer.parseInt(spawnLevels[i]), Integer.parseInt(IDNos[i]), Double.parseDouble(xps[i]), firstMoves[i], secondMoves[i], thirdMoves[i], fourthMoves[i], gp);
			}
			saveScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public String[] getPreviewInfo(int num) {
		String filePath = "res/save_states/" + num + ".txt";
		String[] ret = new String[3];
		ret[1] = "0";
		try {
			Scanner saveScanner = new Scanner(new File(filePath));
			ret[0] = saveScanner.nextLine();
			ret[2] = saveScanner.nextLine();
			saveScanner.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
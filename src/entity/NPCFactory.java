package entity;

import java.io.*;
import java.util.*;
import entity.NPC;
import main.GamePanel;
import pokemon.Pokemon;

public class NPCFactory {

    public static ArrayList<NPC> loadNPCs(String filePath, GamePanel gp) throws IOException {
        ArrayList<NPC> npcList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        NPC currentNPC = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("Map:")) continue;

            if (line.startsWith("---")) {
                if (currentNPC != null) {
                    npcList.add(currentNPC);
                    currentNPC = null;
                }
                continue;
            }

            if (currentNPC == null) {
                currentNPC = new NPC(gp);
            }

            if (line.startsWith("name:")) {
                currentNPC.name = line.substring(5).trim();
            } 
            
            else if (line.startsWith("spriteBase:")) {
                String spriteBase = line.substring("spriteBase:".length()).trim();
                currentNPC.loadSprites(spriteBase);
            } 
            
            else if (line.startsWith("x:")) {
            	currentNPC.worldX  = Double.parseDouble(line.substring(2).trim());
            } 
            
            else if (line.startsWith("y:")) {
            	currentNPC.worldY  = Double.parseDouble(line.substring(2).trim());
            } 
            
            else if (line.startsWith("direction:")) {
                currentNPC.direction = line.substring(10).trim();
            } 
            
            else if (line.startsWith("dialogue:")) {
            	currentNPC.lines = new ArrayList<>((Arrays.asList(line.substring(9).trim().split("\\|"))));
            } 
            
            else if (line.startsWith("team:")) {
                currentNPC.isTrainer = true;
                String[] teamParts = line.substring(5).trim().split("\\|");
                
                for(String i : teamParts) {
                	int idx = i.indexOf(',');
                	String species = i.substring(0, idx);
                	int level = Integer.parseInt(i.substring(idx + 1));
                	
                	for(int j = 0; j < 6 ; j++) {
                		if(currentNPC.heldPokemon[j] == null) {
                			currentNPC.heldPokemon[j] = new Pokemon(gp, species, level);
                			break;
                		}
                	}
                }
            }
            
            else if(line.startsWith("preBattleDialogue:")) {
            	currentNPC.preBattleDialogue = new ArrayList<>(Arrays.asList(line.substring(18).trim().split("\\|")));
            }
            
            else if(line.startsWith("longRangeInteractor")) {
            	currentNPC.longRangeInteractor = true;
            }
            
            else if(line.startsWith("ID:")) {
            	currentNPC.ID = Integer.parseInt(line.substring(3).trim());
            }
            
            else if(line.startsWith("notMovingBattler")) {
            	currentNPC.movingBattler = false;
            }
        
        }
        

        if (currentNPC != null) {
            npcList.add(currentNPC);
        }

        reader.close();
        return npcList;
    }
}

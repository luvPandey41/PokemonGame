package map;

import java.util.ArrayList;

import main.GamePanel;
import pokemon.Pokemon;

public class SpawnSheet {
	public ArrayList<String> grassEncounters;
	public ArrayList<String> fishEncounters;
	public ArrayList<String> surfEncounters;
	public ArrayList<String> caveEncounters;
	public ArrayList<Integer> grassLowerLevels;
	public ArrayList<Integer> grassHigherLevels;
	public ArrayList<Double> grassPercentages;
	public ArrayList<Integer> fishLowerLevels;
	public ArrayList<Integer> fishHigherLevels;
	public ArrayList<Double> fishPercentages;
	public ArrayList<Integer> surfLowerLevels;
	public ArrayList<Integer> surfHigherLevels;
	public ArrayList<Double> surfPercentages;
	public ArrayList<Integer> caveLowerLevels;
	public ArrayList<Integer> caveHigherLevels;
	public ArrayList<Double> cavePercentages;
	public GamePanel gp;
	public double grassTot;
	public double fishTot;
	public double surfTot;
	public double caveTot;
	public SpawnSheet(GamePanel gp) {
		grassEncounters = new ArrayList<>();
		fishEncounters = new ArrayList<>();
		surfEncounters = new ArrayList<>();
		caveEncounters = new ArrayList<>();
		grassLowerLevels = new ArrayList<>();
		grassHigherLevels = new ArrayList<>();
		grassPercentages = new ArrayList<>();
		fishLowerLevels = new ArrayList<>();
		fishHigherLevels = new ArrayList<>();
		fishPercentages = new ArrayList<>();
		surfLowerLevels = new ArrayList<>();
		surfHigherLevels = new ArrayList<>();
		surfPercentages = new ArrayList<>();
		caveLowerLevels = new ArrayList<>();
		caveHigherLevels = new ArrayList<>();
		cavePercentages = new ArrayList<>();
		this.gp = gp;
		grassTot = 0; fishTot = 0; surfTot = 0; caveTot = 0;
	}
	public void addGrassEncounter(String name, int lowerLevel, int higherLevel, double percentage) {
		grassEncounters.add(name);
		grassLowerLevels.add(lowerLevel);
		grassHigherLevels.add(higherLevel);
		grassPercentages.add(percentage);
		grassTot += percentage;
	}
	public void addFishEncounter(String name, int lowerLevel, int higherLevel, double percentage) {
		fishEncounters.add(name);
		fishLowerLevels.add(lowerLevel);
		fishHigherLevels.add(higherLevel);
		fishPercentages.add(percentage);
		fishTot += percentage;
	}
	public void addSurfEncounter(String name, int lowerLevel, int higherLevel, double percentage) {
		surfEncounters.add(name);
		surfLowerLevels.add(lowerLevel);
		surfHigherLevels.add(higherLevel);
		surfPercentages.add(percentage);
		surfTot += percentage;
	}
	public void addCaveEncounter(String name, int lowerLevel, int higherLevel, double percentage) {
		caveEncounters.add(name);
		caveLowerLevels.add(lowerLevel);
		caveHigherLevels.add(higherLevel);
		cavePercentages.add(percentage);
		caveTot += percentage;
	}
	public Pokemon grassEncounter() {
		if(grassEncounters.isEmpty()) return null;
		int randomInt = (int) (Math.random()*grassTot);
		double runningTot = 0;
		int index = 0;
		while(index < grassPercentages.size() && runningTot + grassPercentages.get(index) <= randomInt) {
			runningTot += grassPercentages.get(index);
			index++;
		}
		int level = grassLowerLevels.get(index) + (int)(Math.random() * (grassHigherLevels.get(index) - grassLowerLevels.get(index) + 1));
		return new Pokemon(gp, grassEncounters.get(index), level);
	}
	public Pokemon fishEncounter() {
		if(fishEncounters.isEmpty()) return null;
		int randomInt = (int) (Math.random()*fishTot);
		double runningTot = 0;
		int index = 0;
		while(index < fishPercentages.size() && runningTot + fishPercentages.get(index) <= randomInt) {
			runningTot += fishPercentages.get(index);
			index++;
		}
		int level = fishLowerLevels.get(index) + (int)(Math.random() * (fishHigherLevels.get(index) - fishLowerLevels.get(index) + 1));
		return new Pokemon(gp, fishEncounters.get(index), level);
	}
	public Pokemon surfEncounter() {
		if(surfEncounters.isEmpty()) return null;
		int randomInt = (int) (Math.random()*surfTot);
		double runningTot = 0;
		int index = 0;
		while(index < surfPercentages.size() && runningTot + surfPercentages.get(index) <= randomInt) {
			runningTot += surfPercentages.get(index);
			index++;
		}
		int level = surfLowerLevels.get(index) + (int)(Math.random() * (surfHigherLevels.get(index) - surfLowerLevels.get(index) + 1));
		return new Pokemon(gp, surfEncounters.get(index), level);
	}
	public Pokemon caveEncounter() {
		if(caveEncounters.isEmpty()) return null;
		int randomInt = (int) (Math.random()*caveTot);
		double runningTot = 0;
		int index = 0;
		while(index < cavePercentages.size() && runningTot + cavePercentages.get(index) <= randomInt) {
			runningTot += cavePercentages.get(index);
			index++;
		}
		int level = caveLowerLevels.get(index) + (int)(Math.random() * (caveHigherLevels.get(index) - caveLowerLevels.get(index) + 1));
		return new Pokemon(gp, caveEncounters.get(index), level);
	}
}
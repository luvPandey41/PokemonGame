package pokemon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import item.Item;
import main.GamePanel;

import java.io.File;
import java.io.FileNotFoundException;
public class Pokemon {
	
	public GamePanel gp;
	
	public ArrayList<String> levelUpMoves;
	public ArrayList<Integer> levelsLearnedAt;
	public ArrayList<String> machineMoves;
	public ArrayList<String> eggMoves;
	public ArrayList<String> tutorMoves;
	public ArrayList<String> moveSet;
	public Move[] currentMoves;
	
	public String primaryType, secondaryType, name, ability, evolutionName, primaryAilment;
	public ArrayList<String> volatileAilments;
	public Item heldItem;
	public int spcAtkM, atkM, defM, spcDefM, spdM; //stat modifiers (+1, -6, etc.)
	public int hlt, spcAtk, atk, def, spcDef, spd;
	public int bHlt, bSpcAtk, bAtk, bDef, bSpcDef, bSpd;
	public int ivHlt, ivAtk, ivSpcAtk, ivDef, ivSpcDef, ivSpd;
	public int evHlt, evAtk, evSpcAtk, evDef, evSpcDef, evSpd;
	//ev yields
	public int hltY, spcAtkY, atkY, defY, spcDefY, spdY;
	public int lvl, evolutionLevel, hp;
	
	public BufferedImage[] front, back;
	public BufferedImage icon;
	public boolean isShiny;
	
	public String spawnLocation, originalTrainer, nature, catchDate; //nature may be implemented in a future update.
	public int spawnLevel, IDNo;
	
	public double xp, height, weight;
	
	//fill these out later w/ new information supplied by PokeAPI:
	public int pokedexNum, baseXp, captureRate, baseFriendship;
	public String evolutionTrigger, evolutionItem, evolutionLocation, evolutionTime, evolutionMove, evolutionFriendship, evolutionGender, evolutionParty;
	public String description, growthRate, abilityDescription, gender; 
	
	public int turnsPoisoned = 0, sleepCount = 0, sleepGoal, confusionCount = 0, confusionGoal;


	public Pokemon(GamePanel gp, String name, int level) {
		this.gp = gp;
		levelUpMoves = new ArrayList<String>();
		machineMoves = new ArrayList<String>();
		eggMoves = new ArrayList<String>();
		tutorMoves = new ArrayList<String>();
		moveSet = new ArrayList<String>();
		currentMoves = new Move[4];
		levelsLearnedAt = new ArrayList<Integer>();
		primaryAilment = null;
		volatileAilments = new ArrayList<>();
		createPokemon(name, level);
	}
	
	
	public int getEffectiveStat(String stat) {
		double multiplier = 1.0;
		switch(stat.trim()) {
			case "attack": {
				if(atkM >= 0) multiplier *= (2 + atkM) / 2.0;
				else multiplier *= 2.0 / (2 - atkM);
				if(primaryAilment != null && primaryAilment.trim().equals("burn")) multiplier *= .5;
				return atk *= multiplier;
			}
			case "special attack": {
				if(spcAtkM >= 0) multiplier *= (2 + spcAtkM) / 2.0;
				else multiplier *= 2.0 / (2 - spcAtkM);
				return spcAtk *= multiplier;
			}
			case "defense": {
				if(defM >= 0) multiplier *= (2 + defM) / 2.0;
				else multiplier *= 2.0 / (2 - defM);
				return def *= multiplier;
			}
			case "special defense": {
				if(spcDefM >= 0) multiplier *= (2 + spcDefM) / 2.0;
				else multiplier *= 2.0 / (2 - spcDefM);
				return spcDef *= multiplier;
			}
			case "speed": {
				if(spdM >= 0) multiplier *= (2 + spdM) / 2.0;
				else multiplier *= 2.0 / (2 - spdM);
				if(primaryAilment != null && primaryAilment.trim().equals("paralysis")) multiplier *= .5;
				return spd *= multiplier;
			}
			default: {
				System.out.println("some error occured with the type of stat inputted in getEffectiveStat(). " + stat);
				return 0;
			}
		}
	}
	
	public void createPokemon(String name, int level) {	
		this.lvl = level;
		Scanner fileScanner;
		try{
			fileScanner = new Scanner(new File("res/pokemon_data/" + name + ".txt"));
			String str = fileScanner.nextLine();
			this.name = str.substring(str.indexOf(":")+2);
			this.name = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
			str = fileScanner.nextLine();
			this.pokedexNum = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			str = fileScanner.nextLine();
			String[] arr = str.substring(str.indexOf(":")+2).split("\\|");
			primaryType = arr[0].trim();
			secondaryType = arr.length > 1 ? arr[1].trim() : null;
			str = fileScanner.nextLine();
			this.height = Integer.parseInt(str.substring(str.indexOf(":")+2))/10.0;
			str = fileScanner.nextLine();
			this.weight = Integer.parseInt(str.substring(str.indexOf(":")+2))/10.0;
			fileScanner.nextLine();
			str = fileScanner.nextLine();
			String[] abilities =  str.substring(str.indexOf(":")+2).split("\\|");
			str = fileScanner.nextLine();
			String[] hiddenAbilities =  str.substring(str.indexOf(":")+2).split("\\|");
			str = fileScanner.nextLine();
			String[] abilityDescriptions =  str.substring(str.indexOf(":")+2).split("\\|");
			int rand = (int) (Math.random() * (abilities.length * 128 + hiddenAbilities.length));
			if(rand < 128) {
				this.ability = abilities[0];
				this.abilityDescription = abilityDescriptions[0];
			}
			else if(abilities.length==2 && rand < 256) { 
				this.ability = abilities[1];
				this.abilityDescription = abilityDescriptions[1];
			}
			else if(abilities.length==1 && rand < 129) {
				this.ability = hiddenAbilities[0];
				this.abilityDescription = abilityDescriptions[1];
			}
			else if(hiddenAbilities.length==2 && rand < 130) {
				this.ability = hiddenAbilities[1];
				this.abilityDescription = abilityDescriptions[2];
			}
			else if(abilities.length==2 && rand < 257){
				this.ability = hiddenAbilities[0];
				this.abilityDescription = abilityDescriptions[2];
			}
			fileScanner.nextLine();
			str = fileScanner.nextLine();
			arr = str.substring(str.indexOf(":")+2).split("\\|");
			this.bHlt = Integer.parseInt(arr[0].trim());
			this.bAtk = Integer.parseInt(arr[1].trim());
			this.bDef = Integer.parseInt(arr[2].trim());
			this.bSpcAtk= Integer.parseInt(arr[3].trim());
			this.bSpcDef = Integer.parseInt(arr[4].trim());
			this.bSpd = Integer.parseInt(arr[5].trim());
			str = fileScanner.nextLine();
			this.baseXp = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			arr = str.substring(str.indexOf(":")+2).split("\\|");
			this.hltY = Integer.parseInt(arr[0].trim());
			this.atkY = Integer.parseInt(arr[1].trim());
			this.defY = Integer.parseInt(arr[2].trim());
			this.spcAtkY = Integer.parseInt(arr[3].trim());
			this.spcDefY = Integer.parseInt(arr[4].trim());
			this.spdY = Integer.parseInt(arr[5].trim());
			str = fileScanner.nextLine();
			this.captureRate = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.baseFriendship = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.growthRate = str.substring(str.indexOf(":")+2);
			fileScanner.nextLine();
			fileScanner.nextLine(); //no egg groups yet
			str = fileScanner.nextLine();
			int num = Integer.parseInt(str.substring(str.indexOf(":")+2));
			if(num==-1) this.gender = "genderless";
			else this.gender = (int) (Math.random() * 8) > num ? "male" : "female";
			fileScanner.nextLine(); //no hatch cycles yet
			fileScanner.nextLine();
			str = fileScanner.nextLine();
			arr = str.substring(str.indexOf(":")+2).split("\\|");
			if(!arr[0].equals("none")) {
				String[] names = new String[arr.length];
				int[] chances = new int[arr.length];
				for(int i = 0; i < arr.length; i++) {
					names[i] = arr[i].substring(0, arr[i].indexOf(":"));
					chances[i] = Integer.parseInt(arr[i].substring(arr[i].indexOf(":")+1, arr[i].indexOf("%")));
				}
				int heldItemCounter = 0;
				rand = (int) (Math.random() * 100);
				for(int i = 0; i < arr.length; i++) {
					heldItemCounter+=chances[i];
					if(heldItemCounter > rand) {
						heldItem = new Item(names[i]);
						break;
					
					}
				}
			}
			fileScanner.nextLine();
			str = fileScanner.nextLine();
			this.description = str.substring(str.indexOf(":")+2);
			fileScanner.nextLine();
			fileScanner.nextLine();
			str = fileScanner.nextLine();
			String levelUp = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			String machine = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			String egg = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			String tutor = str.substring(str.indexOf(":")+2);
			arr = levelUp.split("\\|");
			for(int i = 0; i < arr.length; i++) {
				levelUpMoves.add(arr[i].substring(0, arr[i].indexOf(":")).trim());
				moveSet.add(arr[i].substring(0, arr[i].indexOf(":")).trim());
				levelsLearnedAt.add(Integer.parseInt(arr[i].substring(arr[i].indexOf(":")+2).trim()));
			}
			arr = machine.split("\\|");
			for(String move : arr) {
				machineMoves.add(move.trim());
				moveSet.add(move.trim());
			}
			arr = egg.split("\\|");
			for(String move : arr) {
				eggMoves.add(move.trim());
				moveSet.add(move.trim());
			}
			arr = tutor.split("\\|");
			for(String move : arr) {
				tutorMoves.add(move.trim());
				moveSet.add(move.trim());
			}
			fileScanner.nextLine();
			fileScanner.nextLine();
			str = fileScanner.nextLine();
			evolutionName = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			evolutionTrigger = str.substring(str.indexOf(":")+2);
			if(!evolutionTrigger.equals("N/A")) {
				str = fileScanner.nextLine();
				String evoLevel = str.substring(str.indexOf(":")+2);
				evolutionLevel = evoLevel.equals("null") ? -1 : Integer.parseInt(evoLevel);
				str = fileScanner.nextLine();
				evolutionItem = str.substring(str.indexOf(":")+2);
				str = fileScanner.nextLine();
				evolutionLocation = str.substring(str.indexOf(":")+2);
				str = fileScanner.nextLine();
				evolutionTime = str.substring(str.indexOf(":")+2);
				str = fileScanner.nextLine();
				evolutionMove = str.substring(str.indexOf(":")+2);
				str = fileScanner.nextLine();
				evolutionFriendship = str.substring(str.indexOf(":")+2);
				str = fileScanner.nextLine();
				evolutionGender = str.substring(str.indexOf(":")+2);
				str = fileScanner.nextLine();
				evolutionParty = str.substring(str.indexOf(":")+2);
			}
			//add forms and default form
			setMoves();
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		this.xp = calcXP(level, growthRate);
		ivHlt = (int)(Math.random()*32);
		ivAtk = (int)(Math.random()*32);
		ivSpcAtk = (int)(Math.random()*32);
		ivDef = (int)(Math.random()*32);
		ivSpcDef = (int)(Math.random()*32);
		ivSpd = (int)(Math.random()*32);
		evHlt = 0;
		evAtk = 0;
		evSpcAtk = 0;
		evDef = 0;
		evSpcDef = 0;
		evSpd = 0;
		int rand = (int) (Math.random() *25);
		String[] natures = {
			    "hardy", "lonely", "brave", "adamant", "naughty",
			    "bold", "docile", "relaxed", "impish", "lax",
			    "timid", "hasty", "serious", "jolly", "naive",
			    "modest", "mild", "quiet", "bashful", "rash",
			    "calm", "gentle", "sassy", "careful", "quirky"
			};
		this.nature = natures[(int)(Math.random() * natures.length)];
		rand = (int)(Math.random() * 2048);
		try {
			icon = ImageIO.read(new File("res/pokemon_images/" + name.toLowerCase() + "_icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(rand==0) {
			isShiny = true;
			try(ImageInputStream stream = ImageIO.createImageInputStream(new File("res/pokemon_images/" + name + "_front_shiny.gif"))) {
				ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
				reader.setInput(stream, false);
				front = new BufferedImage[reader.getNumImages(true)];
				for(int i = 0; i < front.length; i++) {
					front[i] = reader.read(i);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try(ImageInputStream stream = ImageIO.createImageInputStream(new File("res/pokemon_images/" + name + "_back_shiny.gif"))) {
				ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
				reader.setInput(stream, false);
				back = new BufferedImage[reader.getNumImages(true)];
				for(int i = 0; i < back.length; i++) {
					back[i] = reader.read(i);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			isShiny = false;
			try(ImageInputStream stream = ImageIO.createImageInputStream(new File("res/pokemon_images/" + name + "_front_default.gif"))) {
				ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
				reader.setInput(stream, false);
				front = new BufferedImage[reader.getNumImages(true)];
				for(int i = 0; i < front.length; i++) {
					front[i] = reader.read(i);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try(ImageInputStream stream = ImageIO.createImageInputStream(new File("res/pokemon_images/" + name + "_back_default.gif"))) {
				ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
				reader.setInput(stream, false);
				back = new BufferedImage[reader.getNumImages(true)];
				for(int i = 0; i < back.length; i++) {
					back[i] = reader.read(i);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setStats();
		hp = this.hlt;
	}
	
	public void setStats() {
		hlt =  (((2 * bHlt + ivHlt + (evHlt/4)) *lvl) / 100) + lvl + 10;
		atk = (int) (((((2 * bAtk + ivAtk + (evAtk/4)) * lvl)/100) + 5)*getNatureModifier("attack"));
		spcAtk = (int) (((((2 * bSpcAtk + ivSpcAtk + (evSpcAtk/4)) * lvl)/100) + 5)*getNatureModifier("spAtk"));
		spcDef = (int) (((((2 * bSpcDef + ivSpcDef + (evSpcDef/4)) * lvl)/100) + 5)*getNatureModifier("spDef"));
		def = (int) (((((2 * bDef + ivDef + (evDef/4)) * lvl)/100) + 5)*getNatureModifier("defense"));
		spd = (int) (((((2 * bSpd + ivSpd + (evSpd/4)) * lvl)/100) + 5)*getNatureModifier("speed"));	
	}
	public double getNatureModifier(String stat) {
	    if (nature == null) return 1.0;

	    switch (nature.toLowerCase()) {
	        case "lonely":   if(stat.equals("attack")) return 1.1; if(stat.equals("defense")) return 0.9; break;
	        case "brave":    if(stat.equals("attack")) return 1.1; if(stat.equals("speed")) return 0.9; break;
	        case "adamant":  if(stat.equals("attack")) return 1.1; if(stat.equals("spAtk")) return 0.9; break;
	        case "naughty":  if(stat.equals("attack")) return 1.1; if(stat.equals("spDef")) return 0.9; break;
	        case "bold":     if(stat.equals("defense")) return 1.1; if(stat.equals("attack")) return 0.9; break;
	        case "relaxed":  if(stat.equals("defense")) return 1.1; if(stat.equals("speed")) return 0.9; break;
	        case "impish":   if(stat.equals("defense")) return 1.1; if(stat.equals("spAtk")) return 0.9; break;
	        case "lax":      if(stat.equals("defense")) return 1.1; if(stat.equals("spDef")) return 0.9; break;
	        case "timid":    if(stat.equals("speed")) return 1.1; if(stat.equals("attack")) return 0.9; break;
	        case "hasty":    if(stat.equals("speed")) return 1.1; if(stat.equals("defense")) return 0.9; break;
	        case "jolly":    if(stat.equals("speed")) return 1.1; if(stat.equals("spAtk")) return 0.9; break;
	        case "naive":    if(stat.equals("speed")) return 1.1; if(stat.equals("spDef")) return 0.9; break;
	        case "modest":   if(stat.equals("spAtk")) return 1.1; if(stat.equals("attack")) return 0.9; break;
	        case "mild":     if(stat.equals("spAtk")) return 1.1; if(stat.equals("defense")) return 0.9; break;
	        case "quiet":    if(stat.equals("spAtk")) return 1.1; if(stat.equals("speed")) return 0.9; break;
	        case "rash":     if(stat.equals("spAtk")) return 1.1; if(stat.equals("spDef")) return 0.9; break;
	        case "calm":     if(stat.equals("spDef")) return 1.1; if(stat.equals("attack")) return 0.9; break;
	        case "gentle":   if(stat.equals("spDef")) return 1.1; if(stat.equals("defense")) return 0.9; break;
	        case "sassy":    if(stat.equals("spDef")) return 1.1; if(stat.equals("speed")) return 0.9; break;
	        case "careful":  if(stat.equals("spDef")) return 1.1; if(stat.equals("spAtk")) return 0.9; break;
	        default: return 1.0; 
	    }
	    return 1.0;
	}

	
	public void addXP(int xpNum) {
		xp += xpNum;
	}
	
	public double addXP(Pokemon defeated, boolean isWildBattle) {
		/* exp calculation formulas:
		 * Base EXP = (a * t * b * e * L) / (7 * s)
		 * a = base exp of defeated Pokemon
		 * t = trainer battle bonus (1.5x if in a trainer battle, 1.0 if wild pokemon)
		 * b = evolution bonus, 1.2x if evolved
		 * e = exp-share factor / lucky egg bonus (Way to introduce new variable to quicken exp gain)
		 * L = level of defeated pokemon
		 * s = number of pokemon that gained exp (for exp share)
		 * 
		 * Modified for level scaling: 
		 * Scaled EXP = Base EXP * ( ( ( (2 * d) + 10 ) / ( y + d + 10) ) ^2) * .5
		 * d = level of defeated pokemon (same as L)
		 * y = level of your pokemon
		 */
				
		double a = defeated.baseXp;
		
		double t = 1;
		if(!isWildBattle) {
			t = 1.5;
		}
		
		//consider changing this based of number of evolution
		double b = 1;
		if(!evolutionName.equals("")) {
			b = 1.2;
		}
		
		double e = 1; //exp share and lucky egg dont exist right now.
		
		double L = defeated.lvl;
		
		double s = 1; // exp share doesn't exist yet.
		
		double baseXp = (a * t * b * e * L) / (7 * s);
		
		double d = L;
		
		double y = this.lvl;
		
		double scaledXp = baseXp * Math.pow( ( ((2 * d) + 10) / (y + d + 10)) , 2) * .5;
		
		this.xp += scaledXp;
		
		//Check if leveled up:
		levelUp(this.xp);
		
		return scaledXp;
	}
	
	public void levelUp(double exp) {		
		
		double xpNeeded = calcXP(this.lvl + 1, growthRate);

		if (this.xp > xpNeeded) {
			this.lvl ++;
			checkEvolution();
			System.out.println(this.name + " leved up!"); // replace this with a well designed interface later.
		}
		
	}
	
	public static double calcXP(int n, String growthRate) {
		/* exp-level formulas (desribes exp need for level n):
		 * Fast: 4(n^3)/5
		 * Medium Fast: n^3
		 * Medium Slow: 6/5(n^3) - 15(n^2) + 100n - 140
		 * Slow: 5(n^3)/4
		 * Erratic: 
		 * 0 < n <= 50: ((n^3)(100 - n)) / 50
		 * 50 < n <= 68: ((n^3)(150 - n)) / 100
		 * 68 < n <= 98: ((n^3)((1911 - 10n) / 3)) / 500
		 * 98 < n <= 100: ((n^3)(160-n)) / 100
		 * Fluctuating:
		 * 0 < n <= 15: ((n^3)(((n+1)/3) + 24) / 50
		 * 15 < n <= 36: ((n^3)(n+14)) / 50
		 * 36 < n <= 100: ((n^3)(n+64)) / 50
		 */
		
		double xpNeeded = 0;
		
		switch(growthRate) {
		
		case("Fast"): {
			xpNeeded = .8 * Math.pow(n, 3);
			break;
		}
		
		case("Medium Fast"): {
			xpNeeded = Math.pow(n, 3);
			break;
		}
		
		case("Medium Slow"): {
			xpNeeded = (1.2 * Math.pow(n, 3)) - (15 * Math.pow(n, 2)) + (100 * n) - 140;
			break;
		}
		
		case("Slow"): {
			xpNeeded = 1.25 * Math.pow(n, 3);
			break;
		}
		
		case("Erratic"): {
			if(n <= 50) {
				xpNeeded = .02 * Math.pow(n, 3) * (100 - n);
			}
			else if(n <= 68) {
				xpNeeded = .01 * Math.pow(n, 3) * (150 - n);
			}
			else if(n <= 98) {
				xpNeeded = .002 * Math.pow(n, 3) * (1911 - (10 * n)) * (1.0/3);
			}
			else {
				xpNeeded = .01 * Math.pow(n, 3) * (160 - n);
			}
			break;
		}
		//fluctuating
		default: {
			if(n <= 15) {
				xpNeeded = .02 * Math.pow(n, 3) * (((n + 1)/3) + 24);
			}
			else if(n <= 36) {
				xpNeeded = .02 * Math.pow(n, 3) * (n + 14);
			}
			else {
				xpNeeded = .02 * Math.pow(n, 3) * (n + 64);
			}
			break;
		}
		
		}
		return xpNeeded;
	}
	
	public void checkEvolution() {
		if(this.lvl > this.evolutionLevel) {
			evolve();
		}
		//also check other evolution methods.
	}
	
	public void evolve() {
		
	}
	public static boolean addPokemon(String name, String ability, String abilityDescription, int lvl, boolean isShiny, String spawnLocation, String originalTrainer, 
	String gender, int spawnLevel, int IDNo, double xp, String firstMove, String secondMove, String thirdMove, String fourthMove, GamePanel gp) {
		Pokemon mon = new Pokemon(gp, name, lvl);
		mon.name = name;
		mon.ability = ability;
		mon.abilityDescription = abilityDescription;
		mon.lvl = lvl;
		mon.isShiny = isShiny;
		mon.spawnLocation = spawnLocation;
		mon.originalTrainer = originalTrainer;
		mon.gender = gender;
		mon.spawnLevel = spawnLevel;
		mon.IDNo = IDNo;
		for(String move : mon.moveSet) {
			mon.currentMoves[0] = (move.equals(firstMove)) ? new Move(move) : mon.currentMoves[0];
			mon.currentMoves[1] = (move.equals(secondMove)) ? new Move(move)  : mon.currentMoves[1];
			mon.currentMoves[2] = (move.equals(thirdMove)) ? new Move(move)  : mon.currentMoves[2];
			mon.currentMoves[3] = (move.equals(fourthMove)) ? new Move(move)  : mon.currentMoves[3];
		}
		int pos = 0;
		while(pos<6) {
			if(gp.player.heldPokemon[pos]==null) {
				gp.player.heldPokemon[pos] = mon;
				return true;
			}
			pos++;
		}
		return false;
	}
	public void setMoves() {
		int movesSetSoFar = 0;
		for(int i = 0; i < levelsLearnedAt.size(); i++) {
			if(levelsLearnedAt.get(i)<=lvl) {
				currentMoves[movesSetSoFar%4] = new Move(levelUpMoves.get(i));
				movesSetSoFar++;
			}
		}
	}
}
package pokemon;

import java.util.Scanner;
import java.io.File;
public class Move {
	public String type, name, target, description, ailment, attackType, ailmentTarget, statusTarget;
	public int power, currentPP, pp, priority, criticalHitRatio, leastHits, mostHits;
	public double accuracy, ailmentChance, statusChance, drain, recoil, healing, flinchChance;
	public int atkC = 0, spcAtkC = 0, defC = 0, spcDefC = 0, spdC = 0;
	public Move(String name) {
		createMove(name);
	}
	public void createMove(String name) {
		name = name.replace(" ", "-");
		try {
			Scanner fileScanner = new Scanner(new File("res/move_data/"+name+".txt"));
			String str = fileScanner.nextLine();
			this.name = formatName(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.power = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.accuracy = Integer.parseInt(str.substring(str.indexOf(":")+2))/100.0;
			str = fileScanner.nextLine();
			this.type = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.attackType = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.target = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.pp = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.priority = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.description = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.ailment = str.substring(str.indexOf(":")+2);
			str = fileScanner.nextLine();
			this.ailmentChance = Integer.parseInt(str.substring(str.indexOf(":")+2))/100.0;
			str = fileScanner.nextLine();
			String[] arr = str.substring(str.indexOf(":")+2).split("\\|");
			if(!arr[0].equals("none")) {
				String[] statNames = new String[arr.length];
				String[] statNums = new String[arr.length];
				for(int i = 0; i < arr.length; i++) {
					statNames[i] = arr[i].substring(0, arr[i].indexOf(":")).trim();
					statNums[i] = arr[i].substring(arr[i].indexOf(":")+1);
				}
				for(int i = 0; i < arr.length; i++) {
					switch(statNames[i]) {
						case "attack": 
							atkC = Integer.parseInt(statNums[i].trim());
							break;
						case "special-attack": 
							spcAtkC = Integer.parseInt(statNums[i].trim());
							break;
						case "defense": 
							defC = Integer.parseInt(statNums[i].trim());
							break;
						case "special-defense": 
							spcDefC = Integer.parseInt(statNums[i].trim());
							break;
						case "speed": 
							spdC = Integer.parseInt(statNums[i].trim());
							break;
					}
				}
			}
			str = fileScanner.nextLine();
			this.statusChance = Integer.parseInt(str.substring(str.indexOf(":")+2))/100.0;
			str = fileScanner.nextLine();
			this.criticalHitRatio = Integer.parseInt(str.substring(str.indexOf(":")+2));
			str = fileScanner.nextLine();
			this.drain = Integer.parseInt(str.substring(str.indexOf(":")+2))/100.0;
			str = fileScanner.nextLine();
			this.recoil = Integer.parseInt(str.substring(str.indexOf(":")+2))/100.0;
			str = fileScanner.nextLine();
			this.healing = Integer.parseInt(str.substring(str.indexOf(":")+2))/100.0;
			str = fileScanner.nextLine();
			this.leastHits = Integer.parseInt(str.substring(str.indexOf(":")+2, str.indexOf("-")));
			this.mostHits = Integer.parseInt(str.substring(str.indexOf("-")+1));
			str = fileScanner.nextLine();
			this.flinchChance = Integer.parseInt(str.substring(str.indexOf(":")+2))/100.0;
			fileScanner.close();
			
			this.currentPP = this.pp;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		 String formattedMoveName = name.toLowerCase().replace(' ', '-');
		 statusTarget = switch (formattedMoveName) {
	         case "acid-armor", "agility", "amnesia", "autotomize", "barrier", "belly-drum", "bulk-up",
	         "calm-mind", "charge", "coil", "cosmic-power", "cotton-guard", "defense-curl",
	         "double-team", "dragon-dance", "focus-energy", "growth", "harden", "hone-claws",
	         "howl", "iron-defense", "meditate", "minimize", "nasty-plot", "quiver-dance",
	         "rock-polish", "sharpen", "shell-smash", "shift-gear", "stockpile", "swords-dance",
	         "tail-glow", "withdraw", "work-up" -> "user";
	         case "captivate", "charm", "fake-tears", "feather-dance", "flash", "flatter",
	         "metal-sound", "sand-attack", "scary-face", "screech", "smokescreen", "tickle" -> "single opponent";
	         case "cotton-spore", "leer", "string-shot", "sweet-scent", "tail-whip" -> "all opponents";
	         default -> "Not a stat-changing move";
		 };
		 ailmentTarget = switch (formattedMoveName) {
         case "attract", "confuse-ray", "glare", "grass-whistle", "hypnosis", "lovely-kiss",
              "poison-powder", "sing", "sleep-powder", "spore", "stun-spore", "supersonic",
              "swagger", "sweet-kiss", "thunder-wave", "toxic", "will-o-wisp" -> "single opponent";
         case "dark-void", "poison-gas" -> "all opponents";
         case "teeter-dance" -> "entire field";
         default -> "Not an ailment-inducing move";
     };
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
}
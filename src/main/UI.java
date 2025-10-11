package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import entity.Entity;
import item.Item;
import map.PokemonMap;
import map.SpecialBlock;
import pokemon.Move;
import pokemon.Pokemon;

public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	Font maruMonica;
	Font frlgFont;
	Font b2w2Font;
	
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	
	public String currentDialogue = "";
	public int charIndex;
	public String fullText = "";
	long lastCharTime = 0;
	int charDelay = 10; 
	public boolean isDone = false;
	
	public int slotCol = 0;
	public int slotRow = 0;

	public int dIdx = 0;

	public int menuNum = -1;
	
	public Pokemon starter;
	
	public int menuOption = 1;
	public int switchPokemon = -1;
	public int selectedPokemon = 0;
	public int summaryPageNum = 0;
	
	public int selectedMove = -1;
	public int lockedMove;
	public int bagPageNum = 1;
	public int entryPageNum = 0;
	int messagesRevealed = 1;
	public boolean moveSelected = false;
	public boolean saved = false;
	boolean entered = false;
	public String input = "";
	public ArrayList<String> dialogueTexts;
	public int dialogueX, dialogueY, dialogueWidth, dialogueHeight;
	public BufferedImage dialogueFrame;
	public boolean dialogueSpace = false;
	public int state = 0;
	public int textNum = 0;
	public int prevBagPageNum = 1;
	public Item displayedItem = null;
	public long switchTime = System.currentTimeMillis();
	
	BufferedImage frame;


	public static BufferedImage[] textBoxes = new BufferedImage[20];
	static {
		try {
			textBoxes[0] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/blackTextBox.png"));
			textBoxes[1] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/blueScrollTextBox.png"));
			textBoxes[2] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/blueTextBox.png"));
			textBoxes[3] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/brickTextBox.png"));
			textBoxes[4] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/diamondTextBox.png"));
			textBoxes[5] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/dottedTextBox.png"));
			textBoxes[6] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/flowerTextBox.png"));
			textBoxes[7] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/gildedTextBox.png"));
			textBoxes[8] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/goldTextBox.png"));
			textBoxes[9] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/greenTextBox.png"));
			textBoxes[10] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/halloweenTextBox.png"));
			textBoxes[11] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/landscapeTextBox.png"));
			textBoxes[12] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/orangeTextBox.png"));
			textBoxes[13] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/pinkScrollTextBox.png"));
			textBoxes[14] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/pinkTextBox.png"));
			textBoxes[15] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/purpleTextBox.png"));
			textBoxes[16] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/snowTextBox.png"));
			textBoxes[17] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/frozenTextBox.png"));
			textBoxes[18] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/tropicalTextBox.png"));
			textBoxes[19] = ImageIO.read(SpecialBlock.class.getResourceAsStream("/interfaces/wardenTextBox.png"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public UI(GamePanel gp) {
		this.gp = gp;

		InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
		try {
			maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		is = getClass().getResourceAsStream("/font/pokemon-frlg.ttf");
		try {
			frlgFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		is = getClass().getResourceAsStream("/font/b2w2.ttf");
		try {
			b2w2Font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		dialogueTexts = new ArrayList<String>();
	}

	public void showMessage(String text) {
		message = text;
		messageOn = true;
	}

	public void draw(Graphics2D g2) {
		this.g2 = g2;

		g2.setFont(maruMonica);
		g2.setColor(Color.white);
		state = gp.gameState;
		if(state != gp.dialogueState){
			gp.previousState = gp.gameState;
		}
		//System.out.println(dialogueSpace ? "space" : "");
		
		if(state == gp.playState) {
			displayAreaIcon();
		}
		if(state == gp.pauseState) {
			drawPauseScreen();
		}
		if(state == gp.menuState) {
			
			if(menuNum == 1) drawMenu();
			
			else if(menuNum == 2) {
				//0: shows all six, the others only show one.
				//1: displays general information & trainer memo
				//2: displays stats, exp info, and abilities
				//3: displays moves.
				
				if(summaryPageNum == 0) drawParty();
				
				else if(summaryPageNum == 1) pokemonSummaryPageOne();
				
				else if(summaryPageNum == 2) pokemonSummaryPageTwo();
				
				else if(summaryPageNum == 3) pokemonSummaryPageThree();
				
				else if(summaryPageNum == 4) pokemonSummaryPageFour();
				
				else if(summaryPageNum == 5) pokemonSummaryPageFive();
				
				else if(summaryPageNum == 6) displaySubPartyScreen();
			}
			
			else if(menuNum == 3) {
				drawSelectionScreen();
			}
			
			else if(menuNum == 4) {
				if(bagPageNum == 0) displayItem();
				else if(bagPageNum == 1) drawItemScreen();	
				else if(bagPageNum == 2) drawMedicineScreen();
				else if(bagPageNum == 3) drawMachinesScreen();
				else if(bagPageNum == 4) drawBerriesScreen();
				else if(bagPageNum == 5) drawKeyItemsScreen();
			}
			
			else if(menuNum == 5) {
				drawSaveScreen();
			}
			
			else if(menuNum == -1) {
				if(entryPageNum == 0) drawGameSelectionScreen();
				else if(entryPageNum==1) drawEnterNameScreen();
			}
		}
		if(gp.gameState == gp.dialogueState) {
			//drawDialogueScreen(dialogueTexts.get(0), dialogueX, dialogueY, dialogueWidth, dialogueHeight, dialogueFrame);
			if(drawDialogueScreen()) { 
				gp.gameState = gp.previousState;
				//System.out.println("here");
				//if(dialogueTexts.size()==0 ) gp.gameState = gp.previousState;
			}
		}
	}

	public void drawMenu() {
	
		BufferedImage selected = null, unselected = null, menuBag = null, menuBall = null, menuCard = null, menuDex = null, menuOptions = null, menuSave = null, menuCancel = null;
		try {
			selected = ImageIO.read(getClass().getResourceAsStream("/interfaces/sMenuBar.png"));
			unselected = ImageIO.read(getClass().getResourceAsStream("/interfaces/uMenuBar.png"));
			menuBag = ImageIO.read(getClass().getResourceAsStream("/interfaces/menuBag.png"));
			menuBall = ImageIO.read(getClass().getResourceAsStream("/interfaces/menuBall.png"));
			menuCard = ImageIO.read(getClass().getResourceAsStream("/interfaces/menuCard.png"));
			menuDex = ImageIO.read(getClass().getResourceAsStream("/interfaces/menuDex.png"));
			menuOptions = ImageIO.read(getClass().getResourceAsStream("/interfaces/menuOptions.png"));
			menuSave = ImageIO.read(getClass().getResourceAsStream("/interfaces/menuSave.png"));
			menuCancel = ImageIO.read(getClass().getResourceAsStream("/interfaces/partyCancel.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		setOpacity(.4f);
		g2.setColor(Color.black);
		g2.fillRect(450, 0, 336, 576);
		setOpacity(1f);
		
		for(int i = 0; i < 7; i++) {
			
			BufferedImage img = switch(i) {
				case 0 -> menuBall;
				case 1 -> menuDex;
				case 2 -> menuBag;
				case 3 -> menuCard;
				case 4 -> menuSave;
				case 5 -> menuOptions;
				case 6 -> menuCancel;
				default -> throw new IllegalArgumentException("Unexpected value: " + i);
			};
			
			String text = switch(i) {
				case 0 -> "POKéMON";
				case 1 -> "POKéDEX";
				case 2 -> "BAG";
				case 3 -> "TRAINERCARD";
				case 4 -> "SAVE";
				case 5 -> "OPTIONS";
				case 6 -> "CANCEL";
				default -> throw new IllegalArgumentException("Unexpected value: " + i);
			};
			
			if(i == menuOption) {
				g2.drawImage(selected, 480, 8 + 80 * i, 250, 78, null);
				g2.drawImage(img, 488, 24 + 80 * i + (int) (Math.sin(gp.animationInt / 10 % 6) * 4), 40, 50, null);
			} else {
				g2.drawImage(unselected, 480, 8 + 80 * i, 250, 78, null);
				g2.drawImage(img, 488, 24 + 80 * i, 40, 50, null);
			}
			
			Color primary = new Color(255, 255, 255);
			Color outline = new Color(123, 123, 123);
			
			drawOutlinedString("", 0, 0, 3, 48, primary, outline);
			FontMetrics fm = g2.getFontMetrics();
			
			drawOutlinedString(text, 620 - fm.stringWidth(text) / 2, 64 + 80 * i, 3, 48, primary, outline);
		}
		
		if(gp.keyH.downPressed) {
			menuOption ++;
			if(menuOption == 7) menuOption = 0;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed) {
			menuOption --;
			if(menuOption == -1) menuOption = 6;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.rPressed) {
			menuOption = 0;
			gp.gameState = gp.playState;
			gp.keyH.rPressed = false;
		}
		
		if(gp.keyH.spacePressed) {
			switch(menuOption) {
				case 0: menuNum = 2; selectedPokemon = 1; break;
				case 1:	System.out.println("no dex yet"); break; 
				case 2: menuNum = 4; menuOption = 0; break;
				case 3: System.out.println("no trainercard yet"); break;
				case 4: menuNum = 5; break; 
				case 5: System.out.println("no options yet"); break;
				case 6: gp.gameState = gp.playState;
			}
			gp.keyH.spacePressed = false;
			menuOption = 0;
		}
	
	}
	
	private void drawParty() {
		
		BufferedImage bottomBar = null;
		try {
			bottomBar = ImageIO.read(getClass().getResourceAsStream("/battle/partyTextBar.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		drawPartyArchetype();
		
		g2.drawImage(bottomBar, 0, 486, 768, 90, null);
		drawOutlinedString("Choose a pokemon.", 32, 540, 4, 48, Color.DARK_GRAY, Color.gray);
		drawOutlinedString("Press r to exit.", 576, 540, 4, 36, Color.white, Color.gray);
		
		int currOption = selectedPokemon;
		
		if(gp.keyH.leftPressed || gp.keyH.rightPressed) {		
			if(selectedPokemon % 2 == 0) selectedPokemon ++;
			else selectedPokemon --;		
			gp.keyH.leftPressed = false;
			gp.keyH.rightPressed = false;
		}
		if (gp.keyH.downPressed) {
			if(selectedPokemon < 4) selectedPokemon += 2;
			else selectedPokemon -= 4;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed) {
			if(selectedPokemon < 2) selectedPokemon += 4;
			else selectedPokemon -= 2;
			gp.keyH.upPressed = false;
		}
		
		if(gp.player.heldPokemon[selectedPokemon] == null) {
			selectedPokemon = currOption;
		}
		
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			if(switchPokemon == -1) {
				summaryPageNum = 6;
			} else {
				Pokemon temp = gp.player.heldPokemon[switchPokemon];
				gp.player.heldPokemon[switchPokemon] = gp.player.heldPokemon[selectedPokemon];
				gp.player.heldPokemon[selectedPokemon] = temp;
				switchPokemon = -1;
			}
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rPressed = false;
			menuNum = 1;
		}
	}
	
	public void displaySubPartyScreen() {
		
		BufferedImage selected = null, unselected = null;
		try {
			selected = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedOption.png"));
			unselected = ImageIO.read(getClass().getResourceAsStream("/interfaces/unselectedOption.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		drawPartyArchetype();
		
		setOpacity(.2f);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 768, 576);
		setOpacity(1f);
		
		for(int i = 0; i < 4; i++) {
			if(i != menuOption) {
				g2.drawImage(unselected, 600, 240 + 48 * i, 140, 48, null);
			} else {
				g2.drawImage(selected, 600, 240 + 48 * i, 140, 48, null);
			}
		}
		
		Color primary = new Color(255, 255, 255);
		Color outline = new Color(123, 123, 123);
		
		drawOutlinedString("", 0, 0, 2, 36, primary, outline);
		FontMetrics fm = g2.getFontMetrics();
		
		String text = "Summary";
		drawOutlinedString(text, 666 - fm.stringWidth(text) / 2, 272, 2, 36, primary, outline);
		text = "Switch";
		drawOutlinedString(text, 666 - fm.stringWidth(text) / 2, 320, 2, 36, primary, outline);
		text = "Item";
		drawOutlinedString(text, 666 - fm.stringWidth(text) / 2, 368, 2, 36, primary, outline);
		text = "Cancel";
		drawOutlinedString(text, 666 - fm.stringWidth(text) / 2, 416, 2, 36, primary, outline);
		
		primary = new Color(90, 82, 82);
		outline = new Color(165, 165, 173);
		
		g2.drawImage(textBoxes[7], 16, 432, 736, 128, null);
		drawOutlinedString("What would you like to do?", 48, 492, 3, 48, primary, outline);
		//drawOutlinedString("to do?", 48, 528, 3, 48, primary, outline);
		
		if(gp.keyH.downPressed) {
			if(menuOption == 3) menuOption = 0;
			else menuOption ++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed) {
			if(menuOption == 0) menuOption = 3;
			else menuOption --;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.rPressed) {
			summaryPageNum = 0; 
			gp.keyH.rPressed = false;
		}
		if(gp.keyH.spacePressed) {
			switch(menuOption) {
				case 0: summaryPageNum = 1; break;
				case 1: summaryPageNum = 0; switchPokemon = selectedPokemon; break;
				case 2: summaryPageNum = 0; menuNum = 4; break;
				case 3: summaryPageNum = 0; break;
			}
			gp.keyH.spacePressed = false;
		}
	}
	
	public void drawPartyArchetype() {
		BufferedImage unselected = null, selected = null, unselectedFirst = null, selectedFirst = null, empty = null, bg = null, male = null, female = null;
		try {
			unselected = ImageIO.read(getClass().getResourceAsStream("/battle/unselectedSlot.png"));
			selected = ImageIO.read(getClass().getResourceAsStream("/battle/selectedSlot.png"));
			unselectedFirst = ImageIO.read(getClass().getResourceAsStream("/battle/unselectedFirstSlot.png"));
			selectedFirst = ImageIO.read(getClass().getResourceAsStream("/battle/selectedFirstSlot.png"));
			empty = ImageIO.read(getClass().getResourceAsStream("/battle/emptySlot.png"));
			bg = ImageIO.read(getClass().getResourceAsStream("/battle/bg.png"));
			male = ImageIO.read(getClass().getResourceAsStream("/battle/male.png"));
			female = ImageIO.read(getClass().getResourceAsStream("/battle/female.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(bg, 0, 0, 768, 576, null);

		int y = 29;
		int x = 3;
		
		for(int i = 0; i < 6; i++) {
			Pokemon mon = gp.player.heldPokemon[i];
			
			if(mon != null) {
				
				setOpacity(.7f);
				
				if(i ==  1) { 
					if(i == selectedPokemon || i == switchPokemon) {
						g2.drawImage(selectedFirst, x, y, 378, 138, null);
					} else {
						g2.drawImage(unselectedFirst, x, y, 378, 138, null);
					}
				} {
					if(i == selectedPokemon || i == switchPokemon) {
						g2.drawImage(selected, x, y, 378, 138, null);
					} else {
						g2.drawImage(unselected, x, y, 378, 138, null);
					}
				}
				
				setOpacity(1f);
				
				BufferedImage img = mon.icon;
				int width = img.getWidth() * 3;
				int height = img.getHeight() * 3;
				g2.drawImage(img, x + 64 - width / 2, y + 52 - height / 2, width, height, null);
				
				drawOutlinedString("Lv. " + mon.lvl, x + 24, y + 124, 3, 38, Color.white, Color.gray);
				drawOutlinedString(mon.hp + "/" + mon.hlt, x + 168, y + 124, 3, 38, Color.white, Color.gray);
				
				drawHPBar(x + 135, y + 72, mon);
				
				drawOutlinedString(mon.name, x + 120, y + 63, 4, 48, Color.white, Color.gray); 
				
				if (mon.gender.equals("male")) {
					g2.drawImage(male, x + 312, y + 33, 18, 30, null);
				} else if (mon.gender.equals("female")){
					g2.drawImage(female, x + 312, y + 33, 18, 30, null);
				}
				
			} else {
				
				setOpacity(.7f);

				g2.drawImage(empty, x, y, 378, 138, null);
				
				setOpacity(1f);
				
			}
			
			if(x == 387) {
				x = 3;
				y += 120;
			} else {
				x += 384;
				y += 24;
			}
			
		}
	}
	
	public void pokemonSummaryPageOne() {
		
		Pokemon mon = gp.player.heldPokemon[selectedPokemon];
		
		BufferedImage bg = null, primaryType = null, secondaryType = null, gender = null, right = null;
		
		try{
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/sp1.png"));
			primaryType = ImageIO.read(getClass().getResourceAsStream("/interfaces/" + mon.primaryType + ".png"));
			if(mon.secondaryType != null) secondaryType = ImageIO.read(getClass().getResourceAsStream("/interfaces/" + mon.secondaryType + ".png"));
			if(mon.gender.equals("male")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/male.png"));
			} else if(mon.gender.equals("female")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/female.png"));
			}
			right = ImageIO.read(getClass().getResourceAsStream("/interfaces/rArrow.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(bg, 0, 0, 768, 576, null);
		
		int width = mon.front[0].getWidth() * 4;
		int height = mon.front[0].getHeight() * 4;
		
		g2.drawImage(mon.front[0], 620 - width / 2, 320 - height / 2, width, height, null);
		
		Color primary = new Color(90, 82, 82);
		Color outline = new Color(165, 165, 173);
		Color ePrimary = new Color(0, 0, 214);
		Color eOutline = new Color(15, 148, 255);
		
		drawOutlinedString(mon.pokedexNum + "", 244, 136, 3, 48, primary, outline);
		drawOutlinedString(mon.name, 244, 184, 3, 48, primary, outline);
		
		g2.drawImage(primaryType, 244, 202,	96, 36, null);
		g2.drawImage(secondaryType, 348, 202, 96, 36, null);
		
		drawOutlinedString(mon.originalTrainer, 244, 280, 3, 48, ePrimary, eOutline);
		drawOutlinedString(mon.IDNo + "", 244, 328, 3, 48, primary, outline);
		
		drawOutlinedString((int) (mon.xp) + "", 260, 428, 3, 48, primary, outline);
		drawOutlinedString((int) (Pokemon.calcXP(mon.lvl + 1, mon.growthRate) - mon.xp) + "", 228, 520, 3, 48, primary, outline);
		
		drawOutlinedString(mon.name, 540, 108, 3, 48, primary, outline);
		drawOutlinedString(mon.lvl + "", 520, 164, 3, 48, primary, outline);
		g2.drawImage(gender, 728, 80, 18, 30, null);
		
		drawXPBar(207, 540, 198, 9, (int) (mon.xp - Pokemon.calcXP(mon.lvl, mon.growthRate)), (int) mon.xp);
		
		g2.drawImage(right, (int) (688 + Math.sin(gp.animationInt / 20 % 6) * 6), 34, 18, 18, null);
		
		if(gp.keyH.rightPressed) {
			summaryPageNum ++;
			gp.keyH.rightPressed = false;
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rPressed = false;
			summaryPageNum = 0;
		}
	} 
	
	public void pokemonSummaryPageTwo() {
			
		Pokemon mon = gp.player.heldPokemon[selectedPokemon];
		
		BufferedImage bg = null, gender = null, right = null, left = null;
		
		try{
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/sp2.png"));
			if(mon.gender.equals("male")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/male.png"));
			} else if(mon.gender.equals("female")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/female.png"));
			}
			right = ImageIO.read(getClass().getResourceAsStream("/interfaces/rArrow.png"));
			left = ImageIO.read(getClass().getResourceAsStream("/interfaces/lArrow.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.setFont(b2w2Font.deriveFont(48));
		FontMetrics fm = g2.getFontMetrics();
		
		g2.drawImage(bg, 0, 0, 768, 576, null);
		
		int width = mon.front[0].getWidth() * 4;
		int height = mon.front[0].getHeight() * 4;
		
		g2.drawImage(mon.front[0], 620 - width / 2, 320 - height / 2, width, height, null);
		
		Color primary = new Color(90, 82, 82);
		Color outline = new Color(165, 165, 173);
		Color ePrimary = new Color(0, 0, 214);
		Color eOutline = new Color(15, 148, 255);
		
		drawOutlinedString(mon.nature.substring(0, 1) + mon.nature.substring(1), 48, 160, 3, 48, ePrimary, eOutline);
		//drawOutlinedString("nature", 48 + fm.stringWidth(mon.nature), 160, 3, 48, primary, outline);
		//drawOutlinedString(244, 184, 3, 48, primary, outline);
		
		drawOutlinedString(mon.name, 540, 108, 3, 48, primary, outline);
		drawOutlinedString(mon.lvl + "", 520, 164, 3, 48, primary, outline);
		g2.drawImage(gender, 728, 80, 18, 30, null);
		
		g2.drawImage(right, (int) (688 + Math.sin(gp.animationInt / 20 % 6) * 6), 34, 18, 18, null);
		g2.drawImage(left, (int) (512 - Math.sin(gp.animationInt / 20 % 6) * 6), 34, 18, 18, null);
		
		if(gp.keyH.rightPressed) {
			summaryPageNum ++;
			gp.keyH.rightPressed = false;
		}
		if(gp.keyH.leftPressed) {
			summaryPageNum --;
			gp.keyH.leftPressed = false;
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rPressed = false;
			summaryPageNum = 0;
		}
	} 
	
	public void pokemonSummaryPageThree() {
		
		Pokemon mon = gp.player.heldPokemon[selectedPokemon];
		
		BufferedImage bg = null, gender = null, right = null, left = null;
		
		try{
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/sp3.png"));
			if(mon.gender.equals("male")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/male.png"));
			} else if(mon.gender.equals("female")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/female.png"));
			}
			right = ImageIO.read(getClass().getResourceAsStream("/interfaces/rArrow.png"));
			left = ImageIO.read(getClass().getResourceAsStream("/interfaces/lArrow.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Color primary = new Color(90, 82, 82);
		Color outline = new Color(165, 165, 173);
		
		drawOutlinedString("", 0, 0, 3, 48, primary, outline); // alligns fm properly.
		FontMetrics fm = g2.getFontMetrics();
		
		g2.drawImage(bg, 0, 0, 768, 576, null);
		
		int width = mon.front[0].getWidth() * 4;
		int height = mon.front[0].getHeight() * 4;
		
		g2.drawImage(mon.front[0], 620 - width / 2, 320 - height / 2, width, height, null);
		
		int x = 152;
		drawOutlinedString(mon.hp + "/" + mon.hlt, 268 - fm.stringWidth(mon.hp + "/" + mon.hlt) / 2, 152, 3, 48, primary, outline);
		drawOutlinedString(mon.evHlt + "", 372 - fm.stringWidth(mon.evHlt + "") / 2, 152, 3, 48, primary, outline);
		drawOutlinedString(mon.ivHlt + "", 440 - fm.stringWidth(mon.ivHlt + "") / 2, 152, 3, 48, primary, outline);
		x = 224;
		
		drawOutlinedString(mon.atk + "", 268 - fm.stringWidth(mon.atk + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.evAtk + "", 372 - fm.stringWidth(mon.evAtk + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.ivAtk + "", 440 - fm.stringWidth(mon.ivAtk + "") / 2, x, 3, 48, primary, outline);
		x += 48;
		
		drawOutlinedString(mon.def + "", 268 - fm.stringWidth(mon.def + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.evDef + "", 372 - fm.stringWidth(mon.evDef + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.ivDef + "", 440 - fm.stringWidth(mon.ivDef + "") / 2, x, 3, 48, primary, outline);
		x += 48;
		
		drawOutlinedString(mon.spcAtk + "", 268 - fm.stringWidth(mon.spcAtk + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.evSpcAtk + "", 372 - fm.stringWidth(mon.evSpcAtk + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.ivSpcAtk + "", 440 - fm.stringWidth(mon.ivSpcAtk + "") / 2, x, 3, 48, primary, outline);
		x += 48;
		
		drawOutlinedString(mon.spcDef + "", 268 - fm.stringWidth(mon.spcDef + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.evSpcDef + "", 372 - fm.stringWidth(mon.evSpcDef + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.ivSpcDef + "", 440 - fm.stringWidth(mon.ivSpcDef + "") / 2, x, 3, 48, primary, outline);
		x += 48;
		
		drawOutlinedString(mon.spd + "", 268 - fm.stringWidth(mon.spd + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.evSpd + "", 372 - fm.stringWidth(mon.evSpd + "") / 2, x, 3, 48, primary, outline);
		drawOutlinedString(mon.ivSpd + "", 440 - fm.stringWidth(mon.ivSpd + "") / 2, x, 3, 48, primary, outline);
		x = 8;
		int y =  520;
		
		drawOutlinedString(mon.ability.substring(0, 1).toUpperCase() + mon.ability.substring(1), 280, 472, 3, 48, primary, outline);

		String text = mon.abilityDescription;
		while (text.length() > 0) {
	        int i = text.length();
	        if (fm.stringWidth(text) > 396) {
	            for (i = text.length(); i > 0; i--) {
	                String sub = text.substring(0, i);
	                if (fm.stringWidth(sub) <= 396) {
	                    int spaceIndex = sub.lastIndexOf(' ');
	                    if (spaceIndex != -1) {
	                        i = spaceIndex;
	                    }
	                    break;
	                }
	            }
	        }
	        drawOutlinedString(text.substring(0, i), x, y, 3, 48, primary, outline);
	        y += 48;
	        if (i < text.length()) {
	            text = text.substring(i).trim();
	        } else {
	            break;
	        }
	    }
		
		drawOutlinedString(mon.name, 540, 108, 3, 48, primary, outline);
		drawOutlinedString(mon.lvl + "", 520, 164, 3, 48, primary, outline);
		g2.drawImage(gender, 728, 80, 18, 30, null);
		
		drawHPBar(2, 165, mon);
		
		g2.drawImage(right, (int) (688 + Math.sin(gp.animationInt / 20 % 6) * 6), 34, 18, 18, null);
		g2.drawImage(left, (int) (492 - Math.sin(gp.animationInt / 20 % 6) * 6), 34, 18, 18, null);
		
		if(gp.keyH.rightPressed) {
			summaryPageNum ++;
			gp.keyH.rightPressed = false;
		}
		if(gp.keyH.leftPressed) {
			summaryPageNum --;
			gp.keyH.leftPressed = false;
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rPressed = false;
			summaryPageNum = 0;
		}
	} 
	
	public void pokemonSummaryPageFour() {
		
		Pokemon mon = gp.player.heldPokemon[selectedPokemon];
		
		BufferedImage bg = null, gender = null, left = null;
		
		try{
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/sp4.png"));
			if(mon.gender.equals("male")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/male.png"));
			} else if(mon.gender.equals("female")) {
				gender = ImageIO.read(getClass().getResourceAsStream("/battle/female.png"));
			}
			left = ImageIO.read(getClass().getResourceAsStream("/interfaces/lArrow.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.setFont(b2w2Font.deriveFont(48));
		FontMetrics fm = g2.getFontMetrics();
	
		g2.drawImage(bg, 0, 0, 768, 576, null);
		
		int width = mon.front[0].getWidth() * 4;
		int height = mon.front[0].getHeight() * 4;
		
		g2.drawImage(mon.front[0], 620 - width / 2, 320 - height / 2, width, height, null);
		
		Color primary = new Color(255, 255, 255);
		Color outline = new Color(123, 123, 123);
		
		for(int i = 0; i < 4; i++) {
			Move m = mon.currentMoves[i];
			
			if(m != null) {
				
				BufferedImage moveType = null;
				try {
					moveType = ImageIO.read(getClass().getResourceAsStream("/interfaces/" + m.type + ".png"));
				} catch(IOException e) {
					e.printStackTrace();
				}
				
				g2.drawImage(moveType, 56, 132 + 96 * i, 96, 36, null);
				
				drawOutlinedString(m.name, 160, 160 + 96 * i, 3, 48, primary, outline);
				drawOutlinedString("PP", 188, 204 + 96 * i, 3, 48, primary, outline);
				drawOutlinedString(m.currentPP + "/" + m.pp, 264, 204 + 96 * i, 3, 48, primary, outline);
				
				
			}
			
		}
		
		drawOutlinedString("Press space to change.", 92, 548, 3, 40, primary, outline);
		
		primary = new Color(90, 82, 82);
		outline = new Color(165, 165, 173);
		
		drawOutlinedString(mon.name, 540, 108, 3, 48, primary, outline);
		drawOutlinedString(mon.lvl + "", 520, 164, 3, 48, primary, outline);
		g2.drawImage(gender, 728, 80, 18, 30, null);
		
		g2.drawImage(left, (int) (512 - Math.sin(gp.animationInt / 20 % 6) * 6), 34, 18, 18, null);
		
		if(gp.keyH.spacePressed) {
			menuOption = 0;
			summaryPageNum ++;
			gp.keyH.spacePressed = false;
		}
		if(gp.keyH.leftPressed) {
			summaryPageNum --;
			gp.keyH.leftPressed = false;
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rPressed = false;
			summaryPageNum = 0;
		}
	} 
	
	public void pokemonSummaryPageFive() {
		//not even a summary Page really, but a subclass of 4
		Pokemon mon = gp.player.heldPokemon[selectedPokemon];
		
		BufferedImage bg = null, selSumBar = null, hovSumBar = null, normSumBar = null, notBrightBox = null, brightBox = null, primaryType = null, secondaryType = null;
		
		try{
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/sp5.png"));
			selSumBar = ImageIO.read(getClass().getResourceAsStream("/interfaces/selSumBar.png"));
			hovSumBar = ImageIO.read(getClass().getResourceAsStream("/interfaces/hovSumBar.png"));
			normSumBar = ImageIO.read(getClass().getResourceAsStream("/interfaces/normSumBar.png"));
			notBrightBox = ImageIO.read(getClass().getResourceAsStream("/interfaces/notBrightBox.png"));
			brightBox = ImageIO.read(getClass().getResourceAsStream("/interfaces/brightBox.png"));
			primaryType = ImageIO.read(getClass().getResourceAsStream("/interfaces/" + mon.primaryType + ".png"));
			if(mon.secondaryType != null) secondaryType = ImageIO.read(getClass().getResourceAsStream("/interfaces/" + mon.secondaryType + ".png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(bg, 0, 0, 768, 576, null);
		
		Color primary = new Color(255, 255, 255);
		Color outline = new Color(123, 123, 123);
		
		for(int i = 0; i < 4; i++) {
			Move m = mon.currentMoves[i];
			
			if(i == selectedMove) {
				g2.drawImage(selSumBar, 352, 141 + 102 * i, 418, 102, null);
			}
			else if(i == menuOption) {
				g2.drawImage(hovSumBar, 352, 141 + 102 * i, 418, 102, null);
			} else {
				g2.drawImage(normSumBar, 352, 141 + 102 * i, 418, 102, null);
			}
			
			
			if(m != null) {
				
				BufferedImage moveType = null;
				try {
					moveType = ImageIO.read(getClass().getResourceAsStream("/interfaces/" + m.type + ".png"));
				} catch(IOException e) {
					e.printStackTrace();
				}
				
				g2.drawImage(moveType, 388, 154 + 102 * i, 96, 36, null);
				
				drawOutlinedString(m.name, 492, 184 + 102 * i, 3, 48, primary, outline);
				drawOutlinedString("PP", 520, 228 + 102 * i, 3, 48, primary, outline);
				drawOutlinedString(m.currentPP + "/" + m.pp, 612, 228 + 102 * i, 3, 48, primary, outline);
				
				for(int j = 0; j < 4; j++) {
					int x = 412, y = 204;
					if(j % 2 == 1) x = 439;
					if(j > 1) y = 219;
					if(j == i) {
						g2.drawImage(brightBox, x, y + 102 * i, 21, 9, null);
					} else {
						g2.drawImage(notBrightBox, x, y + 102 * i, 21, 9, null);
					}
				}
				
			}
		}
		
		primary = new Color(90, 82, 82);
		outline = new Color(165, 165, 173);
		
		g2.drawImage(mon.icon, -12, 60, mon.icon.getWidth() * 4, mon.icon.getHeight() * 4, null); // - pixel value actually works lel
		
		g2.drawImage(primaryType, 141, 117, 96, 36, null);
		g2.drawImage(secondaryType, 245, 117, 96, 36, null);
		
		Move curr = mon.currentMoves[menuOption];
		
		BufferedImage moveCategory = null;
		try {
			if(curr.attackType.equals("physical")) {
				moveCategory = ImageIO.read(getClass().getResourceAsStream("/interfaces/physical.png"));
			} else if(curr.attackType.equals("special")) {
				moveCategory = ImageIO.read(getClass().getResourceAsStream("/interfaces/special.png"));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if(curr.attackType.equals("status")) {
			drawOutlinedString("-", 240, 220, 3, 48, primary, outline);
		} else {
			g2.drawImage(moveCategory, 240, 184, 84, 42, null);
		}
		
		String text = curr.power == 0 ? "-" : curr.power + "";
		drawOutlinedString(text, 240, 268, 3, 48, primary, outline);
		text = curr.accuracy == 0 ? "100%" : (int)(curr.accuracy * 100) + "%";
		drawOutlinedString(text, 240, 316, 3, 48, primary, outline);
		
		FontMetrics fm = g2.getFontMetrics();
		text = curr.description;
		
		int y = 368;
		while (text.length() > 0) {
	        int i = text.length();
	        if (fm.stringWidth(text) > 340) {
	            for (i = text.length(); i > 0; i--) {
	                String sub = text.substring(0, i);
	                if (fm.stringWidth(sub) <= 340) {
	                    int spaceIndex = sub.lastIndexOf(' ');
	                    if (spaceIndex != -1) {
	                        i = spaceIndex;
	                    }
	                    break;
	                }
	            }
	        }
	        drawOutlinedString(text.substring(0, i), 16, y, 3, 48, primary, outline);
	        y += 48;
	        if (i < text.length()) {
	            text = text.substring(i).trim();
	        } else {
	            break;
	        }
	    }
		
		if(gp.keyH.rPressed) {
			summaryPageNum = 4;
			gp.keyH.rPressed = false;
		}
		if(gp.keyH.downPressed) {
			if(menuOption == 3) menuOption = 0;
			else menuOption ++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed) {
			if(menuOption == 0) menuOption = 3;
			else menuOption --;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.spacePressed) {
			if(selectedMove == -1) {
				selectedMove = menuOption;
			} else {
				Move temp = mon.currentMoves[selectedMove];
				mon.currentMoves[selectedMove] = mon.currentMoves[menuOption];
				mon.currentMoves[menuOption] = temp;
				selectedMove = -1;
			}
			gp.keyH.spacePressed = false;
		}
	}
	
	public void setOpacity(float x) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, x));
	}
	
	public void drawOutlinedString(String text, int x, int y) {
	    g2.setColor(Color.black);
	    for (int dx = -2; dx <= 2; dx++) {
	        for (int dy = -2; dy <= 2; dy++) {
	            if (dx != 0 || dy != 0) {
	                g2.drawString(text, x + dx, y + dy);
	            }
	        }
	    }
	    g2.setColor(Color.white);
	    g2.drawString(text, x, y);
	}
	
	public void drawOutlinedString(String text, int x, int y, int outlineSize, int fontSize, Color primaryColor, Color outlineColor) {
		g2.setFont(gp.ui.b2w2Font.deriveFont(Font.PLAIN, fontSize));
		g2.setColor(outlineColor);
	    for (int dx = 0; dx <= outlineSize; dx++) {
	        for (int dy = 0; dy <= outlineSize; dy++) {
	            if (dx != 0 || dy != 0) {
	                g2.drawString(text, x + dx, y + dy);
	            }
	        }
	    }
	    g2.setColor(primaryColor);
	    g2.drawString(text, x, y);
	}
	
	public void drawHPBar(int x, int y, Pokemon mon) {
		//used in party interface
		
		BufferedImage hp = null;
		try {
			hp = ImageIO.read(getClass().getResourceAsStream("/battle/partyHPBar.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(hp, x, y, 192, 18, null);
		
		double hpRatio = 1.0 * mon.hp / mon.hlt;
		
		if(hpRatio > .5) {
			g2.setColor(new Color(0, 251, 73));
		} else if(hpRatio > .2) {
			g2.setColor(new Color(243, 178, 0));
		} else {
			g2.setColor(new Color(251, 48, 65));
		}
		
		g2.fillRect(x + 45, y + 6, (int) (hpRatio * 144), 6);
	}
	

	private void drawXPBar(int x, int y, int prevWidth, int prevHeight, int levelXP, int totLevelXP) {
		g2.setColor(new Color(80, 160, 232));
		
		double xpRatio = (1.0 * levelXP/totLevelXP);
		
		g2.fillRect(x, y, (int)(xpRatio * prevWidth), prevHeight);
		
	}

	public void drawSelectionScreen() {
		Pokemon mon = starter;
		
		BufferedImage box = null, selected = null, unselected = null;
		try {
			box = ImageIO.read(getClass().getResourceAsStream("/interfaces/starterbg.png"));
			selected = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedOption.png"));
			unselected = ImageIO.read(getClass().getResourceAsStream("/interfaces/unselectedOption.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		if(menuOption > 1) {
			menuOption = 0;
		}
		
		/*if(gp.csManager.isDone.get(gp.csManager.starterSelection)) {
			if(gp.keyH.spacePressed && dialogueTexts.size() > 0) {
				if(menuOption == 0) {
					gp.player.heldPokemon[0] = mon;
					for(int i  = 0; i < gp.specialBlocks.size(); i++) {
						if(gp.specialBlocks.get(i).mon != null) {
							gp.specialBlocks.remove(i);
							i--;
						}
					}
				}
				menuNum = 0;
				gp.gameState = gp.playState;
				dialogueTexts.clear();
				gp.keyH.spacePressed = false;
			} else {
				gp.keyH.spacePressed = false;
			}
		
			if(gp.keyH.downPressed || gp.keyH.upPressed) {
				menuOption = menuOption == 0 ? 1 : 0;
				gp.keyH.downPressed = false;
				gp.keyH.upPressed = false;
			} 
			
			g2.drawImage(box, 254, 136, 260, 260, null);
			
			int width = mon.front[0].getWidth() * 4;
			int height = mon.front[0].getHeight() * 4;
			
			g2.drawImage(mon.front[0], 384 - width / 2, 280 - height / 2, width, height, null);
			
			g2.drawImage(textBoxes[7], 69, 460, 650, 100, null);
			
			if(dialogueTexts.size() < 1 || !dialogueTexts.get(0).equals("So you would like to choose the " + mon.primaryType + " pokemon " + mon.name + "?")) {
				dialogueTexts.clear();
				charIndex = 0;
				dialogueTexts = new ArrayList<> (Arrays.asList(new String [] {"So you would like to choose the " + mon.primaryType + " pokemon " + mon.name + "?"}));
				gp.keyH.spacePressed = false;
			}
			
			dealWithText(72, 452);
			
			BufferedImage img = menuOption == 0 ? selected : unselected;
			g2.drawImage(img, 540, 360, 140, 48, null);
			img = menuOption == 0 ? unselected : selected;
			g2.drawImage(img, 540, 408, 140, 48, null);
			
			drawOutlinedString("", 0, 0, 2, 36, Color.white, Color.black);
			FontMetrics fm = g2.getFontMetrics();
			
			String text = "Yes";
			drawOutlinedString(text, 608 - fm.stringWidth(text) / 2, 392, 2, 36, Color.white, Color.black);
			text = "No";
			drawOutlinedString(text, 608 - fm.stringWidth(text) / 2, 440, 2, 36, Color.white, Color.black);
		} else {
			
			g2.drawImage(textBoxes[7], 69, 460, 650, 100, null);
			
			if(gp.keyH.spacePressed && dialogueTexts.size() > 0) {
				gp.keyH.spacePressed = false;
				menuNum = 0;
				gp.gameState = gp.playState;
				dialogueTexts.clear();
				gp.keyH.spacePressed = false;
			}

			if(dialogueTexts.size() < 1 || !dialogueTexts.get(0).equals("A pokeball. I wonder whats in it.")) {
				dialogueTexts.clear();
				charIndex = 0;
				dialogueTexts = new ArrayList<> (Arrays.asList(new String [] {"A pokeball. I wonder whats in it."}));
				gp.keyH.spacePressed = false;
			}
			
			dealWithText(72, 452);
			
		}*/

	}
	
	public void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		g2.setColor(new Color(0,0,0));

		String text = "PAUSED";

		int x = getXForCenteredText(text);
		int y = gp.screenHeight/2;

		g2.drawString(text, x, y);

	}
	//don't call this directly, use displayText
	/*
	private boolean drawDialogueScreen(String text, int x, int y, int width, int height, BufferedImage frame) {
		drawSubWindow(x, y, width, height, frame);
	    g2.setColor(Color.black);
	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
		drawText(text, x + 20, y + 40, width);
		if(dialogueSpace) {
			dialogueSpace = false;
			isDone = true;
			return true;
		}
		
		return false;
	}*/
	
	
	public void displayText(ArrayList<String> text) {
		frame = textBoxes[7];
		displayTexts(text, 69, 466, 650, 100, gp.gameState, frame);
	}
	
	public void displayText(String text) {
		frame = textBoxes[7];
		displayText(text, 69, 466, 650, 100, gp.gameState, frame);
	}
	
	public void displayText(String text, int x, int y, int width, int height, int gameState, BufferedImage frame) {
		gp.previousState = gp.gameState;
		dialogueTexts.clear();
		dialogueTexts.add(text);
		dialogueX = x;
		dialogueY = y;
		dialogueWidth = width;
		dialogueHeight = height;
		dialogueFrame = frame;
		gp.previousState = gameState;
		gp.gameState = gp.dialogueState;
	}
	
	public void displayTexts(ArrayList<String> texts, int x, int y, int width, int height, int gameState, BufferedImage frame) {
		gp.previousState = gp.gameState;
		dialogueTexts.clear();
		dialogueTexts.addAll(texts);
		dialogueX = x;
		dialogueY = y;
		dialogueWidth = width;
		dialogueHeight = height;
		dialogueFrame = frame;
		gp.previousState = gameState;
		gp.gameState = gp.dialogueState;
	}


	public boolean drawDialogueScreen() {
		int x = gp.tileSize;
		int y = 412;
		int width = gp.screenWidth - (gp.tileSize*2);
		int height = gp.tileSize*2;

		drawSubWindow(x, y, width, height, frame);
		
		return dealWithText(x, y);
	}
	
	public boolean dealWithText(int x, int y) {
		if (dialogueTexts == null || dIdx >= dialogueTexts.size() || dialogueTexts.get(dIdx) == null) return true;

		if (fullText == null || !fullText.equals(dialogueTexts.get(dIdx))) {
			// New line of dialogue
			fullText = dialogueTexts.get(dIdx);
			currentDialogue = "";
			charIndex = 0;
			lastCharTime = System.currentTimeMillis();
		}
		
		if(charIndex < fullText.length()) {
			if(System.currentTimeMillis()-lastCharTime >= charDelay) {
				currentDialogue += fullText.charAt(charIndex);
				charIndex ++;
				lastCharTime = System.currentTimeMillis();
			}
		}
	    g2.setColor(Color.black);
	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
	    
		drawText(currentDialogue, x + 24, y + 48, 768);
		
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			if(charIndex >= fullText.length()) {
				dIdx ++;
				if(dIdx >= dialogueTexts.size() || dialogueTexts.get(dIdx) == null) {
					dIdx = 0;
					fullText = "";
					currentDialogue = "";
					//isDone = true;
					return true;
				}
				
				fullText = "";
			}
			else {
				currentDialogue = fullText;
				charIndex = fullText.length();
			}
		}
		return false;
	}
	

	public void drawText(int x, int y) {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
		x+= gp.tileSize;
		y+= gp.tileSize;

		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y+=40;
		}
	}
	public void drawText(String text, int x, int y, int width) {
	    FontMetrics metrics = g2.getFontMetrics();
	    while (text.length() > 0) {
	        int i = text.length();
	        if (metrics.stringWidth(text) > width) {
	            for (i = text.length(); i > 0; i--) {
	                String sub = text.substring(0, i);
	                if (metrics.stringWidth(sub) <= width) {
	                    int spaceIndex = sub.lastIndexOf(' ');
	                    if (spaceIndex != -1) {
	                        i = spaceIndex;
	                    }
	                    break;
	                }
	            }
	        }
	        g2.drawString(text.substring(0, i), x, y);
	        y += metrics.getHeight();
	        if (i < text.length()) {
	            text = text.substring(i).trim();
	        } else {
	            break;
	        }
	    }
	}

	public void drawSubWindow(int x, int y, int width, int height, BufferedImage frame) {
		g2.drawImage(frame, x, y, width, height, null);

	}
	public void drawSubWindow(int x, int y, int width, int height) {

		Color c = new Color(0, 0, 0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);

	}

	public int getXForCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	public void drawItemScreen() {
		BufferedImage bg = null, rightArrows = null, selectedItem = null, nonselectedItem = null;
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/itemsScreen.png"));
			rightArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/rightArrows.png"));
			selectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedItem.png"));
			nonselectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/nonselectedItem.png"));
		}
		catch	(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(bg, 0, 0, 768, 576, null);
		g2.setColor(Color.white);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 50F));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString("ITEMS", 384 - fm.stringWidth("ITEMS")/2, 550);
		g2.drawImage(rightArrows, 550 + ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		int inventorySize = gp.player.inventory.size();
		int totalSlots = inventorySize + 1;
		int topItemIndex = 0;
		if (totalSlots > 7) {
		    if (menuOption >= 7) {
		        topItemIndex = menuOption - (7 - 1);
		    }
		}
		int numItemsToDraw = Math.min(totalSlots - topItemIndex, 7);
		for(int i = 0; i < numItemsToDraw; i++) {
			int currentItemIndex = topItemIndex + i;
			int slotY = 35 + 62*i;
			if(currentItemIndex < inventorySize) {
				if(menuOption==currentItemIndex) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				String itemName = gp.player.inventory.get(currentItemIndex).name.toUpperCase();
				if(gp.player.inventory.get(currentItemIndex).pocket.equals("machines")) itemName = gp.player.inventory.get(currentItemIndex).moveName.toUpperCase();
				g2.drawString(itemName, 430, slotY + 45);
		        int quantity = gp.player.inventory.get(currentItemIndex).quantity;
		        String quantityText = "x" + quantity;
		        int quantityWidth = fm.stringWidth(quantityText);
		        g2.drawString(quantityText, 700 - quantityWidth, slotY + 45);
			}
			else{
				if(menuOption==gp.player.inventory.size()) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				g2.drawString("CLOSE BAG", 430, slotY + 45);
			}
		}
		if(gp.keyH.downPressed && menuOption<gp.player.inventory.size()) {
			menuOption++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed && menuOption>0) {
			menuOption--;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.rightPressed) {
			menuOption = 0;
			bagPageNum++;
			gp.keyH.rightPressed = false;
		}
		if(gp.keyH.spacePressed) {
			if(menuOption == gp.player.inventory.size()) {
				menuOption = 0;
				menuNum = 1;
				gp.gameState = gp.playState;
			}
			else {
				prevBagPageNum = bagPageNum;
				bagPageNum = 0;
				displayedItem = gp.player.inventory.get(menuOption);
			}
			gp.keyH.spacePressed = false;
		}
		if(gp.keyH.rPressed) {
			menuOption = 0;
			menuNum = 1;
			gp.keyH.rPressed = false;
		}
	}
	public void drawMedicineScreen() {
		BufferedImage bg = null, rightArrows = null, leftArrows = null, selectedItem = null, nonselectedItem = null;
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/medicinesScreen.png"));
			leftArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/leftArrows.png"));
			rightArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/rightArrows.png"));
			selectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedItem.png"));
			nonselectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/nonselectedItem.png"));
		}
		catch	(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(bg, 0, 0, 768, 576, null);
		g2.setColor(Color.white);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 50F));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString("MEDICINE", 384 - fm.stringWidth("MEDICINE")/2, 550);
		g2.drawImage(rightArrows, 550 + ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		g2.drawImage(leftArrows, 126 - ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		int inventorySize = gp.player.medicine.size();
		int totalSlots = inventorySize + 1;
		int topItemIndex = 0;
		if (totalSlots > 7) {
		    if (menuOption >= 7) {
		        topItemIndex = menuOption - (7 - 1);
		    }
		}
		int numItemsToDraw = Math.min(totalSlots - topItemIndex, 7);
		for(int i = 0; i < numItemsToDraw; i++) {
			int currentItemIndex = topItemIndex + i;
			int slotY = 35 + 62*i;
			if(currentItemIndex < inventorySize) {
				if(menuOption==currentItemIndex) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				String itemName = gp.player.medicine.get(currentItemIndex).name.toUpperCase();
				g2.drawString(itemName, 430, slotY + 45);
		        int quantity = gp.player.medicine.get(currentItemIndex).quantity;
		        String quantityText = "x" + quantity;
		        int quantityWidth = fm.stringWidth(quantityText);
		        g2.drawString(quantityText, 700 - quantityWidth, slotY + 45);
			}
			else{
				if(menuOption==gp.player.medicine.size()) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				g2.drawString("CLOSE BAG", 430, slotY + 45);
			}
		}
		if(gp.keyH.downPressed && menuOption<gp.player.medicine.size()) {
			menuOption++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed && menuOption>0) {
			menuOption--;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.leftPressed) {
			menuOption = 0;
			bagPageNum--;
			gp.keyH.leftPressed = false;
		}
		if(gp.keyH.rightPressed) {
			menuOption = 0;
			bagPageNum++;
			gp.keyH.rightPressed = false;
		}
		if(gp.keyH.spacePressed) {
			if(menuOption == gp.player.medicine.size()) {
				menuOption = 0;
				menuNum = 1;
				gp.gameState = gp.playState;
			}
			else {
				prevBagPageNum = bagPageNum;
				bagPageNum = 0;
				displayedItem = gp.player.medicine.get(menuOption);
			}
			gp.keyH.spacePressed = false;
		}
		if(gp.keyH.rPressed) {
			menuOption = 0;
			menuNum = 1;
			gp.keyH.rPressed = false;
		}
	}
	public void drawMachinesScreen() {
		BufferedImage bg = null, rightArrows = null, leftArrows = null, selectedItem = null, nonselectedItem = null;
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/machinesScreen.png"));
			leftArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/leftArrows.png"));
			rightArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/rightArrows.png"));
			selectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedItem.png"));
			nonselectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/nonselectedItem.png"));
		}
		catch	(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(bg, 0, 0, 768, 576, null);
		g2.setColor(Color.white);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 50F));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString("TMs AND HMs", 384 - fm.stringWidth("TMs AND HMs")/2, 550);
		g2.drawImage(rightArrows, 550 + ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		g2.drawImage(leftArrows, 126 - ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		int inventorySize = gp.player.machines.size();
		int totalSlots = inventorySize + 1;
		int topItemIndex = 0;
		if (totalSlots > 7) {
		    if (menuOption >= 7) {
		        topItemIndex = menuOption - (7 - 1);
		    }
		}
		int numItemsToDraw = Math.min(totalSlots - topItemIndex, 7);
		for(int i = 0; i < numItemsToDraw; i++) {
			int currentItemIndex = topItemIndex + i;
			int slotY = 35 + 62*i;
			if(currentItemIndex < inventorySize) {
				if(menuOption==currentItemIndex) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				String itemName = gp.player.machines.get(currentItemIndex).moveName.toUpperCase();
				g2.drawString(itemName, 430, slotY + 45);
		        int quantity = gp.player.machines.get(currentItemIndex).quantity;
		        String quantityText = "x" + quantity;
		        int quantityWidth = fm.stringWidth(quantityText);
		        g2.drawString(quantityText, 700 - quantityWidth, slotY + 45);
			}
			else{
				if(menuOption==gp.player.machines.size()) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				g2.drawString("CLOSE BAG", 430, slotY + 45);
			}
		}
		if(gp.keyH.downPressed && menuOption<gp.player.machines.size()) {
			menuOption++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed && menuOption>0) {
			menuOption--;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.leftPressed) {
			menuOption = 0;
			bagPageNum--;
			gp.keyH.leftPressed = false;
		}
		if(gp.keyH.rightPressed) {
			menuOption = 0;
			bagPageNum++;
			gp.keyH.rightPressed = false;
		}
		if(gp.keyH.spacePressed) {
			if(menuOption == gp.player.machines.size()) {
				menuOption = 0;
				menuNum = 1;
				gp.gameState = gp.playState;
			}
			else {
				prevBagPageNum = bagPageNum;
				bagPageNum = 0;
				displayedItem = gp.player.machines.get(menuOption);
			}
			gp.keyH.spacePressed = false;
		}
		if(gp.keyH.rPressed) {
			menuOption = 0;
			menuNum = 1;
			gp.keyH.rPressed = false;
		}
	}
	public void drawBerriesScreen() {
		BufferedImage bg = null, rightArrows = null, leftArrows = null, selectedItem = null, nonselectedItem = null;
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/berriesScreen.png"));
			leftArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/leftArrows.png"));
			rightArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/rightArrows.png"));
			selectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedItem.png"));
			nonselectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/nonselectedItem.png"));
		}
		catch	(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(bg, 0, 0, 768, 576, null);
		g2.setColor(Color.white);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 50F));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString("BERRIES", 384 - fm.stringWidth("BERRIES")/2, 550);
		g2.drawImage(rightArrows, 550 + ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		g2.drawImage(leftArrows, 126 - ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		int inventorySize = gp.player.berries.size();
		int totalSlots = inventorySize + 1;
		int topItemIndex = 0;
		if (totalSlots > 7) {
		    if (menuOption >= 7) {
		        topItemIndex = menuOption - (7 - 1);
		    }
		}
		int numItemsToDraw = Math.min(totalSlots - topItemIndex, 7);
		for(int i = 0; i < numItemsToDraw; i++) {
			int currentItemIndex = topItemIndex + i;
			int slotY = 35 + 62*i;
			if(currentItemIndex < inventorySize) {
				if(menuOption==currentItemIndex) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				String itemName = gp.player.berries.get(currentItemIndex).name.toUpperCase();
				g2.drawString(itemName, 430, slotY + 45);
		        int quantity = gp.player.berries.get(currentItemIndex).quantity;
		        String quantityText = "x" + quantity;
		        int quantityWidth = fm.stringWidth(quantityText);
		        g2.drawString(quantityText, 700 - quantityWidth, slotY + 45);
			}
			else{
				if(menuOption==gp.player.berries.size()) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				g2.drawString("CLOSE BAG", 430, slotY + 45);
			}
		}
		if(gp.keyH.downPressed && menuOption<gp.player.berries.size()) {
			menuOption++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed && menuOption>0) {
			menuOption--;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.leftPressed) {
			menuOption = 0;
			bagPageNum--;
			gp.keyH.leftPressed = false;
		}
		if(gp.keyH.rightPressed) {
			menuOption = 0;
			bagPageNum++;
			gp.keyH.rightPressed = false;
		}
		if(gp.keyH.spacePressed) {
			if(menuOption == gp.player.berries.size()) {
				menuOption = 0;
				menuNum = 1;
				gp.gameState = gp.playState;
			}
			else {
				prevBagPageNum = bagPageNum;
				bagPageNum = 0;
				displayedItem = gp.player.berries.get(menuOption);
			}
			gp.keyH.spacePressed = false;
		}
		if(gp.keyH.rPressed) {
			menuOption = 0;
			menuNum = 1;
			gp.keyH.rPressed = false;
		}
	}
	public void drawKeyItemsScreen() {
		BufferedImage bg = null, leftArrows = null, selectedItem = null, nonselectedItem = null;
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/keyItemsScreen.png"));
			leftArrows = ImageIO.read(getClass().getResourceAsStream("/interfaces/leftArrows.png"));
			selectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedItem.png"));
			nonselectedItem = ImageIO.read(getClass().getResourceAsStream("/interfaces/nonselectedItem.png"));
		}
		catch	(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(bg, 0, 0, 768, 576, null);
		g2.setColor(Color.white);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 50F));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString("KEY ITEMS", 384 - fm.stringWidth("KEY ITEMS")/2, 550);
		g2.drawImage(leftArrows, 126 - ((gp.animationInt/30)%2)* 20, 500, 70, 70, null);
		int inventorySize = gp.player.keyItems.size();
		int totalSlots = inventorySize + 1;
		int topItemIndex = 0;
		if (totalSlots > 7) {
		    if (menuOption >= 7) {
		        topItemIndex = menuOption - (7 - 1);
		    }
		}
		int numItemsToDraw = Math.min(totalSlots - topItemIndex, 7);
		for(int i = 0; i < numItemsToDraw; i++) {
			int currentItemIndex = topItemIndex + i;
			int slotY = 35 + 62*i;
			if(currentItemIndex < inventorySize) {
				if(menuOption==currentItemIndex) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				String itemName = gp.player.keyItems.get(currentItemIndex).name.toUpperCase();
				g2.drawString(itemName, 430, slotY + 45);
		        int quantity = gp.player.keyItems.get(currentItemIndex).quantity;
		        String quantityText = "x" + quantity;
		        int quantityWidth = fm.stringWidth(quantityText);
		        g2.drawString(quantityText, 700 - quantityWidth, slotY + 45);
			}
			else{
				if(menuOption==gp.player.keyItems.size()) g2.drawImage(selectedItem, 410, slotY, 310, 60, null);
				else g2.drawImage(nonselectedItem, 410, slotY, 310, 60, null);
				g2.drawString("CLOSE BAG", 430, slotY + 45);
			}
		}
		if(gp.keyH.downPressed && menuOption<gp.player.keyItems.size()) {
			menuOption++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed && menuOption>0) {
			menuOption--;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.leftPressed) {
			menuOption = 0;
			bagPageNum--;
			gp.keyH.leftPressed = false;
		}
		if(gp.keyH.spacePressed) {
			if(menuOption == gp.player.keyItems.size()) {
				menuOption = 0;
				menuNum = 1;
				gp.gameState = gp.playState;
			}
			else {
				prevBagPageNum = bagPageNum;
				bagPageNum = 0;
				displayedItem = gp.player.keyItems.get(menuOption);
			}
			gp.keyH.spacePressed = false;
		}
		if(gp.keyH.rPressed) {
			menuOption = 0;
			menuNum = 1;
			gp.keyH.rPressed = false;
		}
	}
	public void displayItem() {
		BufferedImage bg = null;
		try {
			bg = ImageIO.read(getClass().getResourceAsStream("/interfaces/itemInfoScreen.png"));
		}
		catch	(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(bg, 0, 0, 768, 576, null);
		g2.drawImage(displayedItem.image, 344, 200, 80, 80, null);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 48F));	
		g2.setColor(Color.white);
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(displayedItem.name, 384 - fm.stringWidth(displayedItem.name)/2, 180);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 40F));	
		drawText(displayedItem.description, 45, 330, 678);
		if(gp.keyH.rPressed) {
			menuOption = 0;
			bagPageNum = prevBagPageNum;
			menuNum = 1;
			gp.keyH.rPressed = false;
		}
		if(gp.keyH.spacePressed) {
			gp.player.selectItem(displayedItem);
			menuOption = 0;
			bagPageNum = prevBagPageNum;
			menuNum = 0;
			gp.keyH.spacePressed = false;
		}
	}
	public void drawShadow(String phrase, int x, int y) {
		 g2.setFont(frlgFont.deriveFont(Font.PLAIN, 38F));

		 g2.setColor(new Color(116, 116, 116));
		
		 g2.drawString(phrase, x + 2, y);
		 g2.drawString(phrase, x, y + 2);
		 
		 g2.setColor(new Color(248, 248, 248));
		 
		 g2.drawString(phrase, x, y);
	}
	
	
	public void drawSaveScreen() {
		BufferedImage saveFrame = textBoxes[0], selected = null, nonselected = null, saveInfo = null;
		try {
			selected = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectedItem.png"));
			nonselected = ImageIO.read(getClass().getResourceAsStream("/interfaces/nonselectedItem.png"));
			saveInfo = ImageIO.read(getClass().getResourceAsStream("/interfaces/saveGameInfo.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		g2.drawImage(saveInfo, 10, 10, 420, 200, null);
		g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 35F));
		g2.setColor(Color.white);
		g2.drawString(gp.player.badgesEarned+"", 130, 165);
		g2.drawString(gp.player.pokedexNum+"", 300, 165);
		g2.drawString(gp.player.name+"", 20, 40);
		gp.timePlayed += System.currentTimeMillis() - gp.sessionStartTime;
		gp.sessionStartTime = System.currentTimeMillis();
		for(int i = 0; i < gp.player.heldPokemon.length; i++) {
			if(gp.player.heldPokemon[i]!=null) {
				g2.drawImage(gp.player.heldPokemon[i].icon, i*70, 45, 80, 80, null);
			}
		}
		g2.drawString(getMinutes(gp.timePlayed), 100, 197);
		if(!saved) {
			g2.drawImage(saveFrame, 69, 466, 650, 100, null);
			g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 35F));
			g2.setColor(Color.black);
			g2.drawString("Would you like to save the game?", 100, 510);
			if(menuOption == 0) {
				g2.drawImage(selected, 500, 360, 212, 48, null);
				g2.drawImage(nonselected, 500, 410, 212, 48, null);
			}
			else {
				g2.drawImage(nonselected, 500, 360, 212, 48, null);
				g2.drawImage(selected, 500, 410, 212, 48, null);
			}
			g2.setColor(Color.white);
			g2.drawString("Yes", 520, 395);
			g2.drawString("No", 520, 445);
			if(gp.keyH.downPressed && menuOption == 0) {
				menuOption++;
				gp.keyH.downPressed = false;
			}
			
			if(gp.keyH.upPressed && menuOption == 1) {
				menuOption--;
				gp.keyH.upPressed = false;
			}
			if(gp.keyH.spacePressed) {
				if(menuOption == 0) {
					gp.sManager.save(getAmountofSaves()+1);
					saved = true;
				}
				else {
					menuOption = 0;
					menuNum = 1;
					gp.gameState = gp.playState;
				}
				gp.keyH.spacePressed = false;
			}
		}
		else {
			g2.drawImage(saveFrame, 69, 466, 650, 100, null);
			g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 35F));
			g2.setColor(Color.black);
			g2.drawString(gp.player.name + " saved the game.", 100, 510);
			if(gp.keyH.spacePressed) {
				menuOption = 0;
				menuNum = 1;
				gp.gameState = gp.playState;
				gp.keyH.spacePressed = false;
				saved = false;
			}	
		}
		if(gp.keyH.rPressed) {
			menuOption = 0;
			menuNum = 1;
			gp.keyH.rPressed = false;
			saved = false;
		}
	}
	
	public String getMinutes(Long ms) {
		int minutes =  (int) (ms/60000); 
		int hours = 0;
		if(minutes>=60) {
			hours = minutes/60;
			minutes -= hours*minutes;
		}
		return String.format("%d:%02d", hours, minutes);
	}
	
	private void drawGameSelectionScreen() {
		BufferedImage previousStateFrame = null, newGameFrame = null;
		try {
			previousStateFrame = ImageIO.read(getClass().getResourceAsStream("/interfaces/65x30purpleFrame.png"));
			newGameFrame = ImageIO.read(getClass().getResourceAsStream("/interfaces/65x10purpleFrame.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int numSaves = getAmountofSaves();
		if(gp.keyH.downPressed && menuOption < numSaves+1) {
			menuOption++;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed && menuOption > 1) {
			menuOption--;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.spacePressed) {
			if(menuOption < numSaves+1) {
				gp.sManager.load(menuOption);
				gp.keyH.spacePressed = false;
				menuNum = 1;
				gp.maps.get(2).mapName = gp.player.name+"'s House";
				gp.maps.get(1).mapName = gp.player.name+"'s Room";
				if(gp.currentMap.mapName.equals("null's Room")) gp.currentMap = new PokemonMap(gp.player.name+"'s Room","/maps/starter_house_floor_two.png","/maps/starter_house_floor_two_collision.png", "/interfaces/woodAreaIcon.png", gp);
			}
			else{
				gp.keyH.spacePressed = false;
				entryPageNum = 1;
				menuOption = 0;
			}
		}
		
		g2.setColor(new Color(75, 0, 255, 150));
		g2.fillRect(0, 0, 768, 576);
		g2.setColor(Color.black);
		g2.setFont(frlgFont.deriveFont(Font.PLAIN, 30F));
		for(int i = 1; i <= numSaves; i++) {
			int FrameY = 10 + i*576 - (menuOption*576);
			String[] info = gp.sManager.getPreviewInfo(i);
			g2.drawImage(previousStateFrame, 59, FrameY, 650, 300, null);
			g2.setColor(Color.black);
			g2.drawString("CONTINUE", 100, FrameY + 50);
			g2.setColor(Color.blue);
			g2.drawString("PLAYER: ", 100, FrameY + 120);
			g2.drawString("BADGES: ", 100, FrameY + 190);
			g2.drawString("TIME: ", 100, FrameY + 260);
			g2.drawString(info[0], 250, FrameY + 120);
			g2.drawString(info[1], 250, FrameY + 190);
			g2.drawString(getMinutes(Long.parseLong(info[2])), 250, FrameY + 260);
		}
		g2.drawImage(newGameFrame, 59,  10 + (numSaves+1)*576 - (menuOption*576), 650, 100, null);
		g2.drawString("NEW GAME", 100,  50 + (numSaves+1)*576 - (menuOption*576));
	}
	
	public int getAmountofSaves() {
		String filePathStart = "res/save_states/";
		int i = 1;
		while(true) {
			if(!new File(filePathStart+i+".txt").exists()) return i-1;
			i++;
		}
	}
	
	public void drawEnterNameScreen() {
		BufferedImage inputFrame = null, commandFrame = null, confirmFrame = null, selectArrow = null;
		try {
			commandFrame = ImageIO.read(getClass().getResourceAsStream("/interfaces/65x30purpleFrame.png"));
			inputFrame = ImageIO.read(getClass().getResourceAsStream("/interfaces/65x10purpleFrame.png"));
			confirmFrame = ImageIO.read(getClass().getResourceAsStream("/interfaces/20x10purpleFrame.png"));
			selectArrow = ImageIO.read(getClass().getResourceAsStream("/interfaces/selectArrow.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(gp.keyH.spacePressed&&messagesRevealed<=5) {
			messagesRevealed++;
			gp.keyH.spacePressed = false;
		}
		g2.setColor(Color.CYAN);
		g2.fillRect(0, 0, 768, 576);
		g2.drawImage(commandFrame, 59, 10, 650, 350, null);
		g2.setFont(frlgFont.deriveFont(Font.PLAIN, 27F));
		g2.setColor(Color.black);
		g2.drawString("Hello there! Welcome to the world of Pokemon!", 79, 50);
		if(messagesRevealed>1) g2.drawString("My name is DARK OAK! People call me the POKEMON PROF.", 79, 105);
		if(messagesRevealed>2) g2.drawString("This world is inhabited by creatures called POKEMON!", 79, 160);
		if(messagesRevealed>3) g2.drawString("For some, POKEMON are pets. Others use them for fights.", 79, 215);
		if(messagesRevealed>4) g2.drawString("Myself... I study POKEMON as a profession.", 79, 270);
		if(messagesRevealed>5) { 
			g2.drawString("First, what is your name?", 79, 325);
			g2.drawImage(inputFrame, 59, 466, 650, 100, null);
			g2.setFont(frlgFont.deriveFont(Font.PLAIN, 30F));
			if(gp.keyH.keyTyped == '\n' && input.length()>0) entered = true;
			if(!entered) {
				g2.drawString(input, 79, 500);
				if(gp.keyH.keyTyped == '\b' && input.length()>0) input = input.substring(0, input.length()-1);
				if((Character.isLetterOrDigit(gp.keyH.keyTyped))&&input.length()<12) input+= gp.keyH.keyTyped;
				if((gp.keyH.keyTyped==' '&&input.length()>0&&input.length()<12)) input += ' ';
				if((gp.animationInt/30)%2==0) g2.drawString("ENTER TO CONTINUE", 470, 550);
				gp.keyH.keyTyped = Character.UNASSIGNED;	
			}
			else {
				g2.drawImage(confirmFrame, 509, 360, 200, 100, null);
				g2.drawImage(selectArrow, 530, 380 + menuOption*40, 40, 40, null);
				g2.drawString("So " + input + " is your name?", 79, 500);
				g2.drawString("So " + input + " is your name?", 79, 500);
				g2.drawString("Yes", 590, 400);
				g2.drawString("No", 590, 440);
				if(gp.keyH.downPressed&&menuOption==0) menuOption++;
				if(gp.keyH.upPressed&&menuOption==1) menuOption--;
				if(gp.keyH.spacePressed) {
					if(menuOption == 0) {
						gp.player.name = input;
						menuNum = 1;
						gp.maps.get(2).mapName = gp.player.name+"'s House";
						gp.maps.get(1).mapName = gp.player.name+"'s Room";
						if(gp.currentMap.mapName.equals("null's Room")) gp.currentMap = new PokemonMap(gp.player.name+"'s Room","/maps/starter_house_floor_two.png","/maps/starter_house_floor_two_collision.png", "/interfaces/woodAreaIcon.png", gp);
						gp.newGame();
					}
					else {
						entered = false;
						input = "";
					}
					gp.keyH.spacePressed = false;
				}
			}
		}
		else g2.drawString(input, 79, 500);
	}
	public void displayAreaIcon() {
		if(System.currentTimeMillis() - switchTime < 1000) {
			g2.drawImage(gp.currentMap.areaIconImage, 10, 10, 393, 81, null); 
			g2.setFont(b2w2Font.deriveFont(Font.PLAIN, 48));
			g2.setColor(Color.BLACK);
			FontMetrics fm = g2.getFontMetrics();
			g2.drawString(gp.currentMap.mapName, 206 - fm.stringWidth(gp.currentMap.mapName)/2, 60);
		}
	}
}
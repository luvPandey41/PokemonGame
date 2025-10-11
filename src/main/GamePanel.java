package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entity.Entity;
import entity.NPC;
import entity.Player;
import pokemon.Pokemon;
import map.PokemonMap;
import map.SpecialBlock;

public class GamePanel extends JPanel implements Runnable{
	final int originalTileSize = 32;
	final int scale = 2;

	public final int tileSize = originalTileSize * scale;
	final int maxScreenRow = 9;
	final int maxScreenCol = 12;
	public final int screenHeight = maxScreenRow * tileSize;
	public final int screenWidth = maxScreenCol * tileSize;

	public final int maxWorldCol = 20;
	public final int maxWorldRow = 20;
	public final int worldHeight = maxWorldCol * tileSize;
	public final int worldWidth = maxWorldRow * tileSize;

	int FPS = 60;

	public KeyHandler keyH = new KeyHandler(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	public CutsceneManager csManager = new CutsceneManager(this);
	public BattleHandler bHandler = new BattleHandler(this);
	public SaveManager sManager = new SaveManager(this);
	Thread gameThread;
	
	
	public Player player = new Player(this, keyH);
	public PokemonMap currentMap = new PokemonMap(player.name+"'s Room","/maps/starter_house_floor_two.png","/maps/starter_house_floor_two_collision.png", "/interfaces/woodAreaIcon.png", this);
	public ArrayList<PokemonMap> maps = new ArrayList<>();
	public ArrayList<SpecialBlock> specialBlocks = new ArrayList<>();
	
	public int gameState;
	public int previousState = -1;
	public int beforePreviousState = -1;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int cutsceneState = 4;
	public final int menuState = 5;
	public final int battleState = 6;
	
	public int animationInt = 0;
	
	public long sessionStartTime;
	public long timePlayed;

	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
		player.getPlayerImage();
		
	}

	@Override
	public void run() {
		double drawInterval = 1000000000/FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;

		while(gameThread != null) {
			update();
			repaint();

			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime /= 1000000;
				if(remainingTime < 0) {
					remainingTime = 0;
				}
				Thread.sleep((long) remainingTime);
				nextDrawTime += drawInterval;
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		int state = gameState;
		if(gameState == dialogueState) {
			state = previousState;
		}
		if (state == battleState) bHandler.drawBattle((Graphics2D)g);
		else if(state != menuState || ui.menuNum == 1 || ui.menuNum == 5) paintNormal(g);
		else ui.draw((Graphics2D)(g));
	}
	
	public void paintNormal(Graphics g) {
		super.paintComponents(g);

		Graphics2D g2 = (Graphics2D)g;
		
		currentMap.draw(g2);
		
		for(SpecialBlock i: specialBlocks) {
			if(i!= null && i.map.equals(currentMap.mapName)) {
				i.draw(g2);
			}
		}
		
		for(NPC i : currentMap.npcList) {
			i.draw(g2);
		}
		player.draw(g2);
		

		for(SpecialBlock i: specialBlocks) {
			if(i!= null && i.map.equals(currentMap.mapName)) {
				i.drawOverlay(g2);
			}
		}
		
		ui.draw(g2);
		
		cChecker.draw(g2);

		//csManager.draw(g2);
		
		g2.dispose();
	}
	
	public void setUpGame() {
		aSetter.setMaps();
		aSetter.setSpecialBlocks();
		gameState = menuState;
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

	}

	public void update() {
		//add gamestate here later
		animationInt++;
		if(animationInt==10000) animationInt = 0;
		if(gameState == playState || gameState == cutsceneState) {
			player.update();

			for(NPC i : currentMap.npcList) {
				i.update();
			}
		}
		
	
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void newGame() {
		aSetter.setItems();
		timePlayed = 0;
		player.heldPokemon[5] = new Pokemon(this, "charizard", 70);
		player.heldPokemon[1] = new Pokemon(this, "wartortle", 70);
		player.heldPokemon[2] = new Pokemon(this, "bulbasaur", 70);
		player.heldPokemon[3] = new Pokemon(this, "pikachu", 70);
		player.heldPokemon[4] = new Pokemon(this, "zubat", 70);
		player.heldPokemon[0] = new Pokemon(this, "mewtwo", 70);	
		player.heldPokemon[0].addXP(30);
		sessionStartTime = System.currentTimeMillis();
	}



}
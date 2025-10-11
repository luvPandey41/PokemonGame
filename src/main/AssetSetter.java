package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import item.Item;
import map.PokemonMap;
import map.SpecialBlock;

public class AssetSetter {
	GamePanel gp;

	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setMaps() {
		gp.maps.add(new PokemonMap("Maplewood Town","/maps/maplewood_town.png","/maps/maplewood_town_collision.png", "/interfaces/woodAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap(gp.player.name+"'s Room","/maps/starter_house_floor_two.png","/maps/starter_house_floor_two_collision.png", "/interfaces/woodAreaIcon.png", gp));
		gp.maps.add(new PokemonMap(gp.player.name+"'s House","/maps/starter_house_floor_one.png","/maps/starter_house_floor_one_collision.png", "/interfaces/woodAreaIcon.png", gp));
		gp.maps.add(new PokemonMap("Pokemon Lab","/maps/pokemon_lab.png","/maps/pokemon_lab_collision.png", "/interfaces/modernAreaIcon.png", 1.2, gp));
		gp.maps.add(new PokemonMap("Route 1", "/maps/route_one.png", "/maps/route_one_collision.png", "/interfaces/greenAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap("Moonveil City", "/maps/moonveil_city.png", "/maps/moonveil_city_collision.png", "/interfaces/woodAreaIcon.png", gp));
		gp.maps.add(new PokemonMap("Moonveil Pokemart","/maps/pokemart.png","/maps/pokemart_collision.png", "/interfaces/modernAreaIcon.png", gp));
		gp.maps.add(new PokemonMap("Moonveil Pokecenter","/maps/pokecenter_floor_one.png","/maps/pokecenter_floor_one_collision.png", "/interfaces/modernAreaIcon.png", gp));
		gp.maps.add(new PokemonMap("Moonveil Pokecenter, Floor 2","/maps/pokecenter_floor_two.png","/maps/pokecenter_floor_two_collision.png", "/interfaces/modernAreaIcon.png", gp));
		gp.maps.add(new PokemonMap("Route 2","/maps/route_two.png","/maps/route_two_collision.png", "/interfaces/greenAreaIcon.png", gp));
		gp.maps.add(new PokemonMap("Sandrift Forest", "/maps/sandrift_forest.png", "/maps/sandrift_forest_collision.png", "/interfaces/bushAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap("Sandrift City", "/maps/sandrift_city.png", "/maps/sandrift_city_collision.png", "/interfaces/whiteAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap("Route 3", "/maps/route_three.png", "/maps/route_three_collision.png", "/interfaces/greenAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap("Slitstone Grotto Entrance", "/maps/slitstone_grotto_f1.png", "/maps/slitstone_grotto_f1_collision.png", "/interfaces/ghostAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap("Slitstone Grotto, Floor 2", "/maps/slitstone_grotto_f2.png", "/maps/slitstone_grotto_f2_collision.png", "/interfaces/ghostAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap("Slitstone Grotto, Floor 3", "/maps/slitstone_grotto_f3.png", "/maps/slitstone_grotto_f3_collision.png", "/interfaces/ghostAreaIcon.png", 2, gp));
		gp.maps.add(new PokemonMap("Slitstone Lake", "/maps/slitstone_grotto_f4.png", "/maps/slitstone_grotto_f4_collision.png", "/interfaces/ghostAreaIcon.png", 2, gp));
		gp.maps.get(4).spawnsheet.addGrassEncounter("rattata", 5, 7, 30);
		gp.maps.get(4).spawnsheet.addGrassEncounter("pidgey", 5, 7, 40);
		gp.maps.get(4).spawnsheet.addGrassEncounter("ledyba", 5, 5, 20);
		gp.maps.get(4).spawnsheet.addGrassEncounter("skitty", 6, 7, 10);
		gp.maps.get(10).spawnsheet.addGrassEncounter("weedle", 5, 8, 100);
	}
	public void setSpecialBlocks() {
		BufferedImage post = null;
		try {
			post = ImageIO.read(getClass().getResourceAsStream("/specialBlocks/post.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpecialBlock.addGrass(30, 347, 30, 55, "Route 1", gp);
		SpecialBlock.addGrass(100, 347, 30, 55, "Route 1", gp);
		SpecialBlock.addGrass(30, 164, 40, 100, "Route 1", gp);
		SpecialBlock.addGrass(83, 220, 50, 45, "Route 1", gp);
		SpecialBlock.addMailbox(87, 80, 0, "Maplewood Town", "Your House", gp);
		SpecialBlock.addMailbox(146, 95, 1, "Maplewood Town", "Rival's House", gp);
		SpecialBlock.addTallGrass(132, 56, 73, 135, "Sandrift Forest", gp);
		SpecialBlock.addTallGrass(28, 58, 64, 226, "Sandrift Forest", gp);
		SpecialBlock.addSelectionBall(52, 27, "Pokemon Lab", "bulbasaur", 5, gp);
		SpecialBlock.addSelectionBall(58, 27, "Pokemon Lab", "squirtle", 5, gp);
		SpecialBlock.addSelectionBall(64, 27, "Pokemon Lab", "charmander", 5, gp);
	}
	public void setItems() {
		Item.addItem("acro bike", 1, gp);
		Item.addItem("pinap berry", 67, gp);
		Item.addItem("ultra ball", 10, gp);
		Item.addItem("tm01", 1, gp);
		Item.addItem("hm01", 1, gp);
		Item.addItem("tm02", 1, gp);
		Item.addItem("tm03", 1, gp);
		Item.addItem("tm04", 1, gp);
		Item.addItem("full heal", 1, gp);
		Item.addItem("great ball", 10, gp);
		Item.addItem("full restore", 10, gp);
	}
	
 }
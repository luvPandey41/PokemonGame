package pokemon;

import java.util.HashMap;
import java.util.Map;

public class TypeChart {

    private static final Map<String, Map<String, Double>> typeChart = new HashMap<>();

    static {
        // Initialize all type maps
        String[] types = {
            "normal", "fire", "water", "electric", "grass", "ice", "fighting", "poison",
            "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon", "dark", "steel", "fairy"
        };

        for (String type : types) {
            typeChart.put(type, new HashMap<>());
        }

        // === NORMAL
        typeChart.get("normal").put("rock", 0.5);
        typeChart.get("normal").put("ghost", 0.0);
        typeChart.get("normal").put("steel", 0.5);

        // === FIRE
        typeChart.get("fire").put("fire", 0.5);
        typeChart.get("fire").put("water", 0.5);
        typeChart.get("fire").put("grass", 2.0);
        typeChart.get("fire").put("ice", 2.0);
        typeChart.get("fire").put("bug", 2.0);
        typeChart.get("fire").put("rock", 0.5);
        typeChart.get("fire").put("dragon", 0.5);
        typeChart.get("fire").put("steel", 2.0);

        // === WATER
        typeChart.get("water").put("fire", 2.0);
        typeChart.get("water").put("water", 0.5);
        typeChart.get("water").put("grass", 0.5);
        typeChart.get("water").put("ground", 2.0);
        typeChart.get("water").put("rock", 2.0);
        typeChart.get("water").put("dragon", 0.5);

        // === ELECTRIC
        typeChart.get("electric").put("water", 2.0);
        typeChart.get("electric").put("electric", 0.5);
        typeChart.get("electric").put("grass", 0.5);
        typeChart.get("electric").put("ground", 0.0);
        typeChart.get("electric").put("flying", 2.0);
        typeChart.get("electric").put("dragon", 0.5);

        // === GRASS
        typeChart.get("grass").put("fire", 0.5);
        typeChart.get("grass").put("water", 2.0);
        typeChart.get("grass").put("grass", 0.5);
        typeChart.get("grass").put("poison", 0.5);
        typeChart.get("grass").put("ground", 2.0);
        typeChart.get("grass").put("flying", 0.5);
        typeChart.get("grass").put("bug", 0.5);
        typeChart.get("grass").put("rock", 2.0);
        typeChart.get("grass").put("dragon", 0.5);
        typeChart.get("grass").put("steel", 0.5);

        // === ICE
        typeChart.get("ice").put("fire", 0.5);
        typeChart.get("ice").put("water", 0.5);
        typeChart.get("ice").put("grass", 2.0);
        typeChart.get("ice").put("ice", 0.5);
        typeChart.get("ice").put("ground", 2.0);
        typeChart.get("ice").put("flying", 2.0);
        typeChart.get("ice").put("dragon", 2.0);
        typeChart.get("ice").put("steel", 0.5);

        // === FIGHTING
        typeChart.get("fighting").put("normal", 2.0);
        typeChart.get("fighting").put("ice", 2.0);
        typeChart.get("fighting").put("poison", 0.5);
        typeChart.get("fighting").put("flying", 0.5);
        typeChart.get("fighting").put("psychic", 0.5);
        typeChart.get("fighting").put("bug", 0.5);
        typeChart.get("fighting").put("rock", 2.0);
        typeChart.get("fighting").put("ghost", 0.0);
        typeChart.get("fighting").put("dark", 2.0);
        typeChart.get("fighting").put("steel", 2.0);
        typeChart.get("fighting").put("fairy", 0.5);

        // === POISON
        typeChart.get("poison").put("grass", 2.0);
        typeChart.get("poison").put("poison", 0.5);
        typeChart.get("poison").put("ground", 0.5);
        typeChart.get("poison").put("rock", 0.5);
        typeChart.get("poison").put("ghost", 0.5);
        typeChart.get("poison").put("steel", 0.0);
        typeChart.get("poison").put("fairy", 2.0);

        // === GROUND
        typeChart.get("ground").put("fire", 2.0);
        typeChart.get("ground").put("electric", 2.0);
        typeChart.get("ground").put("grass", 0.5);
        typeChart.get("ground").put("poison", 2.0);
        typeChart.get("ground").put("flying", 0.0);
        typeChart.get("ground").put("bug", 0.5);
        typeChart.get("ground").put("rock", 2.0);
        typeChart.get("ground").put("steel", 2.0);

        // === FLYING
        typeChart.get("flying").put("electric", 0.5);
        typeChart.get("flying").put("grass", 2.0);
        typeChart.get("flying").put("fighting", 2.0);
        typeChart.get("flying").put("bug", 2.0);
        typeChart.get("flying").put("rock", 0.5);
        typeChart.get("flying").put("steel", 0.5);

        // === PSYCHIC
        typeChart.get("psychic").put("fighting", 2.0);
        typeChart.get("psychic").put("poison", 2.0);
        typeChart.get("psychic").put("psychic", 0.5);
        typeChart.get("psychic").put("dark", 0.0);
        typeChart.get("psychic").put("steel", 0.5);

        // === BUG
        typeChart.get("bug").put("fire", 0.5);
        typeChart.get("bug").put("grass", 2.0);
        typeChart.get("bug").put("fighting", 0.5);
        typeChart.get("bug").put("poison", 0.5);
        typeChart.get("bug").put("flying", 0.5);
        typeChart.get("bug").put("psychic", 2.0);
        typeChart.get("bug").put("ghost", 0.5);
        typeChart.get("bug").put("dark", 2.0);
        typeChart.get("bug").put("steel", 0.5);
        typeChart.get("bug").put("fairy", 0.5);

        // === ROCK
        typeChart.get("rock").put("fire", 2.0);
        typeChart.get("rock").put("ice", 2.0);
        typeChart.get("rock").put("fighting", 0.5);
        typeChart.get("rock").put("ground", 0.5);
        typeChart.get("rock").put("flying", 2.0);
        typeChart.get("rock").put("bug", 2.0);
        typeChart.get("rock").put("steel", 0.5);

        // === GHOST
        typeChart.get("ghost").put("normal", 0.0);
        typeChart.get("ghost").put("psychic", 2.0);
        typeChart.get("ghost").put("ghost", 2.0);
        typeChart.get("ghost").put("dark", 0.5);

        // === DRAGON
        typeChart.get("dragon").put("dragon", 2.0);
        typeChart.get("dragon").put("steel", 0.5);
        typeChart.get("dragon").put("fairy", 0.0);

        // === DARK
        typeChart.get("dark").put("fighting", 0.5);
        typeChart.get("dark").put("psychic", 2.0);
        typeChart.get("dark").put("ghost", 2.0);
        typeChart.get("dark").put("dark", 0.5);
        typeChart.get("dark").put("fairy", 0.5);

        // === STEEL
        typeChart.get("steel").put("fire", 0.5);
        typeChart.get("steel").put("water", 0.5);
        typeChart.get("steel").put("electric", 0.5);
        typeChart.get("steel").put("ice", 2.0);
        typeChart.get("steel").put("rock", 2.0);
        typeChart.get("steel").put("fairy", 2.0);
        typeChart.get("steel").put("steel", 0.5);

        // === FAIRY
        typeChart.get("fairy").put("fire", 0.5);
        typeChart.get("fairy").put("fighting", 2.0);
        typeChart.get("fairy").put("dragon", 2.0);
        typeChart.get("fairy").put("dark", 2.0);
        typeChart.get("fairy").put("poison", 0.5);
        typeChart.get("fairy").put("steel", 0.5);
    }

    public static double getEffectiveness(Move move, Pokemon target) {
        String moveType = move.type.toLowerCase();

        double eff1 = 1.0;
        double eff2 = 1.0;

        if (typeChart.containsKey(moveType)) {
            eff1 = typeChart.get(moveType).getOrDefault(target.primaryType.toLowerCase(), 1.0);

            if (target.secondaryType != null) {
                eff2 = typeChart.get(moveType).getOrDefault(target.secondaryType.toLowerCase(), 1.0);
            }
        }
        
        if(move.name.equals("Shadow Claw") && target.name.equals("Pidgey")) {
        	System.out.println("eff1=" + eff1 + ", eff2=" + eff2 + ", move=" + move.name + ", type=" + move.type + ", target=" + target.name + ", types=" + target.primaryType + "/" + target.secondaryType);
        }

        return eff1 * eff2;
    }
    
    public static double getEffectiveness(Pokemon mon, Pokemon target) {
        String moveType = mon.primaryType.toLowerCase();

        double eff1 = 1.0;
        double eff2 = 1.0;

        if (typeChart.containsKey(moveType)) {
            eff1 = typeChart.get(moveType).getOrDefault(target.primaryType.toLowerCase(), 1.0);

            if (target.secondaryType != null) {
                eff2 = typeChart.get(moveType).getOrDefault(target.secondaryType.toLowerCase(), 1.0);
            }
        }
        
        double eff3 = eff1 * eff2;
        
        if(mon.secondaryType != null) {
            String secondaryMoveType = mon.secondaryType.toLowerCase();
            if (typeChart.containsKey(secondaryMoveType)) {
                double eff4 = typeChart.get(secondaryMoveType).getOrDefault(target.primaryType.toLowerCase(), 1.0);
                double eff5 = 1.0;
                if (target.secondaryType != null) {
                    eff5 = typeChart.get(secondaryMoveType).getOrDefault(target.secondaryType.toLowerCase(), 1.0);
                }
                return eff3 * eff4 * eff5;
            }
        }

        return eff3;
    }

}

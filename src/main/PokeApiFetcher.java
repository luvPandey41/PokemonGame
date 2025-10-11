package main;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.*;

public class PokeApiFetcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        String pokemonName = "pikachu";
        String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName;

        // 1. Create HTTP Client and Request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        // 2. Send the request and get response body as String
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Failed to fetch data. Status: " + response.statusCode());
            return;
        }

        // 3. Parse JSON response
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        // 4. Extract basic info
        String name = json.get("name").getAsString();
        String spriteUrl = json.getAsJsonObject("sprites").get("front_default").getAsString();

        // 5. Extract types
        JsonArray typesArray = json.getAsJsonArray("types");
        JsonArray types = new JsonArray();
        for (JsonElement typeEntry : typesArray) {
            JsonObject typeObj = typeEntry.getAsJsonObject().getAsJsonObject("type");
            types.add(typeObj.get("name").getAsString());
        }

        // 6. Extract base stats
        JsonArray statsArray = json.getAsJsonArray("stats");
        JsonObject stats = new JsonObject();
        for (JsonElement statEntry : statsArray) {
            JsonObject statObj = statEntry.getAsJsonObject();
            String statName = statObj.getAsJsonObject("stat").get("name").getAsString();
            int baseStat = statObj.get("base_stat").getAsInt();
            stats.addProperty(statName, baseStat);
        }

        // 7. Extract move names (limit to first 10 to avoid overload)
        JsonArray movesArray = json.getAsJsonArray("moves");
        JsonArray moveNames = new JsonArray();
        for (int i = 0; i < Math.min(100, movesArray.size()); i++) {
            JsonObject move = movesArray.get(i).getAsJsonObject();
            String moveName = move.getAsJsonObject("move").get("name").getAsString();
            moveNames.add(moveName);
        }

        // 8. Combine everything into a new JSON object
        JsonObject finalPokemonData = new JsonObject();
        finalPokemonData.addProperty("name", name);
        finalPokemonData.addProperty("sprite", spriteUrl);
        finalPokemonData.add("types", types);
        finalPokemonData.add("base_stats", stats);
        finalPokemonData.add("moves", moveNames);

        // 9. Save to file
        try (FileWriter writer = new FileWriter(name + ".json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(finalPokemonData, writer);
        }

        System.out.println("Saved data to " + name + ".json");
    }
}

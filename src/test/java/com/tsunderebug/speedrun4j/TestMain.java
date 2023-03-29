package com.tsunderebug.speedrun4j;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tsunderebug.speedrun4j.game.Game;
import com.tsunderebug.speedrun4j.user.User;
import com.tsunderebug.speedrun4j.util.Pagination;

public class TestMain {

	public static void main(String[] args) {
		Speedrun4J s4j = new Speedrun4J("Speedrun4J Test");
		try {
			Game game = s4j.getGame("ebtr");
			User user = s4j.getUser("Gamebuster1990");
			JsonArray links = game.getData().getAsJsonArray("links");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			/*for(JsonElement e : links) {
				if(e.getAsJsonObject().get("rel").getAsString().equals("runs")) {
					System.out.println(gson.toJson(s4j.getRawData(new URL(e.getAsJsonObject().get("uri").getAsString()))));
				}
			}*/
			System.out.println(gson.toJson(user.getData()));
			//URL games = user.getLink("games");
			Pagination<Game> games = user.getModeratedGames();//.setMaxSize(1);
			games = games.setMaxSize(1);
			games = games.seekPage(1);
			games.getSelfLink();
			System.out.println(gson.toJson(games.getData()));
			System.out.println(games.get(0).getName());
			games = games.prev();
			System.out.println(games.get(0).getName());
			games = games.next();
			System.out.println(games.get(0).getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

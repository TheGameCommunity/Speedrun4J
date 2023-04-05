package com.tsunderebug.speedrun4j;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.tsunderebug.speedrun4j._fedata.ModeratedGame;
import com.tsunderebug.speedrun4j._fedata.PlayedGame;
import com.tsunderebug.speedrun4j.game.Category;
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
			Pagination<Game> games = user.getModeratedGames().setMaxSize(20);
			
			for(Game g : games) {
				System.out.println(g.getName());
			}

			PlayedGame[] playedgames = user.getUserStats().getPlayedGames();
			for(PlayedGame g: playedgames) {
				System.out.println(g.getName() + ": " + g.getTotalRuns() + " runs");
			}
			
			ModeratedGame[] moderatedGames = user.getUserStats().getModeratedGames();
			for(ModeratedGame g: moderatedGames) {
				System.out.println(g.getName() + ": " + g.getModLevel());
			}
			
			for(Category category : game.getCategories()) {
				System.out.println(category.getName() + ": " + category.getType());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

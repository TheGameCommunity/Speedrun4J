package com.tsunderebug.speedrun4j._fedata;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonObjectData;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.user.User;
import com.tsunderebug.speedrun4j.util.Undocumented;

@Undocumented
public class UserStats implements JsonObjectData {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	@Undocumented
	public UserStats(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}
	
	@Undocumented
	public String getID() {
		return getUserData().get("id").getAsString();
	}
	
	@Undocumented
	public User getUser() throws IOException {
		return s4j.getUser(getID());
	}
	
	@Undocumented
	public PlayedGame[] getPlayedGames() {
		JsonArray followStats = data.get("gameStats").getAsJsonArray();
		PlayedGame[] ret = new PlayedGame[followStats.size()];
		for(int i = 0; i < followStats.size(); i++) {
			ret[i] = new PlayedGame(s4j, followStats.get(i).getAsJsonObject());
		}
		return ret;
	}
	
	@Undocumented
	public FollowedGame[] getFollowedGames() {
		JsonArray followStats = data.get("followStats").getAsJsonArray();
		FollowedGame[] ret = new FollowedGame[followStats.size()];
		for(int i = 0; i < followStats.size(); i++) {
			ret[i] = new FollowedGame(s4j, followStats.get(i).getAsJsonObject());
		}
		return ret;
	}
	
	@Undocumented
	public ModeratedGame[] getModeratedGames() {
		JsonArray followStats = data.get("modStats").getAsJsonArray();
		ModeratedGame[] ret = new ModeratedGame[followStats.size()];
		for(int i = 0; i < followStats.size(); i++) {
			ret[i] = new ModeratedGame(s4j, followStats.get(i).getAsJsonObject());
		}
		return ret;
	}
	
	@Undocumented
	private JsonObject getUserData() {
		return data.getAsJsonObject("user");
	}
	
	@Override
	public Speedrun4J getS4j() {
		return s4j;
	}

	@Override
	public JsonObject getData() {
		return data.deepCopy();
	}
	
}

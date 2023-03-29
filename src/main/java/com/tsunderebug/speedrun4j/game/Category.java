package com.tsunderebug.speedrun4j.game;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.LinkedJson;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.game.run.Playtype;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Category implements LinkedJson {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public Category(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}

	public String getId() {
		return data.get("id").getAsString();
	}

	public String getName() {
		return data.get("name").getAsString();
	}

	public URL getLink() throws MalformedURLException {
		return new URL(data.get("weblink").getAsString());
	}

	public String getType() {
		return data.get("type").getAsString();
	}

	public String getRules() {
		return data.get("rules").getAsString();
	}

	public Playtype getPlayers() {
		//TODO: implement
		return null;
	}

	public boolean isMiscellaneous() {
		return data.get("miscellaneous").getAsBoolean();
	}

	public Game getGame() throws IOException {
		return new Game(s4j, s4j.getRawData(this.getLink("game")));
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

package com.tsunderebug.speedrun4j.game;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.Identified;
import com.tsunderebug.speedrun4j.LinkedJson;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.WebLinked;

public class Level implements LinkedJson, Identified, WebLinked {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public Level(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}
	
	public String getRules() {
		return data.get("rules").getAsString();
	}
	
	public Game getGame() throws IOException {
		return new Game(s4j, s4j.getRawData(this.getLink("game")));
	}
	
	public Category[] getCategories() throws IOException {
		return s4j.getCategories(this);
	}
	
	public Variable[] getVariables() throws IOException {
		return s4j.getVariables(this);
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

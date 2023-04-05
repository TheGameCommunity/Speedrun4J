package com.tsunderebug.speedrun4j.game;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.Identified;
import com.tsunderebug.speedrun4j.LinkedJson;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.WebLinked;
import com.tsunderebug.speedrun4j.game.run.Playtype;

import java.io.IOException;

public class Category implements LinkedJson, Identified, WebLinked {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public Category(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}

	public CategoryType getType() {
		return CategoryType.fromString(data.get("type").getAsString());
	}

	public String getRules() {
		return data.get("rules").getAsString();
	}

	public Playtype getPlayers() {
		throw new UnsupportedOperationException();
	}

	public boolean isMiscellaneous() {
		return data.get("miscellaneous").getAsBoolean();
	}

	public Game getGame() throws IOException {
		return new Game(s4j, s4j.getRawData(this.getLink("game")));
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

	public static enum CategoryType {
		FULL_GAME("per-game"),
		INDIVIDUAL_LEVEL("per-level"),
		UNKNOWN("unknown");

		private final String name;
		
		CategoryType(String name) {
			this.name = name;
		}
		
		public String toString() {
			return name;
		}
		
		public static CategoryType fromString(String type) {
			for(CategoryType t : values()) {
				if(t.name.equals(type)) {
					return t;
				}
			}
			return UNKNOWN;
		}
	}
	
}

package com.tsunderebug.speedrun4j.game.run;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.Identified;
import com.tsunderebug.speedrun4j.LinkedJson;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.user.User;

public class Player implements LinkedJson, Identified {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public Player(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}
	
	public PlayerType getUserType() {
		return PlayerType.fromString(data.get("rel").getAsString());
	}
	
	public User getUser() throws IOException {
		if(getUserType() == PlayerType.USER) {
			return s4j.getUser(getID());
		}
		throw new UnsupportedOperationException("Cannot get the user of a guest.");
	}
	
	@Override
	public Speedrun4J getS4j() {
		return s4j;
	}

	@Override
	public JsonObject getData() {
		return data;
	}
	
	public static enum PlayerType {
		USER("user"),
		GUEST("guest"),
		UNKNOWN("unknown");

		private final String id;
		
		PlayerType(String id) {
			this.id = id;
		}
		
		public static PlayerType fromString(String id) {
			for(PlayerType p : values()) {
				if(id.equals(p.id)) {
					return p;
				}
			}
			return UNKNOWN;
		}
	}

}

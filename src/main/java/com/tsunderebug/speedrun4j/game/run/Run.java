package com.tsunderebug.speedrun4j.game.run;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.Identified;
import com.tsunderebug.speedrun4j.LinkedJson;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.WebLinked;
import com.tsunderebug.speedrun4j.game.Category;
import com.tsunderebug.speedrun4j.game.Game;
import com.tsunderebug.speedrun4j.game.Level;
import com.tsunderebug.speedrun4j.game.Proof;

public class Run implements LinkedJson, Identified, WebLinked {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public Run(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}
	
	/**
	 * @return the game the run is associated with
	 * @throws IOException if an IOException occurs
	 * @throws UnsupportedOperationException if the run does not exist
	 */
	public Game getGame() throws IOException, UnsupportedOperationException {
		if(exists()) {
			return s4j.getGame(data.get("game").getAsString());
		}
		throw new UnsupportedOperationException("Run does not exist");
	}
	
	/**
	 * @return the level the run is associated with, or null if the run is not associated with a level
	 * @throws IOException if an IOException occurs
	 * @throws UnsupportedOperationException if the run does not exist
	 */
	public Level getLevel() throws IOException, UnsupportedOperationException {
		if(exists()) {
			JsonElement l = data.get("level");
			if(l.isJsonNull()) {
				return null;
			}
			return s4j.getLevel(l.getAsString());
		}
		throw new UnsupportedOperationException("Run does not exist");
	}
	
	/**
	 * @return the category the run is associated with
	 * @throws IOException if an IOException occurs
	 * @throws UnsupportedOperationException if the run does not exist
	 */
	public Category getCategory() throws IOException, UnsupportedOperationException {
		if(exists()) {
			return s4j.getCategory(data.get("category").getAsString());
		}
		throw new UnsupportedOperationException("Run does not exist");
	}
	
	/**
	 * @return a Proof object that represents the proof provided for the run
	 * @throws UnsupportedOperationException if the run does not exist
	 */
	public Proof getProof() {
		if(exists()) {
			return new Proof(s4j, data.get("videos"));
		}
		throw new UnsupportedOperationException("Run does not exist");
	}
	
	/**
	 * @throws UnsupportedOperationException if the run does not exist
	 */
	public String getComment() {
		if(exists()) {
			return data.get("comment").getAsString();
		}
		throw new UnsupportedOperationException("Run does not exist");
	}
	

	
	/**
	 * @throws UnsupportedOperationException if this run does not exist
	 */
	public Player[] getPlayers() {
		if(exists()) {
			JsonArray p = data.get("players").getAsJsonArray();
			Player[] players = new Player[p.size()];
			for(int i = 0; i < players.length; i++) {
				players[i] = new Player(s4j, p.get(i).getAsJsonObject());
			}
			return players;
		}
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @return {@link Status#PENDING} if the run is pending verification
	 * <p>{@link Status#VERIFIED} if the run has been verified
	 * <P>{@link Status#REJECTED} if the run has been rejected
	 * <p>{@link Status#NOT_FOUND} if the request was valid but the run was not found (Typically happens when the run is deleted)
	 * <p>{@link Status#UNKNOWN} if the status was none of the above
	 */
	public RunStatus<JsonElement> getStatus() {
		return new RunStatus<>(s4j, data.get("status"));
	}
	
	public boolean exists() {
		switch (getStatus().getStatus()) {
			case PENDING:
			case REJECTED:
			case VERIFIED:
				return true;
			default:
				return false;
		}
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

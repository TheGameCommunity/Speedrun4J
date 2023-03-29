package com.tsunderebug.speedrun4j.game;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonData;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.game.run.Ruleset;
import com.tsunderebug.speedrun4j.platform.Platform;
import com.tsunderebug.speedrun4j.user.GameStaff;
import com.tsunderebug.speedrun4j.user.Moderator;
import com.tsunderebug.speedrun4j.user.Supermod;
import com.tsunderebug.speedrun4j.user.Verifier;

public class Game implements JsonData {
	
	private Speedrun4J s4j;
	private JsonObject gameData;
	
	public Game(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.gameData = data;
	}
	
	public String getID() {
		return gameData.get("id").getAsString();
	}
	
	public String getName() {
		return gameData.getAsJsonObject("names").get("international").getAsString();
	}
	
	public String getAbbreviation() {
		return gameData.get("abbreviation").getAsString();
	}
	
	public Year getReleaseYear() {
		return Year.of(gameData.get("released").getAsInt());
	}
	
	public LocalDate getReleaseDate() {
		return LocalDate.parse(gameData.get("release-date").getAsString());
	}
	
	public Ruleset getRuleset() {
		return new Ruleset(gameData.getAsJsonObject("ruleset"));
	}
	
	public GameType[] getGameTypes() {
		return null;
	}
	
	public Platform[] getPlatforms() {
		return null;
	}
	
	public Region[] getRegions() {
		return null;
	}
	
	public Genre[] getGenres() {
		return null;
	}
	
	public Engine[] getEngines() {
		return null;
	}
	
	public Developer[] getDevelopers() {
		return null;
	}
	
	public Publisher[] getPublishers() {
		return null;
	}
	
	public Supermod[] getSupermods() {
		return null;
	}
	
	public Moderator[] getNormalModerators() {
		return null;
	}
	
	public Verifier[] getVerifiers() {
		return null;
	}
	
	public GameStaff[] getGameStaff() {
		return null;
	}
	
	public LocalDateTime getDateCreated() {
		String date = gameData.get("created").getAsString();
		return LocalDateTime.parse(date.substring(0, date.length() - 1));
	}
	
	public Record[] getRecords() {
		return getRecords(0);
	}
	
	public Record[] getRecords(int page) {
		return null;
	}
	
	public JsonObject getData() {
		return gameData.deepCopy();
	}

	@Override
	public Speedrun4J getS4j() {
		return s4j;
	}
	
}

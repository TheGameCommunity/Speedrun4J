package com.tsunderebug.speedrun4j._fedata;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonObjectData;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.game.Game;
import com.tsunderebug.speedrun4j.util.Undocumented;

@Undocumented
public class UndocumentedGame implements JsonObjectData {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	@Undocumented
	public UndocumentedGame(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}
	
	@Undocumented
	public Game getGame() throws IOException {
		return s4j.getGame(getGameData().get("id").getAsString());
	}
	
	@Undocumented
	public String getAbbreviation() {
		return getGameData().get("url").getAsString();
	}
	
	@Undocumented
	public String getName() {
		return getGameData().get("name").getAsString();
	}
	
	@Undocumented
	public String getType() {
		return getGameData().get("type").getAsString();
	}
	
	@Undocumented
	public boolean excludesLoadtimes() {
		return getGameData().get("loadTimes").getAsBoolean();
	}
	
	@Undocumented
	public boolean hasMilliseconds() {
		return getGameData().get("milliseconds").getAsBoolean();
	}
	
	@Undocumented
	public boolean hasIGT() {
		return getGameData().get("igt").getAsBoolean();
	}
	
	@Undocumented
	public boolean hasVerification() {
		return getGameData().get("verification").getAsBoolean();
	}
	
	@Undocumented
	public boolean requiresVideo() {
		return getGameData().get("requireVideo").getAsBoolean();
	}
	
	@Undocumented
	public boolean modRunsAutoVerify() {
		return getGameData().get("autoVerify").getAsBoolean();
	}
	
	@Undocumented
	public LocalDate getReleaseDate() {
		return LocalDate.from(Instant.ofEpochSecond(getGameData().get("releaseDate").getAsLong()));
	}
	
	@Undocumented
	public LocalDateTime getLeaderboardCreationDate() {
		return LocalDateTime.from(Instant.ofEpochSecond(getGameData().get("addedDate").getAsLong()));
	}
	
	@Undocumented
	public LocalDateTime getLastRefreshDate() {
		return LocalDateTime.from(Instant.ofEpochSecond(getGameData().get("touchDate").getAsLong()));
	}
	
	@Undocumented
	public int getActivePlayerCount() {
		return getGameData().get("activePlayerCount").getAsInt();
	}
	
	@Undocumented
	public int getBoostCount() {
		return getGameData().get("boostReceivedCount").getAsInt();
	}
	
	@Undocumented
	public int getDistinctBoostersCount() {
		return getGameData().get("boostDistinctDonorsCount").getAsInt();
	}
	
	@Undocumented
	private JsonObject getGameData() {
		return data.getAsJsonObject("game");
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

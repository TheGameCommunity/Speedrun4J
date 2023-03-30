package com.tsunderebug.speedrun4j._fedata;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.util.Undocumented;

@Undocumented
public class PlayedGame extends UndocumentedGame {

	@Undocumented
	public PlayedGame(Speedrun4J s4j, JsonObject data) {
		super(s4j, data);
	}

	@Undocumented
	public int getTotalRuns() {
		return getData().get("totalRuns").getAsInt();
	}
	
	@Undocumented
	public Duration getPlaytime() {
		return Duration.ofSeconds(getData().get("totalTime").getAsLong());
	}
	
	@Undocumented
	public int getUniqueLevels() {
		return getData().get("uniqueLevels").getAsInt();
	}
	
	@Undocumented
	public int getUniqueCategories() {
		return getData().get("uniqueCategories").getAsInt();
	}
	
	@Undocumented
	public LocalDateTime getFirstRunDate() {
		return LocalDateTime.from(Instant.ofEpochSecond(getData().get("minDate").getAsLong()));
	}
	
	@Undocumented
	public LocalDateTime getLastRunDate() {
		return LocalDateTime.from(Instant.ofEpochSecond(getData().get("maxDate").getAsLong()));
	}
	
}

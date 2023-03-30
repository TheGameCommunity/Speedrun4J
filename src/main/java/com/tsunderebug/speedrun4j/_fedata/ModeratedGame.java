package com.tsunderebug.speedrun4j._fedata;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.user.ModeratorType;
import com.tsunderebug.speedrun4j.util.Undocumented;

@Undocumented
public class ModeratedGame extends UndocumentedGame {

	@Undocumented
	public ModeratedGame(Speedrun4J s4j, JsonObject data) {
		super(s4j, data);
	}

	@Undocumented
	public ModeratorType getModLevel() {
		return ModeratorType.get(getData().get("level").getAsInt());
	}
	
	@Undocumented
	public int getReviewCount() {
		return getData().get("totalRuns").getAsInt();
	}
	
	@Undocumented
	public Duration getReviewTime() {
		return Duration.ofSeconds(getData().get("totalTime").getAsLong());
	}
	
	@Undocumented
	public LocalDateTime getAppointmentDate() {
		return LocalDateTime.from(Instant.ofEpochSecond(getData().get("minDate").getAsLong()));
	}
	
	@Undocumented
	public LocalDateTime getLastReviewDate() {
		return LocalDateTime.from(Instant.ofEpochSecond(getData().get("maxDate").getAsLong()));
	}
	
}

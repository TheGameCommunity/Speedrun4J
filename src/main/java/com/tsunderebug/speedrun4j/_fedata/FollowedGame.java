package com.tsunderebug.speedrun4j._fedata;

import java.time.Instant;
import java.time.LocalDateTime;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.util.Undocumented;

@Undocumented
public class FollowedGame extends UndocumentedGame {

	@Undocumented
	public FollowedGame(Speedrun4J s4j, JsonObject data) {
		super(s4j, data);
	}

	@Undocumented
	public int getPos() {
		return getData().get("pos").getAsInt();
	}
	
	@Undocumented
	public int getAccessCount() {
		return getData().get("accessCount").getAsInt();
	}
	
	@Undocumented
	public LocalDateTime getAccessDate() {
		return LocalDateTime.from(Instant.ofEpochSecond(getData().get("lastAccessDate").getAsInt()));
	}
	
}

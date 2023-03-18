package com.tsunderebug.speedrun4j.game.run;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonData;

public class Ruleset implements JsonData {
	
	private JsonObject ruleData;
	
	public Ruleset(JsonObject ruleData) {
		this.ruleData = ruleData;
	}
	
	public boolean showMilliseconds() {
		return ruleData.get("show-milliseconds").getAsBoolean();
	}
	
	public boolean requiresVerification() {
		return ruleData.get("require-verification").getAsBoolean();
	}
	
	public boolean requiresVideo() {
		return ruleData.get("require-video").getAsBoolean();
	}
	
	public boolean emulatorsAllowed() {
		return ruleData.get("emulators-allowed").getAsBoolean();
	}
	
	public JsonObject getData() {
		return ruleData.deepCopy();
	}

}

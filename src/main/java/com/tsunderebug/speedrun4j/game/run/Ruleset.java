package com.tsunderebug.speedrun4j.game.run;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonData;
import com.tsunderebug.speedrun4j.Speedrun4J;

public class Ruleset implements JsonData {
	
	private Speedrun4J s4j;
	private JsonObject ruleData;
	
	public Ruleset(Speedrun4J s4j, JsonObject ruleData) {
		this.s4j = s4j;
		this.ruleData = ruleData;
	}
	
	public boolean showMilliseconds() {
		return ruleData.get("show-milliseconds").getAsBoolean();
	}
	
	public boolean requiresVerification() {
		return ruleData.get("require-verification").getAsBoolean();
	}
	
	/*
	public TimingMethod[] getTimingMethods () {
		
	}
	*/
	
	/*
	public TimingMethod getDefaultTimingMethod() {
		
	}
	*/
	
	public boolean requiresVideo() {
		return ruleData.get("require-video").getAsBoolean();
	}
	
	public boolean emulatorsAllowed() {
		return ruleData.get("emulators-allowed").getAsBoolean();
	}
	
	@Override
	public JsonObject getData() {
		return ruleData.deepCopy();
	}

	@Override
	public Speedrun4J getS4j() {
		return s4j;
	}

}

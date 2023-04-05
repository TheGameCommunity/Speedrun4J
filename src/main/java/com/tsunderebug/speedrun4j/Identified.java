package com.tsunderebug.speedrun4j;

public interface Identified extends JsonData {

	public default String getID() {
		return getData().get("id").getAsString();
	}
	
	public default String getName() {
		return getData().get("name").getAsString();
	}
	
}

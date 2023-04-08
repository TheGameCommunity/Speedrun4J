package com.tsunderebug.speedrun4j;

import com.google.gson.JsonObject;

public interface Identified extends JsonObjectData {

	/**
	 * @return the id of this, or null if this does not have an id
	 */
	public default String getID() {
		JsonObject data = getData();
		if(data.has("id")) {
			return data.get("id").getAsString();
		}
		return null;
	}
	
	/**
	 * @return the name of this, or null if this does not have a name
	 */
	public default String getName() {
		JsonObject data = getData();
		if(data.has("name")) {
			return data.get("name").getAsString();
		}
		return null;
	}
	
}

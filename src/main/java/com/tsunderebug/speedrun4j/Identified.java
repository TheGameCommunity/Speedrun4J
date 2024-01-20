package com.tsunderebug.speedrun4j;

import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonElement;
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
		else {
			if(data.has("names")) {
				JsonObject names = (JsonObject) data.get("names");
				Set<Entry<String, JsonElement>> nameSet = names.entrySet();
				if(!nameSet.isEmpty()) {
					return nameSet.iterator().next().getValue().getAsString();
				}
			}
		}
		return null;
	}
	
}

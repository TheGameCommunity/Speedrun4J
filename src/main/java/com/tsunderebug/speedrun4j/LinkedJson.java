package com.tsunderebug.speedrun4j;

import com.google.gson.JsonObject;

public interface LinkedJson extends JsonData {

	public default JsonObject getLinks() {
		return this.getData().getAsJsonObject("links");
	}
	
}

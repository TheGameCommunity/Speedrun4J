package com.tsunderebug.speedrun4j;

import com.google.gson.JsonArray;

public interface LinkedJson extends JsonData {

	public default JsonArray getLinks() {
		return this.getData().getAsJsonArray("links");
	}
	
}

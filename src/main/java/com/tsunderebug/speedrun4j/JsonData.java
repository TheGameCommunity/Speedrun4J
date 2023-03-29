package com.tsunderebug.speedrun4j;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface JsonData {

	public Speedrun4J getS4j();
	public JsonObject getData();
	
	public static JsonData asJsonData(Speedrun4J s4j, JsonElement e) {
		if(e.isJsonObject()) {
			return new JsonData() {

				@Override
				public Speedrun4J getS4j() {
					return s4j;
				}

				@Override
				public JsonObject getData() {
					return e.getAsJsonObject();
				}
				
			};
		}
		throw new IllegalArgumentException(e.toString());
	}
	
	public default LinkedJson asLinkedJson() {
		return new LinkedJson() {

			@Override
			public Speedrun4J getS4j() {
				return JsonData.this.getS4j();
			}

			@Override
			public JsonObject getData() {
				return JsonData.this.getData();
			}
			
		};
	}
	
}

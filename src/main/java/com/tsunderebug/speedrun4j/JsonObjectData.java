package com.tsunderebug.speedrun4j;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface JsonObjectData extends JsonData<JsonObject>{

	public Speedrun4J getS4j();
	
	public static JsonObjectData asJsonData(Speedrun4J s4j, JsonElement e) {
		if(e.isJsonObject()) {
			return new JsonObjectData() {

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
				return JsonObjectData.this.getS4j();
			}

			@Override
			public JsonObject getData() {
				return JsonObjectData.this.getData();
			}
			
		};
	}
	
}

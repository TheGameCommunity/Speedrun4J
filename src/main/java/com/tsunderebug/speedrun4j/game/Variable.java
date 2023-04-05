package com.tsunderebug.speedrun4j.game;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonData;
import com.tsunderebug.speedrun4j.LinkedJson;
import com.tsunderebug.speedrun4j.Speedrun4J;

public class Variable implements LinkedJson {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public Variable(Speedrun4J s4j, JsonObject data) {
		this.s4j = s4j;
		this.data = data;
	}
	
	public String getID() {
		return data.get("id").getAsString();
	}
	
	public String getName() {
		return data.get("name").getAsString();
	}
	
	public Category getCategory() throws IOException {
		JsonElement e = data.get("category");
		if(e.isJsonNull()) {
			return null;
		}
		return s4j.getCategory(e.getAsString());
	}
	
	public Scope getScope() {
		return new Scope(s4j, data.get("scope").getAsJsonObject());
	}
	
	public boolean isMandatory() {
		return data.get("mandatory").getAsBoolean();
	}
	
	public boolean isUserDefined() {
		return data.get("user-defined").getAsBoolean();
	}
	
	public boolean canObsolete() {
		return data.get("obsoletes").getAsBoolean();
	}
	
	public Value[] getValues() {
		JsonObject values = data.get("values").getAsJsonObject();
		Value[] ret = new Value[values.entrySet().size()];
		int i = 0;
		Iterator<Map.Entry<String, JsonElement>> iterator = values.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, JsonElement> entry = iterator.next();
			
			ret[i] = new Value(s4j, entry.getKey(), entry.getValue().getAsJsonObject());
			i++;
		}
		return ret;
	}
	
	public Value getDefaultValue() {
		JsonObject values = data.get("values").getAsJsonObject();
		JsonElement val = values.get("default");
		if(val.isJsonNull()) {
			return null;
		}
		return new Value(s4j, val.getAsString(), values.get(val.getAsString()).getAsJsonObject());
	}
	
	public boolean isSubCategory() {
		return data.get("is-subcategory").getAsBoolean();
	}
	
	public Game getGame() throws IOException {
		return new Game(s4j, s4j.getRawData(getLink("game")));
	}
	
	@Override
	public Speedrun4J getS4j() {
		return s4j;
	}

	@Override
	public JsonObject getData() {
		return data.deepCopy();
	}
	
	public static final class Value implements JsonData {

		private final Speedrun4J s4j;
		private final String id;
		private final JsonObject data;
		
		public Value(Speedrun4J s4j, String id, JsonObject data) {
			this.s4j = s4j;
			this.id = id;
			this.data = data;
		}
		
		public String getID() {
			return id;
		}
		
		public String getLabel() {
			return data.get("label").getAsString();
		}
		
		public String getRules() {
			return data.get("rules").getAsString();
		}
		
		public boolean isMiscellaneous() {
			return getFlags().get("miscellaneous").getAsBoolean();
		}
		
		public JsonObject getFlags() {
			return data.get("flags").getAsJsonObject();
		}
		
		@Override
		public Speedrun4J getS4j() {
			return s4j;
		}

		@Override
		public JsonObject getData() {
			return data.deepCopy();
		}
		
	}

	public static final class Scope implements JsonData {
		
		protected final Speedrun4J s4j;
		protected final JsonObject data;
		protected final String name;
		
		protected Scope(Speedrun4J s4j, JsonObject data) {
			this.s4j = s4j;
			this.data = data;
			name = data.get("type").getAsString();
		}
		
		public String getType() {
			return name;
		}
		
		public Level getLevel() throws IOException {
			if(data.has("level")) {
				return s4j.getLevel(data.get("level").getAsString());
			}
			return null;
		}

		@Override
		public Speedrun4J getS4j() {
			return s4j;
		}

		@Override
		public JsonObject getData() {
			return data.deepCopy();
		}

		
	}
	
}

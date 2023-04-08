package com.tsunderebug.speedrun4j.game;

import java.io.IOException;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonObjectData;
import com.tsunderebug.speedrun4j.Speedrun4J;

public class Proof implements JsonObjectData {

	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public Proof(Speedrun4J s4j, JsonElement data) {
		this.s4j = s4j;
		if(data.isJsonObject()) {
			this.data = data.getAsJsonObject();
		}
		else {
			this.data = null;
		}
	}
	
	/**
	 * @return true if there is proof data, false otherwise
	 */
	public boolean hasProofData() {
		return data != null;
	}
	
	/**
	 * @return true if there is proof text, false otherwise
	 */
	public boolean hasProofText() {
		if(hasProofData()) {
			if(data.has("text")) {
				if(data.get("text").isJsonPrimitive()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return true if true if there are proof links, false otherwise
	 */
	public boolean hasProofLinks() {
		if(hasProofData()) {
			if(data.has("links")) {
				JsonArray links = data.get("links").getAsJsonArray();
				if(links.size() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return the proof text, or null if it does not exist
	 */
	public String getProofText() {
		if(hasProofText()) {
			return data.get("text").getAsString();
		}
		return null;
	}
	
	/**
	 * @return an array of proof URLs, or an empty array if there are no proof urls.
	 * @throws IOException if a malformed url is encountered (should be impossible)
	 */
	public URL[] getProofLinks() throws IOException {
		if(hasProofLinks()) {
			JsonArray links = data.get("links").getAsJsonArray();
			URL[] urls = new URL[links.size()];
			for(int i = 0; i < links.size(); i++) {
				urls[i] = new URL(links.get(i).getAsJsonObject().get("uri").getAsString());
			}
			return urls;
		}
		return new URL[]{};
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

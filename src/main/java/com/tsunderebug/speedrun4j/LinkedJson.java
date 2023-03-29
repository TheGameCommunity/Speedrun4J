package com.tsunderebug.speedrun4j;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface LinkedJson extends JsonData {

	public default JsonArray getLinks() {
		return this.getData().getAsJsonArray("links");
	}
	
	public default boolean hasLinks() {
		return this.getLinks() != null;
	}
	
	public default boolean hasLink(String name) {
		return getLink(name) != null;
	}
	
	public default URL getSelfLink() {
		return getLink("self");
	}
	
	public default URL getLink(String name) {
		if(hasLinks()) {
			JsonArray links = getLinks();
			for(JsonElement e : getLinks()) {
				JsonObject o = e.getAsJsonObject();
				if(o.has("rel")) {
					if(o.get("rel").getAsString().equals(name)) {
						try {
							return new URL(o.get("uri").getAsString());
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	
}

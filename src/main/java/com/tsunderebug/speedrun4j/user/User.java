package com.tsunderebug.speedrun4j.user;

import java.io.IOError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.function.Function;

import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.LinkedJson;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j._fedata.UserStats;
import com.tsunderebug.speedrun4j.game.Game;
import com.tsunderebug.speedrun4j.util.Pagination;
import com.tsunderebug.speedrun4j.util.URIFixer;
import com.tsunderebug.speedrun4j.util.Undocumented;

public class User implements LinkedJson {
	
	private final Speedrun4J s4j;
	private final JsonObject data;
	
	public User(Speedrun4J s4j, JsonObject json) {
		this.s4j = s4j;
		this.data = json;
	}

	
	public String getId() {
		return data.get("id").getAsString();
	}
	
	public String getName() {
		return getName("international");
	}
	
	public String getJapaneseName() {
		return getName("japanese");
	}
	
	/**
	 * Valid types: "international", "japanese"
	 * Names can be null
	 * @return A map of locales to user names
	 */
	public String getName(String type) {
		JsonObject names = data.getAsJsonObject("names");
		if(names.has(type)) {
			return names.get(type).getAsString();
		}
		return "";
	}

	public URL getWeblink() throws MalformedURLException {
		return new URL(URIFixer.fix(data.get("weblink")).getAsString());
	}

	public NameStyle getNameStyle() {
		return null;
	}

	public String getRole() {
		return data.get("role").getAsString();
	}

	public LocalDateTime getSignup() {
		String date = data.get("created").getAsString();
		return LocalDateTime.parse(date.substring(0, date.length() - 1));
	}

	public Location getLocation() {
		if(data.has("location")) {
			//return new Location(data.get("location"));
		}
		return null;
	}

	public URL getTwitch() {
		return getSocial("twitch");
	}

	public URL getHitbox() {
		return getSocial("hitbox");
	}

	public URL getYoutube() {
		return getSocial("youtube");
	}

	public URL getTwitter() {
		return getSocial("twitter");
	}
	
	public URL getSpeedrunsLive() {
		return getSocial("speedrunslive");
	}
	
	public URL getSocial(String socialNetwork) {
		if(data.has(socialNetwork)) {
			try {
				return new URL(data.get(socialNetwork).getAsString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Record[] getPBs() throws IOException {
		return null;
	}
	
	@Undocumented
	public UserStats getUserStats() throws IOException {
		return s4j.getUserStats(getId());
	}

	@Override
	public JsonObject getData() {
		return data.deepCopy();
	}

	public Pagination<Game> getModeratedGames() throws IOException {
		Function<JsonObject, Game> jsonFactory = (json) -> {return new Game(s4j, json);};
		Function<URL, Game> urlFactory = (url) -> {
			try {
				return jsonFactory.apply(s4j.getRawData(url));
			} catch (IOException e) {
				throw new IOError(e);
			}
		};
		return new Pagination<Game>(s4j, getLink("games"), jsonFactory, urlFactory);
	}

	@Override
	public Speedrun4J getS4j() {
		return s4j;
	}

}

package com.tsunderebug.speedrun4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.tsunderebug.speedrun4j._fedata.UserStats;
import com.tsunderebug.speedrun4j.game.Category;
import com.tsunderebug.speedrun4j.game.Game;
import com.tsunderebug.speedrun4j.user.User;
import com.tsunderebug.speedrun4j.util.URIFixer;
import com.tsunderebug.speedrun4j.util.Undocumented;

public final class Speedrun4J {

	private static final Gson GSON = new Gson();
	private static final String USER_AGENT_ROOT;
	private static final String API_ROOT = "https://www.speedrun.com/api/v1/";
	@Undocumented
	@Deprecated
	private static final String UNDOCUMENTED_API_ROOT = "https://www.speedrun.com/_fedata/";
	
	static {
		String version = "@speedrun4JVersion@";
		USER_AGENT_ROOT = version.indexOf('@') != -1 ? "Speedrun4j/DEVELOP" : "Speedrun4j/" + version;
	}
	
	private final String userAgent;
	private final String apiKey;
	
	public Speedrun4J(String userAgent) {
		this(userAgent, null);
	}
	
	public Speedrun4J(String userAgent, String apiKey) {
		this.userAgent = USER_AGENT_ROOT + " - " + userAgent;
		this.apiKey = apiKey;
	}
	
	public final String getUserAgent() {
		return USER_AGENT_ROOT + " - " + userAgent; 
	}
	
	public Game getGame(String gameID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "games/" + gameID);
		return new Game(this, getData(url).getAsJsonObject());
	}
	
	public User getUser(String userID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "users/" + userID);
		return new User(this, getData(url).getAsJsonObject());
	}
	
	public Category getCategory(String categoryID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "categories/" + categoryID);
		return new Category(this, getData(url).getAsJsonObject());
	}
	
	@Undocumented
	public UserStats getUserStats(String userID) throws IOException {
		URL url = new URL(UNDOCUMENTED_API_ROOT + "user/stats?userId=" + userID);
		return new UserStats(this, getRawData(url).getAsJsonObject());
	}
	
	public JsonElement getData(URL url) throws IOException {
		return getRawData(url).get("data");
	}
	
	public JsonObject getRawData(URL url) throws IOException {
		if(!url.getProtocol().equals("https")) {
			throw new IOException("Insecure protocol: " + url.getProtocol());
		}
		HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
		c.setRequestProperty("User-Agent", userAgent);
		InputStreamReader r = new InputStreamReader(c.getInputStream());
		JsonReader json = GSON.newJsonReader(r);
		JsonElement data = JsonParser.parseReader(json);
		return JsonParser.parseString(URIFixer.fix(data.toString())).getAsJsonObject();
	}
	
}

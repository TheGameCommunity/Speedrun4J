package com.tsunderebug.speedrun4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.tsunderebug.speedrun4j._fedata.UserStats;
import com.tsunderebug.speedrun4j.game.Category;
import com.tsunderebug.speedrun4j.game.Game;
import com.tsunderebug.speedrun4j.game.Level;
import com.tsunderebug.speedrun4j.game.Variable;
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
	
	/**
	 * @param gameID
	 * @return the game with the specified id
	 * @throws IOException
	 */
	public Game getGame(String gameID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "games/" + gameID);
		return new Game(this, getData(url).getAsJsonObject());
	}
	
	/**
	 * @param userID
	 * @return the user with the speicifed id
	 * @throws IOException
	 */
	public User getUser(String userID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "users/" + userID);
		return new User(this, getData(url).getAsJsonObject());
	}
	
	/**
	 * @param categoryID
	 * @return the category with the specified id
	 * @throws IOException
	 */
	public Category getCategory(String categoryID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "categories/" + categoryID);
		return new Category(this, getData(url).getAsJsonObject());
	}
	
	/**
	 * @param game the game to obtain categories from
	 * @return all categories of the specified game
	 * @throws IOException
	 */
	public Category[] getCategories(Game game) throws IOException {
		return getCategories((LinkedJson) game);
	}
	
	/**
	 * @param level the level to obtain categories from
	 * @return all categories of the specified level
	 * @throws IOException
	 */
	public Category[] getCategories(Level level) throws IOException {
		return getCategories((LinkedJson) level);
	}
	
	/**
	 * @deprecated intended for internal use only.
	 * @param obj the object to obtain categories from
	 * @return all categories of the specified object
	 * @throws IOException
	 */
	@Deprecated
	public Category[] getCategories(LinkedJson obj) throws IOException {
		return getCategories(new URL(obj.getSelfLink() + "/categories"));
	}
	
	/**
	 * @param url the urls to obtain categories from
	 * @return all categories the SRC api returns from the specified url
	 * @throws IOException
	 */
	private Category[] getCategories(URL url) throws IOException {
		JsonArray data = getData(url).getAsJsonArray();
		Category[] categories = new Category[data.size()];
		for(int i = 0; i < categories.length; i++) {
			categories[i] = new Category(this, data.get(i).getAsJsonObject());
		}
		return categories;
	}
	

	/**
	 * @param levelID
	 * @return the level with the specified ID
	 * @throws IOException
	 */
	public Level getLevel(String levelID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "levels/" + levelID);
		return new Level(this, getData(url).getAsJsonObject());
	}
	
	/**
	 * @param variableID
	 * @return the variable with the specified ID
	 * @throws IOException
	 */
	public Variable getVariable(String variableID) throws IOException {
		URL url = new URL(Speedrun4J.API_ROOT + "variables/" + variableID);
		return new Variable(this, getData(url).getAsJsonObject());
	}
	
	/**
	 * @param category
	 * @return the variables of the specified category
	 * @throws IOException
	 */
	public Variable[] getVariables(Category category) throws IOException {
		return getVariables((LinkedJson) category);
	}
	
	/**
	 * @param game
	 * @return the variables of the specified game
	 * @throws IOException
	 */
	public Variable[] getVariables(Game game) throws IOException {
		return getVariables((LinkedJson) game);
	}
	
	/**
	 * @param level
	 * @return the variables of the specified level
	 * @throws IOException
	 */
	public Variable[] getVariables(Level level) throws IOException {
		return getVariables((LinkedJson) level);
	}
	
	/**
	 * @deprecated intended for internal use only.
	 * @param obj the object to obtain variables from
	 * @return all variables of the specified object
	 * @throws IOException
	 */
	@Deprecated
	public Variable[] getVariables(LinkedJson obj) throws IOException {
		return getVariables(new URL(obj.getSelfLink() + "/variables"));
	}
	
	/**
	 * @param url the urls to obtain variables from
	 * @return all variables the SRC api returns from the specified url
	 * @throws IOException
	 */
	private Variable[] getVariables(URL url) throws IOException {
		JsonArray data = getData(url).getAsJsonArray();
		Variable[] variables = new Variable[data.size()];
		for(int i = 0; i < variables.length; i++) {
			variables[i] = new Variable(this, data.get(i).getAsJsonObject());
		}
		return variables;
	}
	
	/**
	 * WARNING: Uses undocumented SRC API, this may break or be removed
	 * at any time!
	 * 
	 * @Undocumented
	 * @param userID
	 * @return Stats of the specified user
	 * @throws IOException
	 */
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

package com.tsunderebug.speedrun4j.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonData;
import com.tsunderebug.speedrun4j.Paginated;
import com.tsunderebug.speedrun4j.Speedrun4J;

public final class Pagination<T extends JsonData> implements Paginated<T> {
	
	private static final Pattern maxURIPattern = Pattern.compile("&max=\\d{1,3}");
	private static final Pattern offsetURIPattern = Pattern.compile("&offset=\\d{1,3}");
	
	private final JsonData data;
	private final URL url;
	private final Function<JsonObject, T> jsonFactory;
	private final Function<URL, T> urlFactory;
	
	public Pagination(Speedrun4J s4j, URL url, Function<JsonObject, T> jsonFactory, Function<URL, T> urlFactory) throws IOException {
		this(url, JsonData.asJsonData(s4j, s4j.getRawData(url)), jsonFactory, urlFactory);
	}
	
	private Pagination(URL url, JsonData data, Function<JsonObject, T> jsonFactory, Function<URL, T> urlFactory) {
		this.url = url;
		this.data = data;
		this.jsonFactory = jsonFactory;
		this.urlFactory = urlFactory;
	}
	
	private Pagination(URL url, Function<JsonObject, T> jsonFactory, Function<URL, T> urlFactory) {
		this(url, urlFactory.apply(url), jsonFactory, urlFactory);
	}
	
	private Pagination(URL url, Pagination<T> pagination) {
		this(url, pagination.jsonFactory, pagination.urlFactory);
	}
	
	public int getOffset() {
		return getPaginationData().get("offset").getAsInt();
	}
	
	public int getMax() {
		return getPaginationData().get("max").getAsInt();
	}
	
	public int getSize() {
		return getPaginationData().get("size").getAsInt();
	}
	
	private URL getNext() {
		if(hasLinks()) {
			JsonArray links = getPaginationData().getAsJsonArray("links");
			for(JsonElement e : links) {
				if(e.getAsJsonObject().get("rel").getAsString().equals("next")) {
					try {
						return new URL(e.getAsJsonObject().get("rel").getAsString());
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	private URL getPrevious() {
		if(hasLinks()) {
			JsonArray links = getPaginationData().getAsJsonArray("links");
			for(JsonElement e : links) {
				if(e.getAsJsonObject().get("rel").getAsString().equals("prev")) {
					try {
						return new URL(e.getAsJsonObject().get("rel").getAsString());
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @return the next page
	 */
	@Override
	public Pagination<T> next() {
		return seekPage(getPage() + 1);
	}
	
	/**
	 * @return the previous page
	 */
	@Override
	public Pagination<T> prev() {
		return seekPage(getPage() - 1);
	}
	
	@Override
	public JsonObject getData() {
		return data.getData().deepCopy();
	}
	
	public JsonObject getPaginationData() {
		return getData().getAsJsonObject("pagination");
	}
	
	@Override
	public URL getSelfLink() {
		return url;
	}

	@Override
	public Speedrun4J getS4j() {
		return data.getS4j();
	}
	
	@Override
	public Pagination<T> setMaxSize(int max) {
		URL url = getSelfLink();
		String nextSTR = url.toString();
		int end = getOffset() + getSize();
		Matcher maxMatcher = maxURIPattern.matcher(nextSTR);
		
		if(maxMatcher.matches()) {
			nextSTR = maxMatcher.replaceFirst("&max=" + max);
		}
		else {
			nextSTR = nextSTR + "&max=" + max;
		}
		try {
			return new Pagination<>(new URL(nextSTR), this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @deprecated It is recommended to seek using {@link #seekPage(int)}
	 */
	@Override
	@Deprecated
	public Pagination<T> seek(int offset) {
		String ret = this.getSelfLink().toString();
		Matcher offsetMatcher = offsetURIPattern.matcher(ret);
		
		if(offsetMatcher.matches()) {
			ret = offsetMatcher.replaceFirst("&offset=" + offset);
		}
		else {
			ret = ret + "&offset=" + offset;
		}
		try {
			return new Pagination<>(new URL(ret), this);
		} catch (MalformedURLException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public Pagination<T> seekPage(int page) {
		String ret = this.getSelfLink().toString();
		Matcher offsetMatcher = offsetURIPattern.matcher(ret);
		
		if(offsetMatcher.matches()) {
			ret = offsetMatcher.replaceFirst("&offset=" + page * getMax());
		}
		else {
			ret = ret + "&offset=" + page * getMax();
		}
		System.out.println(ret);
		try {
			return new Pagination<T>(new URL(ret), this);
		} catch (MalformedURLException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public T get(int index) {
		JsonObject parentData = getData();
		if(parentData.has("data")) {
			JsonArray data = parentData.getAsJsonArray("data");
			if(index < data.size()) {
				return jsonFactory.apply(data.get(index).getAsJsonObject());
			}
		}
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		JsonArray data = getData().getAsJsonArray("data");
		int size = data.size();
		return new Iterator<T>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				return index < size;
			}

			@Override
			public T next() {
				T ret = jsonFactory.apply(data.get(index).getAsJsonObject());
				index++;
				return ret;
			}
			
		};
	}

	@Override
	public boolean hasNextPage() {
		return data.asLinkedJson().hasLink("next");
	}

	@Override
	public boolean hasPreviousPage() {
		return data.asLinkedJson().hasLink("prev");
	}
	
}

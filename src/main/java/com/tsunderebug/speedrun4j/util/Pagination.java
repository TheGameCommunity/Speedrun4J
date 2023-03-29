package com.tsunderebug.speedrun4j.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
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
		return getLink("next");
	}
	
	private URL getPrevious() {
		return getLink("prev");
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
		JsonObject o = getData().getAsJsonObject("pagination");
		if(o == null) {
			System.err.println(url);
			System.err.println(getData().toString());
		}
		return o;
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
		if(max < 1) {
			throw new IndexOutOfBoundsException(max + "");
		}
		URL url = getSelfLink();
		String nextSTR = url.toString();
		int end = getOffset() + getSize();
		Matcher maxMatcher = maxURIPattern.matcher(nextSTR);
		
		if(maxMatcher.find()) {
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
		if(offset < 0) {
			throw new IndexOutOfBoundsException(offset + "");
		}
		String ret = this.getSelfLink().toString();
		Matcher offsetMatcher = offsetURIPattern.matcher(ret);
		
		if(offsetMatcher.find()) {
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
		if(page < 0) {
			throw new IndexOutOfBoundsException(page + "");
		}
		String ret = this.getSelfLink().toString();
		Matcher offsetMatcher = offsetURIPattern.matcher(ret);
		if(offsetMatcher.find()) {
			ret = offsetMatcher.replaceFirst("&offset=" + page * getMax());
		}
		else {
			ret = ret + "&offset=" + page * getMax();
		}
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
	
	public ListIterator<T> iterator() {
		return new ListIterator<T>() {
			ListIterator<Paginated<T>> pageIterator = pageIterator();
			Paginated<T> page = Pagination.this;
			int index = 0;
			@Override
			public boolean hasNext() {
				if(index < page.getSize()) {
					return true;
				}
				else {
					if (pageIterator.hasNext()) { //sometimes the api can return an empty page, so we have to check if the size of the next page is 0
						boolean valid = pageIterator.next().getSize() > 0;
						pageIterator.previous();
						return valid;
					}
				}
				return false;
			}

			@Override
			public T next() {
				if(index < page.getSize()) {
					return page.get(index++);
				}
				else if(pageIterator.hasNext()) {
					page = page.next();
					pageIterator = page.pageIterator();
					index = 0;
					return page.get(index++);
				}
				else {
					return null;
				}
			}

			@Override
			public boolean hasPrevious() {
				return index > 0 || pageIterator.hasPrevious();
			}

			@Override
			public T previous() {
				if(index > 0) {
					return page.get(index--);
				}
				else if(pageIterator.hasPrevious()) {
					page = page.prev();
					pageIterator = page.pageIterator();
					index = page.getSize();
					return page.get(index--);
				}
				return null;
			}

			@Override
			public int nextIndex() {
				return -1;
			}

			@Override
			public int previousIndex() {
				return -1;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public void set(T e) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void add(T e) {
				throw new UnsupportedOperationException();
			}
			
		};
	}

	@Override
	public ListIterator<Paginated<T>> pageIterator() {
		return new ListIterator<Paginated<T>>() {
			Paginated<T> current = Pagination.this;
			
			@Override
			public boolean hasNext() {
				return current.hasNextPage();
			}
			
			@Override
			public Paginated<T> next() {
				return current = current.next();
			}
			
			@Override
			public boolean hasPrevious() {
				return current.hasPreviousPage();
			}
			
			@Override
			public Paginated<T> previous() {
				return current = current.prev();
			}
			
			@Override
			public int nextIndex() {
				return getPage() + 1;
			}
			
			@Override
			public int previousIndex() {
				return getPage() - 1;
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void set(Paginated<T> e) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public void add(Paginated<T> e) {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
	public Stream<T> stream() {
		Iterator<T> iterator = iterator();
		
		return Stream.iterate(
			get(0),
			(obj) -> {
				return(iterator.hasNext() || pageIterator().hasNext());
			},
			(obj) -> {
				if(iterator.hasNext()) {
					return iterator.next();
				}
				else if(pageIterator().hasNext()) {
					return pageIterator().next().get(0);
				}
				return null;
			}
		);
	}
	
	@Override
	public JsonArray getLinks() {
		return data.getData().getAsJsonObject("pagination").getAsJsonArray("links");
	}

	@Override
	public boolean hasNextPage() {
		return hasLink("next");
	}

	@Override
	public boolean hasPreviousPage() {
		return hasLink("prev");
	}
	
}

package com.tsunderebug.speedrun4j.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class URIFixer {

	public static final Pattern FIX_PATTERN = Pattern.compile("\\\\u[a-f0-9]{4}"); //true regex: \\u[a-f0-9]{4}
	
	public static String fix(String uri) {
		String ret = uri;
		Matcher m = FIX_PATTERN.matcher(uri);
		Iterator<MatchResult> i = m.results().iterator();
		while(i.hasNext()) {
			MatchResult result = i.next();
			String matched = uri.substring(result.start(), result.end());
			int codepoint = Integer.parseInt(matched.substring(2), 16);
			String replacement = new String(Character.toChars(codepoint));
			ret = ret.replace(matched, replacement);
		}
		return ret;
	}
	
	public static JsonElement fix(JsonElement uri) {
		return new JsonPrimitive(fix(uri.getAsString()));
	}
	
	public static URI fix(URI uri) {
		try {
			return new URI(fix(uri.toString()));
		} catch (URISyntaxException e) {
			throw new AssertionError(e); //should be impossible...
		}
	}
	
	public static URL fix(URL url) {
		try {
			return new URL(fix(url.toString()));
		} catch (MalformedURLException e) {
			throw new AssertionError(e); //should be impossible...
		}
	}
	
}
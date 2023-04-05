package com.tsunderebug.speedrun4j;

import java.io.IOException;
import java.net.URL;

public interface WebLinked extends JsonData {

	public default URL getLink() throws IOException {
		return new URL(getData().get("weblink").getAsString());
	}
	
}

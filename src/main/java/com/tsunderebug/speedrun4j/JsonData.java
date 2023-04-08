package com.tsunderebug.speedrun4j;

import com.google.gson.JsonElement;

public interface JsonData<T extends JsonElement> {

	public Speedrun4J getS4j();
	public T getData();
	
}

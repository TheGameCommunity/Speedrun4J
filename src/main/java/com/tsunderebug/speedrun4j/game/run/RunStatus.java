package com.tsunderebug.speedrun4j.game.run;

import java.io.IOException;
import java.time.LocalDateTime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsunderebug.speedrun4j.JsonData;
import com.tsunderebug.speedrun4j.Speedrun4J;
import com.tsunderebug.speedrun4j.user.User;

public class RunStatus<T extends JsonElement> implements JsonData<T> {

	private final Speedrun4J s4j;
	private final T data;
	
	public RunStatus(Speedrun4J s4j, T data) {
		this.s4j = s4j;
		this.data = data;
	}
	
	public Status getStatus() {
		if(isFromJsonObject()) {
			return Status.fromJSON(((JsonObject)data).get("status"));
		}
		return Status.fromJSON(data);
	}
	
	public User getExaminer() throws IOException {
		if(isFromJsonObject()) {
			JsonElement examiner = getData(JsonObject.class).get("examiner");
			if(examiner == null || examiner.isJsonNull()) {
				return null;
			}
			return s4j.getUser(examiner.getAsString());
		}
		return null;
	}
	
	public LocalDateTime getReviewDate() {
		switch(getStatus()) {
			case VERIFIED:
				String date = getData(JsonObject.class).get("verify-date").getAsString();
				return LocalDateTime.parse(date.substring(0, date.length() - 1));
			case REJECTED:
				return null; //TODO
			default:
				return null;
		}
	}
	
	public final boolean isFromJsonObject() {
		return data.isJsonObject();
	}
	
	@Override
	public Speedrun4J getS4j() {
		return s4j;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getData() {
		if(data.isJsonObject()) {
			return (T)data.getAsJsonObject().deepCopy();
		}
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public <Type> Type getData(Class<Type> type) {
		return (Type)getData();
	}

	public static enum Status {
		PENDING("new"),
		VERIFIED("verified"),
		REJECTED("rejected"),
		NOT_FOUND("404"),
		UNKNOWN("unknown");

		private final String id;
		
		Status(String id) {
			this.id = id;
		}
		
		public static Status fromJSON(JsonElement e) {
			if(e.isJsonObject()) {
				return fromID(e.getAsJsonObject().get("status").getAsString());
			}
			return fromID(e.getAsString());
		}
		
		public static Status fromID(String id) {
			for(Status status : values()) {
				if(id.equals(status.id)) {
					return status;
				}
			}
			return UNKNOWN;
		}
	}
	
}

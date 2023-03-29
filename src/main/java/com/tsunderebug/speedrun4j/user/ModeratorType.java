package com.tsunderebug.speedrun4j.user;

public enum ModeratorType {

	UNKNOWN(Integer.MIN_VALUE, "unknown-moderation-rank"),
	VERIFIER(-1, "verifier"),
	MODERATOR(0, "moderator"),
	SUPER_MODERATOR(1, "super-moderator");

	private final int index;
	private final String name;
	
	ModeratorType(int i, String name) {
		this.index = i;
		this.name = name;
	}
	
	public static ModeratorType get(String name) {
		for(ModeratorType modType : values()) {
			if(modType.name.equals(name)) {
				return modType;
			}
		}
		return UNKNOWN;
	}
	
	public static ModeratorType get(int index) {
		switch(index) {
			case -1:
				return VERIFIER;
			case 0:
				return MODERATOR;
			case 1:
				return SUPER_MODERATOR;
			default:
				return UNKNOWN;
		}
	}
	
	
}

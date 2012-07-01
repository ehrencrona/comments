package com.velik.comments.json;

public class NoSuchValueException extends Exception {

	public NoSuchValueException(String key) {
		super("Missing value " + key + ".");
	}

}

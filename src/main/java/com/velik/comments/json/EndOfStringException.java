package com.velik.comments.json;

public class EndOfStringException extends ParseException {

	public EndOfStringException(JsonParser parser) {
		super(parser, "Unexpected end of string.");
	}

}

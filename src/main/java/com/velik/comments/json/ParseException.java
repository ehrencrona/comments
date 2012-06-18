package com.velik.comments.json;

public class ParseException extends Exception {

	public ParseException(JsonParser parser, String message) {
		super("At " + getPosition(parser) + ": " + message);
	}

	private static String getPosition(JsonParser parser) {
		String json = parser.getJson();
		int at = parser.getAt();

		if (at >= json.length()) {
			return "end of string";
		}

		String result = json.substring(at);

		if (result.length() > 20) {
			result = result + "...";
		}

		return "..." + result;
	}
}

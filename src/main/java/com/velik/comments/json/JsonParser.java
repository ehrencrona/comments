package com.velik.comments.json;

public class JsonParser {
	int at = 0;
	private String json;

	public JsonParser(String json) {
		this.json = json;
	}

	public Object parse() throws ParseException {
		char ch = peekSkippingWhitespace();

		Object result;

		if (ch == '{') {
			result = parseMap();
		} else if (ch == '[') {
			result = parseArray();
		} else if (ch == '"') {
			result = parseString();
		} else {
			result = parseNumber();
		}

		return result;

	}

	protected char peekSkippingWhitespace() throws EndOfStringException {
		char ch = readSkippingWhitespace();

		backtrack();

		return ch;
	}

	private JsonArray parseArray() throws ParseException {
		char ch = read();

		if (ch != '[') {
			throw new ParseException(this, "Expected [");
		}

		ch = peekSkippingWhitespace();

		JsonArray result = new JsonArray();

		if (ch == ']') {
			read();

			return result;
		}

		do {
			result.add(parse());

			ch = readSkippingWhitespace();

			if (ch != ',' && ch != ']') {
				throw new ParseException(this, "Expected , or ]");
			}
		} while (ch == ',');

		return result;
	}

	protected char readSkippingWhitespace() throws EndOfStringException {
		char ch = read();

		while (isWhitespace(ch)) {
			ch = read();
		}

		return ch;
	}

	private JsonMap parseMap() throws ParseException {
		char ch = read();

		if (ch != '{') {
			throw new ParseException(this, "Expected {");
		}

		JsonMap result = new JsonMap();

		do {
			ch = peekSkippingWhitespace();

			if (ch != '}') {
				Object key = parse();

				ch = readSkippingWhitespace();

				if (ch != ':') {
					throw new ParseException(this, "Expected :");
				}

				Object value = parse();

				result.put(key, value);

				ch = readSkippingWhitespace();

				if (ch != ',' && ch != '}') {
					throw new ParseException(this, "Expected , or }");
				}
			} else {
				read();
			}
		} while (ch == ',');

		return result;
	}

	private Integer parseNumber() throws ParseException {
		StringBuffer result = new StringBuffer(10);

		char ch;

		do {
			try {
				ch = read();
			} catch (EndOfStringException e) {
				break;
			}

			if (Character.isLetter(ch)) {
				throw new ParseException(this, "Expected a number but found \"" + result + "\".");
			}

			if (Character.isDigit(ch)) {
				result.append(ch);
			} else {
				backtrack();
			}
		} while (Character.isDigit(ch));

		try {
			return Integer.parseInt(result.toString());
		} catch (NumberFormatException e) {
			throw new ParseException(this, "Expected a number.");
		}
	}

	private String parseString() throws ParseException {
		char ch = read();

		if (ch != '"') {
			throw new ParseException(this, "Expected \"");
		}

		StringBuffer result = new StringBuffer(10);

		while ((ch = read()) != '"') {
			result.append(ch);
		}

		return result.toString();
	}

	private void backtrack() {
		at--;
	}

	private char peek() throws EndOfStringException {
		if (at < json.length()) {
			return json.charAt(at);
		} else {
			throw new EndOfStringException(this);
		}
	}

	private boolean isWhitespace(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\n';
	}

	private char read() throws EndOfStringException {
		try {
			return peek();
		} finally {
			at++;
		}
	}

	public String getJson() {
		return json;
	}

	public int getAt() {
		return at;
	}
}

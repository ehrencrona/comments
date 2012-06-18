package com.velik.comments.json;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class JsonObject {

	public String toString() {
		return toJson();
	}

	public String toJson() {
		CharArrayWriter writer = new CharArrayWriter(1024);

		try {
			print(writer);
		} catch (IOException e) {
			return "'" + e.toString() + "'";
		}

		return new String(writer.toCharArray());
	}

	public abstract void print(Writer writer) throws IOException;

	public static JsonObject toJsonObject(Object object) {
		if (object instanceof JsonObject) {
			return (JsonObject) object;
		} else {
			return new JsonLiteral(object);
		}
	}

	public static JsonArray array(Object... values) {
		return new JsonArray(values);
	}

	public static JsonMap map() {
		return new JsonMap();
	}

}

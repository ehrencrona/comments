package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonObject {
	private List<Object> array = new ArrayList<Object>();

	public JsonArray(Object... values) {
		for (Object value : values) {
			array.add(value);
		}
	}

	public JsonArray add(int number) {
		array.add(number);

		return this;
	}

	public JsonArray add(String string) {
		array.add(string);

		return this;
	}

	public JsonArray add(JsonObject object) {
		array.add(object);

		return this;
	}

	public JsonArray add(Object object) {
		array.add(object);

		return this;
	}

	@Override
	public void print(Writer writer) throws IOException {
		writer.append('[');

		boolean first = true;

		for (Object object : array) {
			if (first) {
				first = false;
			} else {
				writer.append(',');
			}

			JsonObject.toJsonObject(object).print(writer);
		}

		writer.append(']');
	}

}

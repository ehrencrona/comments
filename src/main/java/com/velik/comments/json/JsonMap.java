package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JsonMap extends JsonObject {
	private Map<Object, Object> map = new LinkedHashMap<Object, Object>();

	public JsonMap put(Object key, Object value) {
		map.put(key, value);

		return this;
	}

	@Override
	public void print(Writer writer) throws IOException {
		writer.append('{');

		boolean first = true;

		for (Entry<Object, Object> object : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				writer.append(',');
			}

			JsonObject.toJsonObject(object.getKey()).print(writer);

			writer.append(':');

			JsonObject.toJsonObject(object.getValue()).print(writer);
		}

		writer.append('}');
	}
}

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

			Object key = object.getKey();

			toJsonObject(key).print(writer);

			writer.append(':');

			toJsonObject(object.getValue()).print(writer);
		}

		writer.append('}');
	}

	public Object get(String key) throws NoSuchValueException {
		Object result = map.get(key);

		if (result == null) {
			throw new NoSuchValueException(key);
		}

		return result;
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}
}

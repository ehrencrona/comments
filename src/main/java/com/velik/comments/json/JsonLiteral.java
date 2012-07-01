package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

public class JsonLiteral extends JsonObject {
	private Object object;

	public JsonLiteral(Object object) {
		this.object = object;
	}

	@Override
	public void print(Writer writer) throws IOException {
		if (object == null) {
			writer.write("null");
			return;
		}

		if (object instanceof Number || object instanceof Boolean) {
			writer.write(object.toString());
		} else {
			writer.append('"');
			writer.write(object.toString().replaceAll("\"", "\\\"").replaceAll("\n", "\\\n"));
			writer.append('"');
		}
	}
}

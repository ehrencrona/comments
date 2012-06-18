package com.velik.comments.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonParserTest {

	@Test
	public void testParse() throws Exception {
		assertJsonEquals("[]", "[]");
		assertJsonEquals("{}", "{}");
		assertJsonEquals("123", "123");
		assertJsonEquals("1", "1");
		assertJsonEquals("\"\"", "\"\"");
		assertJsonEquals("\"abc\"", "\"abc\"");
		assertJsonEquals("[1,2,3]", "[1,2,3]");
		assertJsonEquals("[1,2,3]", " [ 1 , 2 , 3 ] ");
		assertJsonEquals("[1,2,3]", "[1,2,3]");
		assertJsonEquals("[{1:2},{2:3}]", "[{1:2},{2:3}]");
		assertJsonEquals("{\"a\":1,\"b\":2}", "{\"a\":1,\"b\":2}");
		assertJsonEquals("{\"a\":1,\"b\":2}", " { \"a\" : 1 , \"b\" : 2 } ");
	}

	private void assertJsonEquals(String expectedJson, String parse) throws Exception {
		Object result = new JsonParser(parse).parse();
		JsonObject json;

		if (result instanceof JsonObject) {
			json = (JsonObject) result;
		} else {
			json = new JsonLiteral(result);
		}

		Assert.assertEquals(expectedJson, json.toJson());
	}
}

package com.velik.comments.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonParserTest {

	@Test
	public void testArrays() throws Exception {
		assertJsonEquals("[]", "[]");
		assertJsonEquals("[1,2,3]", "[1,2,3]");
		assertJsonEquals("[1,2,3]", " [ 1 , 2 , 3 ] ");
		assertJsonEquals("[1,2,3]", "[1,2,3]");
		assertJsonEquals("[{1:2},{2:3}]", "[{1:2},{2:3}]");
	}

	@Test
	public void testMaps() throws Exception {
		assertJsonEquals("{}", "{}");
		assertJsonEquals("{\"a\":1,\"b\":2}", "{\"a\":1,\"b\":2}");
		assertJsonEquals("{\"a\":1,\"b\":2}", " { \"a\" : 1 , \"b\" : 2 } ");
		assertJsonEquals("{\"a\":[0]}", "{\"a\":[0]}");
		assertJsonEquals("{\"a\":true}", "{\"a\":true}");
	}

	@Test
	public void testLiterals() throws Exception {
		assertJsonEquals("123", "123");
		assertJsonEquals("1", "1");
		assertJsonEquals("\"\"", "\"\"");
		assertJsonEquals("\"abc\"", "\"abc\"");
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

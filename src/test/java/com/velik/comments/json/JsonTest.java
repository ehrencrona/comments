package com.velik.comments.json;

import static com.velik.comments.json.JsonObject.array;
import static com.velik.comments.json.JsonObject.map;

import org.junit.Assert;
import org.junit.Test;

public class JsonTest {

	@Test
	public void testAllAtOnce() {
		Assert.assertEquals("{\"a\":1,\"b\":[1,2,3]}", map().put("a", 1).put("b", array(1, 2, 3)).toJson());
	}
}

package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;

public class ProfileIdSetJsonWrapper extends JsonObject {
	private ProfileSet set;

	public ProfileIdSetJsonWrapper(ProfileSet set) {
		this.set = set;
	}

	@Override
	public void print(Writer writer) throws IOException {
		JsonArray array = new JsonArray();

		for (ProfileId id : set) {
			array.add(id.getIntegerId());
		}

		array.print(writer);
	}

}

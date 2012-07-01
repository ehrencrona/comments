package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

import com.velik.comments.Finder;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;

public class ProfileSetJsonWrapper extends JsonObject {
	private ProfileSet set;
	private Finder finder;

	public ProfileSetJsonWrapper(ProfileSet set, Finder finder) {
		this.set = set;
		this.finder = finder;
	}

	@Override
	public void print(Writer writer) throws IOException {
		JsonArray array = new JsonArray();

		for (ProfileId profileId : set) {
			Profile profile = finder.getProfile(profileId);

			array.add(new ProfileJsonWrapper(profile, finder, false));
		}

		array.print(writer);
	}

}

package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

import com.velik.comments.Finder;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;

public class ProfileJsonWrapper extends JsonObject {

	private Profile profile;
	private Finder finder;
	private boolean includeFavorites;

	public ProfileJsonWrapper(Profile profile, Finder finder) {
		this(profile, finder, true);
	}

	public ProfileJsonWrapper(Profile profile, Finder finder, boolean includeFavorites) {
		this.profile = profile;
		this.finder = finder;
		this.includeFavorites = includeFavorites;
	}

	@Override
	public void print(Writer writer) throws IOException {
		JsonArray json = new JsonArray();

		if (!profile.isAnonymous()) {
			json.add(profile.getId().getIntegerId());
			json.add(profile.getAlias());

			if (includeFavorites) {
				JsonArray favorites = new JsonArray();

				for (ProfileId favoriteId : profile.getFavoritesAsSet()) {
					favorites.add(new ProfileJsonWrapper(finder.getProfile(favoriteId), finder, false));
				}

				json.add(favorites);
			}
		}

		json.print(writer);
	}

}

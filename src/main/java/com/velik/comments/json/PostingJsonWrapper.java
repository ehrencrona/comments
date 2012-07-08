package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

import com.velik.comments.Posting;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.pojo.ProfileSetPojo;

public abstract class PostingJsonWrapper<P extends Posting> extends JsonObject {
	protected P posting;
	protected Profile profile;

	protected ProfileSet profileCollector = new ProfileSetPojo();

	public PostingJsonWrapper(P posting, Profile profile) {
		this.posting = posting;
		this.profile = profile;
	}

	@Override
	public void print(Writer writer) throws IOException {
		toJsonObject().print(writer);
	}

	protected JsonArray toJsonObject() {
		JsonArray result = new JsonArray(posting.getId());

		PostingSize size = getSize();

		if (size != PostingSize.HIDDEN) {
			result.add(size);

			// TODO would be nice to skip this when the posting is already
			// expanded. however, if we are retrieving the full comment list
			// this has to be set even if the posting size if full size we could
			// go from hidden to collapsed to full.
			result.add(posting.getSummarizedText());

			if (size == PostingSize.FULL) {
				result.add(posting.getText());
			} else {
				result.add("");
			}

			result.add(posting.getPosterId().getIntegerId());

			if (size == PostingSize.FULL) {
				ProfileSet likers = posting.getLikers();
				ProfileSet favoriteLikers = likers.intersection(profile.getFavoritesAsSet());

				if (likers.contains(profile.getId())) {
					favoriteLikers.add(profile.getId());
				}

				if (profileCollector != null) {
					for (ProfileId likerId : favoriteLikers) {
						profileCollector.add(likerId);
					}
				}

				result.add(array(new ProfileIdSetJsonWrapper(favoriteLikers), likers.size() - favoriteLikers.size()));
			} else if (getSize() != PostingSize.HIDDEN) {
				result.add("");
			}

			if (size != PostingSize.HIDDEN) {
				if (profileCollector != null) {
					profileCollector.add(posting.getPosterId());
				}
			}
		}

		return result;
	}

	protected abstract PostingSize getSize();

	public void setProfileCollector(ProfileSet profileSet) {
		this.profileCollector = profileSet;
	}

	public ProfileSet getProfileCollector() {
		return profileCollector;
	}
}

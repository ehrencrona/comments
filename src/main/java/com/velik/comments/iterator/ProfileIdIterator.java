package com.velik.comments.iterator;

import java.util.Iterator;

import com.velik.comments.Finder;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.exception.NoSuchProfileException;

public class ProfileIdIterator extends AbstractIdIterator<ProfileId, Profile> {

	public ProfileIdIterator(Iterator<ProfileId> delegate, Finder finder) {
		super(delegate, finder);
	}

	@Override
	protected Profile getObject(ProfileId profileId) throws NoSuchProfileException {
		return finder.getProfile(profileId);
	}

}

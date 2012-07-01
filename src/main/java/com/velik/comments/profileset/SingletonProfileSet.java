package com.velik.comments.profileset;

import java.util.Collections;
import java.util.Iterator;

import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;

public class SingletonProfileSet implements ProfileSet {
	private ProfileId profileId;

	public SingletonProfileSet(ProfileId profileId) {
		this.profileId = profileId;
	}

	@Override
	public Iterator<ProfileId> iterator() {
		return Collections.singletonList(profileId).iterator();
	}

	@Override
	public boolean intersects(ProfileSet favorites) {
		return favorites.contains(profileId);
	}

	@Override
	public boolean contains(ProfileId profile) {
		return profileId.equals(profile);
	}

	@Override
	public ProfileSet intersection(ProfileSet favorites) {
		if (favorites.contains(profileId)) {
			return this;
		} else {
			return new EmptyProfileSet();
		}
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public void add(ProfileId profileId) {
		throw new UnsupportedOperationException();
	}

}

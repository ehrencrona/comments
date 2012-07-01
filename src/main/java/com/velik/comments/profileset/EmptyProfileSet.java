package com.velik.comments.profileset;

import java.util.Collections;
import java.util.Iterator;

import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;

public class EmptyProfileSet implements ProfileSet {

	@Override
	public Iterator<ProfileId> iterator() {
		return Collections.<ProfileId> emptyList().iterator();
	}

	@Override
	public boolean intersects(ProfileSet favorites) {
		return false;
	}

	@Override
	public boolean contains(ProfileId profile) {
		return false;
	}

	@Override
	public ProfileSet intersection(ProfileSet favorites) {
		return this;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public void add(ProfileId profileId) {
		throw new UnsupportedOperationException();
	}

}

package com.velik.comments.profileset;

import java.util.Iterator;

import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;

public class LazyUnionProfileSet implements ProfileSet {
	private ProfileSet set1;
	private ProfileSet set2;

	public LazyUnionProfileSet(ProfileSet set1, ProfileSet set2) {
		this.set1 = set1;
		this.set2 = set2;
	}

	@Override
	public Iterator<ProfileId> iterator() {
		// TODO implement.
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean intersects(ProfileSet profileSet) {
		return set1.intersects(profileSet) || set2.intersects(profileSet);
	}

	@Override
	public boolean contains(ProfileId profile) {
		return set1.contains(profile) || set2.contains(profile);
	}

	@Override
	public ProfileSet intersection(ProfileSet favorites) {
		return new LazyUnionProfileSet(favorites.intersection(set1), favorites.intersection(set2));
	}

	@Override
	public int size() {
		return set1.size() + set2.size();
	}

	@Override
	public void add(ProfileId profileId) {
		throw new UnsupportedOperationException();
	}

}

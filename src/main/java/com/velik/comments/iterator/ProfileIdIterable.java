package com.velik.comments.iterator;

import java.util.Iterator;

import com.velik.comments.Finder;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;

public class ProfileIdIterable implements Iterable<Profile> {
	private Iterable<ProfileId> delegate;
	private Finder finder;

	public ProfileIdIterable(Iterable<ProfileId> delegate, Finder finder) {
		this.delegate = delegate;
		this.finder = finder;
	}

	@Override
	public Iterator<Profile> iterator() {
		return new ProfileIdIterator(delegate.iterator(), finder);
	}

}

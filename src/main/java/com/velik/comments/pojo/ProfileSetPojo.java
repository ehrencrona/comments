package com.velik.comments.pojo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.velik.comments.Finder;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;

public class ProfileSetPojo implements ProfileSet {
	private Set<ProfileId> idSet;
	private Finder finder;

	public ProfileSetPojo(Finder finder) {
		this(new HashSet<ProfileId>(), finder);
	}

	public ProfileSetPojo(Set<ProfileId> idSet, Finder finder) {
		this.idSet = idSet;
		this.finder = finder;
	}

	public void add(ProfileId profileId) {
		idSet.add(profileId);
	}

	private Set<ProfileId> getProfileIdSet() {
		return idSet;
	}

	public ProfileSet intersection(ProfileSet profileSet) {
		HashSet<ProfileId> resultSet = new HashSet<ProfileId>(idSet);

		resultSet.retainAll(((ProfileSetPojo) profileSet).getProfileIdSet());

		return new ProfileSetPojo(resultSet, finder);
	}

	@Override
	public Iterator<ProfileId> iterator() {
		return idSet.iterator();
	}

	@Override
	public boolean contains(ProfileId profile) {
		return idSet.contains(profile);
	}

}

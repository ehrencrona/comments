package com.velik.comments.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;

public class ProfileSetPojo implements ProfileSet, Serializable {
	private static final long serialVersionUID = 1;
	private Set<ProfileId> idSet;

	public ProfileSetPojo() {
		this(new HashSet<ProfileId>());
	}

	public ProfileSetPojo(Set<ProfileId> idSet) {
		this.idSet = idSet;
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

		return new ProfileSetPojo(resultSet);
	}

	@Override
	public Iterator<ProfileId> iterator() {
		return idSet.iterator();
	}

	@Override
	public boolean contains(ProfileId profile) {
		return idSet.contains(profile);
	}

	@Override
	public boolean intersects(ProfileSet favorites) {
		for (ProfileId favorite : this) {
			if (favorites.contains(favorite)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int size() {
		return idSet.size();
	}

	public String toString() {
		StringBuffer result = new StringBuffer(100);

		boolean first = true;

		for (ProfileId profileId : this) {
			if (!first) {
				result.append(", ");
			} else {
				first = false;
			}

			result.append(profileId.toString());
		}

		return result.toString();
	}

}

package com.velik.comments;

public interface ProfileSet extends Iterable<ProfileId> {

	boolean intersects(ProfileSet favorites);

	boolean contains(ProfileId profile);

	ProfileSet intersection(ProfileSet favorites);

	int size();

	void add(ProfileId profileId);

}

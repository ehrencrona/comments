package com.velik.comments;

public interface ProfileSet extends Iterable<ProfileId> {

	boolean contains(ProfileId profile);

}

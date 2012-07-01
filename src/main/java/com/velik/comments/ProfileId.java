package com.velik.comments;

public class ProfileId extends Id {
	public static ProfileId ANONYMOUS = new ProfileId(-1);

	public ProfileId(int id) {
		super(id);
	}

	public boolean isAnonymous() {
		return this.equals(ANONYMOUS);
	}

}

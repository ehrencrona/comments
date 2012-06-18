package com.velik.comments;

public interface Profile extends ModelObject<ProfileId>, Valued {

	ProfileId getId();

	String getAlias();

	/**
	 * Those who have this profile as favorite.
	 */
	ProfileSet getFavoriteOf();

	ProfileSet getFavorites();

	void addFavorite(ProfileId profile);

	int getPoints();

	/**
	 * New postings in any comment I have posted in or replied to plus any
	 * postings by friends
	 */
	Iterable<Posting> getNewsfeed(int count);

	Iterable<Posting> getOwnPostings();

	Iterable<Valuation> getReceivedValuations(long since);

	Iterable<Valuation> getGivenValuations();

}

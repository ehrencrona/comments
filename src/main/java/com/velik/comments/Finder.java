package com.velik.comments;

import com.velik.comments.exception.NoSuchCommentListException;
import com.velik.comments.exception.NoSuchPostingException;
import com.velik.comments.exception.NoSuchProfileException;
import com.velik.comments.exception.NoSuchValuationException;

public interface Finder {

	/**
	 * If the profileId is ANONYMOUS or the profile does not exist, the
	 * anonymous profile is returned.
	 */
	Profile getProfile(ProfileId profileId);

	/**
	 * TODO Why does this method throw an exception rather than returning
	 * anonymous when getProfile(ProfileId) is different? Any reason getPosting
	 * shouldn't do the same?
	 */
	ProfileId getProfile(String alias) throws NoSuchProfileException;

	Iterable<Profile> getProfiles();

	Posting getPosting(PostingId postingId) throws NoSuchPostingException;

	Valuation getValuation(ValuationId valuationId) throws NoSuchValuationException;

	Profile createProfile(String alias);

	CommentList getCommentList(CommentListId commentListId) throws NoSuchCommentListException;

	CommentList createCommentList(CommentListId id);

	void persist();

	void initalize();
}

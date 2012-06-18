package com.velik.comments;

import com.velik.comments.exception.NoSuchCommentListException;
import com.velik.comments.exception.NoSuchPostingException;
import com.velik.comments.exception.NoSuchProfileException;
import com.velik.comments.exception.NoSuchValuationException;

public interface Finder {

	Profile getProfile(ProfileId profileId) throws NoSuchProfileException;

	Posting getPosting(PostingId postingId) throws NoSuchPostingException;

	Valuation getValuation(ValuationId valuationId) throws NoSuchValuationException;

	Profile createProfile(String alias);

	CommentList getCommentList(CommentListId commentListId) throws NoSuchCommentListException;

	CommentList createCommentList(CommentListId id);

}

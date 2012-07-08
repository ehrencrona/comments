package com.velik.comments.pojo;

import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationId;
import com.velik.comments.exception.NoSuchCommentListException;
import com.velik.comments.exception.NoSuchPostingException;
import com.velik.comments.exception.NoSuchProfileException;
import com.velik.comments.exception.NoSuchValuationException;

public class DelegatingFinder implements Finder {
	protected Finder delegate;

	public Profile getProfile(ProfileId profileId) {
		return delegate.getProfile(profileId);
	}

	public ProfileId getProfile(String alias) throws NoSuchProfileException {
		return delegate.getProfile(alias);
	}

	public Posting getPosting(PostingId postingId) throws NoSuchPostingException {
		return delegate.getPosting(postingId);
	}

	public Valuation getValuation(ValuationId valuationId) throws NoSuchValuationException {
		return delegate.getValuation(valuationId);
	}

	public Profile createProfile(String alias) {
		return delegate.createProfile(alias);
	}

	public CommentList getCommentList(CommentListId commentListId) throws NoSuchCommentListException {
		return delegate.getCommentList(commentListId);
	}

	public CommentList createCommentList(CommentListId id) {
		return delegate.createCommentList(id);
	}

	public void persist() {
		delegate.persist();
	}

	public void initalize() {
		delegate.initalize();
	}

	@Override
	public Iterable<Profile> getProfiles() {
		return delegate.getProfiles();
	}

}

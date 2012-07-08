package com.velik.comments.mustache;

import java.util.Collections;
import java.util.Date;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.Reply;
import com.velik.comments.delegate.DelegatingPosting;

public class PostingMustacheWrapper extends DelegatingPosting {
	private Finder finder;

	public PostingMustacheWrapper(Posting posting, Finder finder) {
		super(posting);

		this.finder = finder;
	}

	@Override
	public Date getDate() {
		return new DateMustacheWrapper(super.getDate());
	}

	public ProfileMustacheWrapper getPoster() {
		return new ProfileMustacheWrapper(finder.getProfile(getPosterId()), finder);
	}

	public boolean isHidden() {
		return false;
	}

	public boolean isFull() {
		return true;
	}

	/**
	 * TODO Not a proper solution. Helps the profile template not show any
	 * replies in the list of comments.
	 */
	public Iterable<Reply> getReplies() {
		return Collections.emptyList();
	}

}

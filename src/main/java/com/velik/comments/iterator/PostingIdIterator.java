package com.velik.comments.iterator;

import java.util.Iterator;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.exception.NoSuchObjectException;

public class PostingIdIterator<P extends Posting> extends AbstractIdIterator<PostingId, P> {

	public PostingIdIterator(Iterator<PostingId> delegate, Finder finder) {
		super(delegate, finder);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected P getObject(PostingId postingId) throws NoSuchObjectException {
		return (P) finder.getPosting(postingId);
	}

}

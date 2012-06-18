package com.velik.comments.iterator;

import java.util.Iterator;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;

public class PostingIdIterable<P extends Posting> implements Iterable<P> {
	private Iterable<PostingId> delegate;
	private Finder finder;

	public PostingIdIterable(Iterable<PostingId> delegate, Finder finder) {
		this.delegate = delegate;
		this.finder = finder;
	}

	@Override
	public Iterator<P> iterator() {
		return new PostingIdIterator<P>(delegate.iterator(), finder);
	}

}

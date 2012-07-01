package com.velik.comments.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.PostingList;
import com.velik.comments.iterator.PostingIdIterator;

public class PostingListPojo<P extends Posting> implements PostingList<P> {
	private static final long serialVersionUID = 1;

	private List<PostingId> postings = new ArrayList<PostingId>();
	private Finder finder;
	private Set<PostingId> idSetCache;

	public PostingListPojo(Finder finder) {
		this.finder = finder;
	}

	@Override
	public Iterator<P> iterator() {
		return new PostingIdIterator<P>(postings.iterator(), finder);
	}

	public void add(PostingId postingId) {
		postings.add(postingId);

		if (idSetCache != null) {
			idSetCache.add(postingId);
		}
	}

	@Override
	public boolean contains(PostingId id) {
		return getIdSet().contains(id);
	}

	private Set<PostingId> getIdSet() {
		if (idSetCache == null) {
			idSetCache = new HashSet<PostingId>(postings);
		}

		return idSetCache;
	}

}

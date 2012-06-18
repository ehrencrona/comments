package com.velik.comments;

public interface PostingList<T extends Posting> extends Iterable<T> {
	boolean contains(PostingId id);
}

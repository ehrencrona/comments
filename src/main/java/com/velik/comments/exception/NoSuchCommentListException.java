package com.velik.comments.exception;

public class NoSuchCommentListException extends NoSuchObjectException {

	public NoSuchCommentListException(NoSuchObjectException cause) {
		super(cause);
	}

}

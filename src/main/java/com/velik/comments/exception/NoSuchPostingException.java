package com.velik.comments.exception;

public class NoSuchPostingException extends NoSuchObjectException {

	public NoSuchPostingException(NoSuchObjectException cause) {
		super(cause);
	}

	public NoSuchPostingException(String message) {
		super(message);
	}

}

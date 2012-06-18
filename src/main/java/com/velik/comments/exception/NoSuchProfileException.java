package com.velik.comments.exception;

public class NoSuchProfileException extends NoSuchObjectException {

	public NoSuchProfileException(NoSuchObjectException cause) {
		super(cause);
	}

}

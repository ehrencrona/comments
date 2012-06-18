package com.velik.comments.exception;

public class NoSuchObjectException extends Exception {

	public NoSuchObjectException(String message) {
		super(message);
	}

	public NoSuchObjectException(NoSuchObjectException cause) {
		this(cause.getMessage());
	}

}

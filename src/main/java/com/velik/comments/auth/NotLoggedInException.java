package com.velik.comments.auth;

public class NotLoggedInException extends Exception {

	public NotLoggedInException() {
		super();
	}

	public NotLoggedInException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NotLoggedInException(String arg0) {
		super(arg0);
	}

	public NotLoggedInException(Throwable arg0) {
		super(arg0);
	}

}

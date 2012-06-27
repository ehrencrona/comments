package com.velik.comments.auth;

public class LoginFailureException extends Exception {

	public LoginFailureException() {
		super();
	}

	public LoginFailureException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LoginFailureException(String arg0) {
		super(arg0);
	}

	public LoginFailureException(Throwable arg0) {
		super(arg0);
	}

}

package com.velik.comments.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.ProfileId;

public interface AuthenticationService {

	/**
	 * Returns {@link ProfileId#ANONYMOUS} if no user is logged in.
	 */
	ProfileId getLoggedInUser(HttpServletRequest request);

	ProfileId login(String alias, String password, HttpServletRequest request, HttpServletResponse response)
			throws LoginFailureException;

	void logout(HttpServletRequest request, HttpServletResponse response);

}

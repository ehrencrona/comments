package com.velik.comments.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.auth.LoginFailureException;
import com.velik.comments.json.JsonMap;
import com.velik.comments.json.JsonObject;
import com.velik.comments.json.NoSuchValueException;
import com.velik.comments.json.ParseException;
import com.velik.comments.json.ProfileJsonWrapper;
import com.velik.comments.util.CheckedCast;
import com.velik.comments.util.CheckedClassCastException;

public class AuthenticationServlet extends AbstractHttpServlet {
	private static final Logger LOGGER = Logger.getLogger(AuthenticationServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProfileId profileId = authenticationService.getLoggedInUser(request);
		Profile profile = finder.getProfile(profileId);

		respondWithJson(new ProfileJsonWrapper(profile, finder, true), response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		try {
			respondWithJson(doPost(parsePostedJson(request), request, response), response);
		} catch (ParseException e) {
			errorResponse("Invalid JSON posted: " + e.toString(), HttpServletResponse.SC_BAD_REQUEST, request, response);
		}
	}

	private JsonObject doPost(Object postedJson, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if ("/logout".equals(request.getPathInfo())) {
			authenticationService.logout(request, response);

			return new ProfileJsonWrapper(finder.getProfile(ProfileId.ANONYMOUS), finder, false);
		}

		try {
			JsonMap map = CheckedCast.cast(postedJson, JsonMap.class);

			String alias = CheckedCast.cast(map.get("user"), String.class, "user value");
			String password = CheckedCast.cast(map.get("password"), String.class, "password value");

			ProfileId profileId = authenticationService.login(alias, password, request, response);

			Profile profile = finder.getProfile(profileId);

			if (profile.isAnonymous()) {
				errorResponse("Unknown alias.", HttpServletResponse.SC_UNAUTHORIZED, request, response);
				return null;
			}

			LOGGER.log(Level.INFO, "Logged in " + alias + ".");

			return new ProfileJsonWrapper(profile, finder, false);
		} catch (LoginFailureException e) {
			errorResponse("Login failed: " + e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED, request, response);
		} catch (CheckedClassCastException e) {
			errorResponse("Invalid JSON posted: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, request,
					response);
		} catch (NoSuchValueException e) {
			errorResponse("Invalid JSON posted: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, request,
					response);
		}

		return null;
	}

}

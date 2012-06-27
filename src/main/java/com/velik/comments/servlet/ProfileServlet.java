package com.velik.comments.servlet;

import static com.velik.comments.util.CheckedCast.cast;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.json.JsonArray;
import com.velik.comments.json.JsonMap;
import com.velik.comments.json.NoSuchValueException;
import com.velik.comments.json.ParseException;
import com.velik.comments.json.ProfileJsonWrapper;
import com.velik.comments.util.CheckedCast;
import com.velik.comments.util.CheckedClassCastException;

public class ProfileServlet extends AbstractHttpServlet {
	private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null) {
			pathInfo = "";
		}

		if (pathInfo.startsWith("/")) {
			pathInfo = pathInfo.substring(1);
		}

		if (pathInfo.endsWith(".json")) {
			pathInfo = pathInfo.substring(0, pathInfo.length() - 5);
		}

		String[] pathSegments = pathInfo.split("/");

		ProfileId profileId;

		if (pathSegments.length == 1 && pathSegments[0].equals("")) {
			profileId = authenticationService.getLoggedInUser(request);
		} else if (pathSegments.length == 1) {
			try {
				profileId = new ProfileId(Integer.parseInt(pathSegments[0]));
			} catch (NumberFormatException e) {
				errorResponse("Expected profile ID to be a number.", SC_BAD_REQUEST, request, response);
				return;
			}
		} else {
			errorResponse("Should specify profile ID as single path segment.", SC_NOT_FOUND, request, response);
			return;
		}

		Profile profile = finder.getProfile(profileId);

		if (profile.isAnonymous()) {
			errorResponse("Unknown profile ID " + profileId + ".", SC_NOT_FOUND, request, response);
			return;
		}

		if (request.getMethod().equals("GET")) {
			doGet(profile, request, response);
		} else if (request.getMethod().equals("POST")) {
			doPost(profile, request, response);
		} else {
			errorResponse("Unsupported method " + request.getMethod(), SC_BAD_REQUEST, request, response);
			return;
		}
	}

	private void doPost(Profile profile, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		ProfileId currentProfileId = authenticationService.getLoggedInUser(request);

		if (!currentProfileId.equals(profile.getId())) {
			errorResponse("You are not allowed to modify someone else's profile.", SC_BAD_REQUEST, request, response);
			return;
		}

		try {
			JsonMap map = CheckedCast.cast(parsePostedJson(request), JsonMap.class, "top object");

			JsonArray favorites = CheckedCast.cast(map.get("favorites"), JsonArray.class, "favorites entry");

			for (Object favorite : favorites) {
				Integer favoriteId = cast(cast(favorite, JsonMap.class, "item in favorites array").get("id"),
						Integer.class, "ID entry in favorite");

				profile.addFavorite(new ProfileId(favoriteId));

				LOGGER.log(Level.INFO, "Added " + favoriteId + " as favorite of " + profile + ".");
			}
		} catch (CheckedClassCastException e) {
			errorResponse("Invalid JSON posted: " + e.getMessage(), SC_NOT_FOUND, request, response);
			return;
		} catch (ParseException e) {
			errorResponse("Invalid JSON posted: " + e.getMessage(), SC_NOT_FOUND, request, response);
			return;
		} catch (NoSuchValueException e) {
			errorResponse("Invalid JSON posted: " + e.getMessage(), SC_NOT_FOUND, request, response);
			return;
		}

		doGet(profile, request, response);
	}

	private void doGet(Profile profile, HttpServletRequest request, HttpServletResponse response) throws IOException {
		respondWithJson(new ProfileJsonWrapper(profile, finder), response);
	}
}

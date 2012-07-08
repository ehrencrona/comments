package com.velik.comments.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.Finder;
import com.velik.comments.ProfileId;
import com.velik.comments.exception.NoSuchProfileException;

public class DummyAuthenticationService implements AuthenticationService {
	private static final Logger LOGGER = Logger.getLogger(DummyAuthenticationService.class.getName());
	private static final String LOGIN_COOKIE = "cl"; // "comments login"

	private static final Random RANDOM = new Random();

	private Map<ProfileId, Long> securityTokenById = new HashMap<ProfileId, Long>();

	private Finder finder;
	private boolean completelyUnsafe;

	public DummyAuthenticationService(Finder finder, boolean completelyUnsafe) {
		this.finder = finder;
		this.completelyUnsafe = completelyUnsafe;
	}

	@Override
	public ProfileId getLoggedInUser(HttpServletRequest request) {
		try {
			String cookieValue = getLoginCookie(request);

			int separator = cookieValue.lastIndexOf(':');

			String alias;
			Long providedToken;

			if (separator > 0) {
				alias = cookieValue.substring(0, separator);
				try {
					providedToken = Long.parseLong(cookieValue.substring(separator + 1), 16);
				} catch (NumberFormatException e) {
					String message = "Invalid format of login cookie \"" + cookieValue
							+ "\". Expected it to end with a number.";

					throw loginVerificationFailure(message);
				}
			} else {
				String message = "Invalid format of login cookie \"" + cookieValue + "\".";

				throw loginVerificationFailure(message);
			}

			ProfileId profileId;

			try {
				profileId = finder.getProfile(alias);
			} catch (NoSuchProfileException e) {
				throw loginVerificationFailure("Unknown user \"" + alias + "\".");
			}

			if (!completelyUnsafe) {
				Long storedToken = securityTokenById.get(profileId);

				if (storedToken == null) {
					throw loginVerificationFailure("No token stored for " + profileId + " (" + alias + ")");
				}

				if (!storedToken.equals(providedToken)) {
					throw loginVerificationFailure("Provided token did not match stored for " + alias + ".");
				}
			}

			return profileId;
		} catch (NotLoggedInException e) {
			return ProfileId.ANONYMOUS;
		}
	}

	protected NotLoggedInException loginVerificationFailure(String message) throws NotLoggedInException {
		LOGGER.log(Level.WARNING, message);

		return new NotLoggedInException(message);
	}

	protected String getLoginCookie(HttpServletRequest request) throws NotLoggedInException {
		String cookieValue = null;

		for (Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(LOGIN_COOKIE)) {
				if (cookieValue != null) {
					LOGGER.log(Level.WARNING,
							"Double login cookies: " + cookieValue + " and " + cookie.getValue() + ".");
				}

				cookieValue = cookie.getValue();
			}
		}

		if (cookieValue == null) {
			throw new NotLoggedInException("No login cookie found.");
		}

		return cookieValue;
	}

	@Override
	public ProfileId login(String alias, String password, HttpServletRequest request,
			HttpServletResponse response) throws LoginFailureException {
		if (!alias.equals(password)) {
			throw new LoginFailureException("Password not matching.");
		}

		ProfileId profileId;

		try {
			profileId = finder.getProfile(alias);
		} catch (NoSuchProfileException e) {
			throw new LoginFailureException("Unknown user: " + e.getMessage());
		}

		Long securityToken = generateSecurityToken();

		securityTokenById.put(profileId, securityToken);

		Cookie cookie = new Cookie(LOGIN_COOKIE, alias + ":" + Long.toHexString(securityToken));
		cookie.setPath("/");
		cookie.setMaxAge(-1);

		response.addCookie(cookie);

		return profileId;
	}

	private Long generateSecurityToken() {
		return Math.abs(RANDOM.nextLong());
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie(LOGIN_COOKIE, "-");
		cookie.setPath("/");
		cookie.setMaxAge(0);

		response.addCookie(cookie);
	}
}

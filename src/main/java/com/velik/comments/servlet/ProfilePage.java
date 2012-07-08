package com.velik.comments.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.exception.NoSuchProfileException;
import com.velik.comments.json.JsonMap;
import com.velik.comments.mustache.ProfileMustacheWrapper;
import com.velik.comments.mustache.json.JsonObjectHandler;

public class ProfilePage extends AbstractHttpService {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null) {
			pathInfo = "";
		}

		if (pathInfo.startsWith("/")) {
			pathInfo = pathInfo.substring(1);
		}

		response.setCharacterEncoding("UTF-8");

		try {
			ProfileId profileId = finder.getProfile(pathInfo);
			Profile profile = finder.getProfile(profileId);

			DefaultMustacheFactory mustacheFactory = new DefaultMustacheFactory(new File(getServletContext()
					.getRealPath("/ms")));

			mustacheFactory.setObjectHandler(new JsonObjectHandler());

			JsonMap headerContext = new JsonMap().put("contextPath", request.getContextPath());

			render("header.ms", response, mustacheFactory, headerContext);

			render("profile.ms", response, mustacheFactory, new ProfileMustacheWrapper(profile, finder));

			render("footer.ms", response, mustacheFactory, headerContext);
		} catch (NoSuchProfileException e) {
			errorResponse("Unknown profile \"" + pathInfo + "\".", HttpServletResponse.SC_NOT_FOUND, request,
					response);
		}

	}

	private void render(String templateName, HttpServletResponse response, MustacheFactory mustacheFactory,
			Object context) throws IOException {
		// TODO Don't recompile every time.
		Mustache header = mustacheFactory.compile(templateName);

		header.execute(response.getWriter(), context);
	}
}

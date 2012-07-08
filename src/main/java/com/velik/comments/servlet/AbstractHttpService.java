package com.velik.comments.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.Finder;
import com.velik.comments.auth.AuthenticationService;
import com.velik.comments.auth.DummyAuthenticationService;
import com.velik.comments.json.JsonObject;
import com.velik.comments.json.JsonParser;
import com.velik.comments.json.ParseException;
import com.velik.comments.pojo.SerializedPojoFinder;

public class AbstractHttpService extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(AbstractHttpService.class.getName());

	/**
	 * TODO Replace by dependency injection.
	 */
	static Finder finder = new SerializedPojoFinder();
	static AuthenticationService authenticationService = new DummyAuthenticationService(finder, true);

	protected void respondWithJson(JsonObject json, HttpServletResponse response) throws IOException {
		if (json == null) {
			return;
		}

		OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		json.print(writer);

		writer.close();
	}

	protected Object parsePostedJson(HttpServletRequest request) throws ServletException, IOException, ParseException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

		StringBuffer input = new StringBuffer(2000);

		String line;

		while ((line = reader.readLine()) != null) {
			input.append(line);
		}

		return new JsonParser(input.toString()).parse();
	}

	protected void errorResponse(String message, int responseCode, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LOGGER.log(Level.WARNING, "For " + request.getRequestURI() + ": " + message);

		response.setStatus(responseCode);

		response.getOutputStream().println(message);
	}
}

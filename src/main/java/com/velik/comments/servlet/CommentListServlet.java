package com.velik.comments.servlet;

import static com.velik.comments.util.CheckedCast.cast;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.Profile;
import com.velik.comments.exception.NoSuchCommentListException;
import com.velik.comments.exception.NoSuchPostingException;
import com.velik.comments.json.CommentListJsonWrapper;
import com.velik.comments.json.JsonMap;
import com.velik.comments.json.JsonParser;
import com.velik.comments.json.NoSuchValueException;
import com.velik.comments.json.ParseException;
import com.velik.comments.json.PostingJsonWrapper;
import com.velik.comments.pojo.FinderPojo;
import com.velik.comments.util.CheckedCast;
import com.velik.comments.util.CheckedClassCastException;

public class CommentListServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(CommentListServlet.class.getName());

	private static Finder finder = new FinderPojo();

	static {
		finder.createCommentList(new CommentListId("singleton"));
	}

	private static Profile profile = finder.createProfile("sysadmin");

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo.startsWith("/")) {
			pathInfo = pathInfo.substring(1);
		}

		if (pathInfo.endsWith(".json")) {
			pathInfo = pathInfo.substring(0, pathInfo.length() - 5);
		}

		String[] pathSegments = pathInfo.split("/");

		if (pathSegments.length == 0 || pathSegments.length > 3) {
			errorResponse("Should specify comment list ID and optionally posting ID as path segment.", SC_NOT_FOUND,
					request, response);
		}

		CommentList commentList;
		Comment comment = null;

		try {
			commentList = finder.getCommentList(new CommentListId(pathSegments[0]));
		} catch (NoSuchCommentListException e) {
			errorResponse(e.toString(), SC_NOT_FOUND, request, response);
			return;
		}

		if (pathSegments.length > 1) {
			try {
				comment = CheckedCast.cast(finder.getPosting(new PostingId(Integer.parseInt(pathSegments[1]))),
						Comment.class);
			} catch (NumberFormatException e) {
				errorResponse(e.toString(), SC_NOT_FOUND, request, response);
				return;
			} catch (NoSuchPostingException e) {
				errorResponse(e.toString(), SC_NOT_FOUND, request, response);
				return;
			} catch (CheckedClassCastException e) {
				errorResponse(e.toString(), SC_NOT_FOUND, request, response);
				return;
			}
		}

		if (request.getMethod().equals("POST")) {
			doPost(request, response, commentList, comment);
		} else {
			doGet(request, response, commentList, comment);
		}
	}

	private void errorResponse(String message, int responseCode, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LOGGER.log(Level.WARNING, "For " + request.getRequestURI() + ": " + message);

		response.setStatus(responseCode);

		response.getOutputStream().println(message);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response, CommentList commentList,
			Comment comment) throws ServletException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

		StringBuffer input = new StringBuffer(2000);

		String line;

		while ((line = reader.readLine()) != null) {
			input.append(line);
		}

		try {
			JsonMap json = cast(new JsonParser(input.toString()).parse(), JsonMap.class);

			String text = cast(json.get("text"), String.class);

			Posting newPosting;

			if (comment != null) {
				LOGGER.log(Level.WARNING, "Added reply \"" + text + "\" by " + profile + " to " + comment + " in "
						+ commentList + ".");

				newPosting = comment.reply(text, profile.getId());
			} else {
				newPosting = commentList.comment(text, profile.getId());

				LOGGER.log(Level.WARNING, "Added comment " + newPosting + " to " + commentList + ".");
			}

			Writer writer = new OutputStreamWriter(response.getOutputStream());

			new PostingJsonWrapper<Posting>(newPosting).print(writer);

			writer.close();
		} catch (ParseException e) {
			errorResponse(e.toString() + " Expecting a JSON map.", SC_BAD_REQUEST, request, response);
		} catch (CheckedClassCastException e) {
			errorResponse(e.toString(), SC_BAD_REQUEST, request, response);
		} catch (NoSuchValueException e) {
			errorResponse(e.toString() + " Value \"text\" should be the text to post.", SC_BAD_REQUEST, request,
					response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response, CommentList commentList,
			Comment comment) throws ServletException, IOException {
		OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		new CommentListJsonWrapper(commentList, profile).print(writer);

		writer.close();
	}

}

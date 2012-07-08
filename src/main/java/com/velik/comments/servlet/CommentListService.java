package com.velik.comments.servlet;

import static com.velik.comments.json.PostingSize.FULL;
import static com.velik.comments.util.CheckedCast.cast;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.ValuationType;
import com.velik.comments.exception.NoSuchCommentListException;
import com.velik.comments.exception.NoSuchPostingException;
import com.velik.comments.json.CommentListJsonWrapper;
import com.velik.comments.json.JsonArray;
import com.velik.comments.json.JsonMap;
import com.velik.comments.json.JsonObject;
import com.velik.comments.json.NoSuchValueException;
import com.velik.comments.json.ParseException;
import com.velik.comments.json.PostingJsonWrapper;
import com.velik.comments.json.PostingSize;
import com.velik.comments.json.ProfileJsonWrapper;
import com.velik.comments.json.ProfileSetJsonWrapper;
import com.velik.comments.size.CommentSizeCalculator;
import com.velik.comments.size.DefaultCommentSizeCalculator;
import com.velik.comments.size.FixedCommentSizeCalculator;
import com.velik.comments.util.CheckedCast;
import com.velik.comments.util.CheckedClassCastException;

public class CommentListService extends AbstractHttpService {
	private static final Logger LOGGER = Logger.getLogger(CommentListService.class.getName());

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String pathInfo = request.getPathInfo();

		boolean full = false;

		if (pathInfo.startsWith("/full")) {
			full = true;
			pathInfo = pathInfo.substring(5);
		}

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
		Posting posting = null;

		CommentListId commentListId = new CommentListId(pathSegments[0]);

		try {
			commentList = finder.getCommentList(commentListId);
		} catch (NoSuchCommentListException e) {
			commentList = finder.createCommentList(commentListId);
		}

		if (pathSegments.length > 1) {
			try {
				posting = CheckedCast.cast(finder.getPosting(new PostingId(Integer.parseInt(pathSegments[1]))),
						Posting.class);
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

		ProfileId profileId = authenticationService.getLoggedInUser(request);

		if (request.getMethod().equals("POST")) {
			doPost(request, response, commentList, posting, profileId);
		} else {
			doGet(request, response, commentList, posting, profileId, full);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response, CommentList commentList,
			Posting posting, ProfileId profileId) throws ServletException, IOException {
		try {
			Object jsonObject = parsePostedJson(request);
			JsonMap json = cast(jsonObject, JsonMap.class);

			Posting replyPosting = null;

			try {
				if (posting == null || posting instanceof Comment) {
					replyPosting = postNewPosting(commentList, (Comment) posting, profileId, json);
				}
			} catch (NoSuchValueException e) {
				// fine. not a post.
			}

			try {
				json.get("like");

				Profile profile = finder.getProfile(profileId);

				posting.value(ValuationType.LIKE, profile.getPoints(), profileId);

				replyPosting = posting;
			} catch (NoSuchValueException e) {
				// fine. not a like.
			}

			if (replyPosting == null) {
				errorResponse("Either value \"text\" should be the text to post or \"like\" should be set to true.",
						SC_BAD_REQUEST, request, response);
				return;
			}

			doGet(request, response, commentList, replyPosting, profileId, true);
		} catch (ParseException e) {
			errorResponse(e.toString() + " Expecting a JSON map.", SC_BAD_REQUEST, request, response);
		} catch (CheckedClassCastException e) {
			errorResponse(e.toString(), SC_BAD_REQUEST, request, response);
		}
	}

	protected Posting postNewPosting(CommentList commentList, Comment comment, ProfileId profileId, JsonMap json)
			throws CheckedClassCastException, NoSuchValueException {
		String text = cast(json.get("text"), String.class);

		Posting newPosting;

		if (comment != null) {
			LOGGER.log(Level.WARNING, "Added reply \"" + text + "\" by " + profileId + " to " + comment + " in "
					+ commentList + ".");

			newPosting = comment.reply(text, profileId);
		} else {
			newPosting = commentList.comment(text, profileId);

			LOGGER.log(Level.WARNING, "Added comment " + newPosting + " to " + commentList + ".");
		}

		return newPosting;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response, CommentList commentList,
			final Posting comment, ProfileId profileId, boolean full) throws ServletException, IOException {
		Profile profile = finder.getProfile(profileId);

		ProfileSet profileIdSet;
		JsonObject commentJson;

		if (comment == null) {
			CommentSizeCalculator favoriteSizeCalculator;
			CommentSizeCalculator nonFavoriteSizeCalculator;

			if (full) {
				favoriteSizeCalculator = new FixedCommentSizeCalculator(PostingSize.FULL);
				nonFavoriteSizeCalculator = favoriteSizeCalculator;
			} else {
				favoriteSizeCalculator = new DefaultCommentSizeCalculator(true);
				nonFavoriteSizeCalculator = new DefaultCommentSizeCalculator(false);
			}

			CommentListJsonWrapper wrapper = new CommentListJsonWrapper(commentList, profile, favoriteSizeCalculator,
					nonFavoriteSizeCalculator);

			profileIdSet = wrapper.getProfileCollector();

			commentJson = wrapper;
		} else {
			PostingJsonWrapper<?> wrapper = new PostingJsonWrapper<Posting>(comment, profile) {
				@Override
				protected PostingSize getSize() {
					return FULL;
				}
			};

			profileIdSet = wrapper.getProfileCollector();

			commentJson = new JsonArray(wrapper);
		}

		JsonMap json = new JsonMap();

		json.put("c", commentJson);
		json.put("p", new ProfileSetJsonWrapper(profileIdSet, finder));
		json.put("l", new ProfileJsonWrapper(finder.getProfile(authenticationService.getLoggedInUser(request)), finder,
				false));

		respondWithJson(json, response);
	}
}

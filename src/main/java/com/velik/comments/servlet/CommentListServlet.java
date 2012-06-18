package com.velik.comments.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Finder;
import com.velik.comments.Profile;
import com.velik.comments.exception.NoSuchCommentListException;
import com.velik.comments.json.CommentListJsonWrapper;
import com.velik.comments.pojo.FinderPojo;

public class CommentListServlet extends HttpServlet {
	private static Finder finder = new FinderPojo();

	static {
		finder.createCommentList(new CommentListId("singleton"));
	}

	private static Profile profile = finder.createProfile("sysadmin");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");

		if (pathInfo.length == 0 || pathInfo.length > 2) {
			throw new ServletException();
		}

		CommentList commentList;

		try {
			commentList = finder.getCommentList(new CommentListId(pathInfo[1]));
		} catch (NoSuchCommentListException e) {
			throw new ServletException(e);
		}

		OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());

		response.setContentType("application/json");

		new CommentListJsonWrapper(commentList, profile).print(writer);

		writer.close();
	}

}

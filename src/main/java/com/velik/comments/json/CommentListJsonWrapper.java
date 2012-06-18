package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.Profile;

public class CommentListJsonWrapper extends JsonObject {
	private CommentList commentList;
	private Profile profile;

	public CommentListJsonWrapper(CommentList commentList, Profile profile) {
		this.commentList = commentList;
		this.profile = profile;
	}

	@Override
	public void print(Writer writer) throws IOException {
		JsonArray json = array();

		for (Comment comment : commentList) {
			json.add(new CommentJsonWrapper(comment, profile));
		}

		json.print(writer);
	}
}

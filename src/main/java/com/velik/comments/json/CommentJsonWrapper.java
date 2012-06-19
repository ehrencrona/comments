package com.velik.comments.json;

import com.velik.comments.Comment;
import com.velik.comments.PostingList;
import com.velik.comments.Profile;
import com.velik.comments.Reply;

public class CommentJsonWrapper extends PostingJsonWrapper<Comment> {

	private Profile profile;

	public CommentJsonWrapper(Comment comment, Profile profile) {
		super(comment);

		this.profile = profile;
	}

	@Override
	protected JsonArray toJsonObject() {
		JsonArray replies = array();

		PostingList<Reply> repliesInvolvingFavorites = posting.getRepliesInvolvingFavorites(profile);

		for (Reply reply : repliesInvolvingFavorites) {
			replies.add(new PostingJsonWrapper<Reply>(reply));
		}

		for (Reply reply : posting.getReplies(repliesInvolvingFavorites)) {
			replies.add(new PostingJsonWrapper<Reply>(reply));
		}

		return super.toJsonObject().add(replies);
	}
}

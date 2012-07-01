package com.velik.comments.json;

import com.velik.comments.Profile;
import com.velik.comments.Reply;
import com.velik.comments.size.CommentSize;

public class ReplyJsonWrapper extends PostingJsonWrapper<Reply> {

	private CommentSize commentSize;

	public ReplyJsonWrapper(Reply reply, CommentSize commentSize, Profile profile) {
		super(reply, profile);

		this.commentSize = commentSize;
	}

	@Override
	protected PostingSize getSize() {
		return commentSize.calculate(posting, profile);
	}

}

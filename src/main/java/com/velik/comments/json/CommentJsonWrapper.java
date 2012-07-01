package com.velik.comments.json;

import com.velik.comments.Comment;
import com.velik.comments.Profile;
import com.velik.comments.Reply;
import com.velik.comments.pojo.FinderPojo;
import com.velik.comments.pojo.PostingListPojo;
import com.velik.comments.size.CommentSize;
import com.velik.comments.size.CommentSizeCalculator;

public class CommentJsonWrapper extends PostingJsonWrapper<Comment> {
	private static final PostingListPojo<Reply> EMPTY_POSTING_LIST = new PostingListPojo<Reply>(new FinderPojo());

	private CommentSize commentSize;

	public CommentJsonWrapper(Comment comment, Profile profile, CommentSizeCalculator sizeCalculator) {
		super(comment, profile);

		commentSize = sizeCalculator.calculate(posting, profile);
	}

	@Override
	protected JsonArray toJsonObject() {
		JsonArray result = super.toJsonObject();

		if (getSize() == PostingSize.FULL) {
			JsonArray replies = array();

			for (Reply reply : posting.getReplies(EMPTY_POSTING_LIST)) {
				PostingJsonWrapper<Reply> replyWrapper = new ReplyJsonWrapper(reply, commentSize, profile);

				replyWrapper.setProfileCollector(profileCollector);

				replies.add(replyWrapper);
			}

			result.add(replies);
		}

		return result;
	}

	@Override
	protected PostingSize getSize() {
		return commentSize.getPostingSize();
	}
}

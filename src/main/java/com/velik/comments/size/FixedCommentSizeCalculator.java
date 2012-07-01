package com.velik.comments.size;

import com.velik.comments.Comment;
import com.velik.comments.Profile;
import com.velik.comments.Reply;
import com.velik.comments.json.PostingSize;

public class FixedCommentSizeCalculator implements CommentSizeCalculator {
	public static final FixedCommentSizeCalculator FULL = new FixedCommentSizeCalculator(PostingSize.FULL);

	private PostingSize replySize;
	private PostingSize commentSize;

	public FixedCommentSizeCalculator(PostingSize size) {
		this(size, size);
	}

	public FixedCommentSizeCalculator(PostingSize commentSize, PostingSize replySize) {
		this.commentSize = commentSize;
		this.replySize = replySize;
	}

	@Override
	public CommentSize calculate(Comment posting, Profile profile) {
		return new CommentSize() {

			@Override
			public PostingSize calculate(Reply posting, Profile profile) {
				return replySize;
			}

			@Override
			public PostingSize getPostingSize() {
				return commentSize;
			}
		};
	}

}

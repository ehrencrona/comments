package com.velik.comments.size;

import static com.velik.comments.json.PostingSize.FULL;
import static com.velik.comments.json.PostingSize.HIDDEN;

import com.velik.comments.Comment;
import com.velik.comments.Profile;
import com.velik.comments.Reply;
import com.velik.comments.json.PostingSize;

public class DefaultCommentSizeCalculator implements CommentSizeCalculator {
	private static final int FULL_POINTS_LIMIT = 200;
	private static final int HIDDEN_POINTS_LIMIT = 90;
	private boolean involvesFavorites;

	public class DefaultCommentSize implements CommentSize {
		private PostingSize postingSize;

		public DefaultCommentSize(PostingSize postingSize) {
			this.postingSize = postingSize;
		}

		@Override
		public PostingSize getPostingSize() {
			return postingSize;
		}

		@Override
		public PostingSize calculate(Reply posting, Profile profile) {
			if (profile.isFavoriteOrSelf(posting.getPosterId())) {
				return FULL;
			} else {
				return DefaultCommentSizeCalculator.calculate(posting.getPoints());
			}
		}
	}

	public DefaultCommentSizeCalculator(boolean involvesFavorites) {
		this.involvesFavorites = involvesFavorites;
	}

	static PostingSize calculate(int points) {
		if (points >= FULL_POINTS_LIMIT) {
			return PostingSize.FULL;
		} else if (points <= HIDDEN_POINTS_LIMIT) {
			return PostingSize.HIDDEN;
		} else {
			return PostingSize.SHORT;
		}
	}

	@Override
	public CommentSize calculate(Comment comment, Profile profile) {
		CommentSize result;

		if (profile.isFavoriteOrSelf(comment.getPosterId())) {
			result = new DefaultCommentSize(FULL);
		} else {
			PostingSize sizeBasedOnValue = calculate(comment.getPoints());

			if (involvesFavorites) {
				// it was among the favorites but the comment itself was not
				// from a favorite so
				// we expand it but show only the favorites' replies.
				result = new CommentSize() {
					@Override
					public PostingSize calculate(Reply posting, Profile profile) {
						if (profile.isFavoriteOrSelf(posting.getPosterId())) {
							return FULL;
						} else {
							return HIDDEN;
						}
					}

					@Override
					public PostingSize getPostingSize() {
						return FULL;
					}
				};
			} else {
				result = new DefaultCommentSize(sizeBasedOnValue);
			}
		}

		return result;
	}
}

package com.velik.comments;

public interface Comment extends Posting {

	PostingList<Reply> getRepliesInvolvingFavorites(Profile profile);

	Iterable<Reply> getReplies(PostingList<Reply> exclude);

	Reply reply(String text, ProfileId posterId);

	ProfileSet getAllRepliers();

}

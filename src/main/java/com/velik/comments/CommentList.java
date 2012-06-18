package com.velik.comments;

public interface CommentList extends PostingList<Comment>, ModelObject<CommentListId> {

	Comment comment(String text, ProfileId profileId);

	PostingList<Comment> getCommentsInvolvingFavorites(Profile profile);

	Iterable<Comment> getComments(PostingList<Comment> exclude, int pageSize);

}

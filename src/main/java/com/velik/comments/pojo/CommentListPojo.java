package com.velik.comments.pojo;

import java.util.Iterator;

import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Finder;
import com.velik.comments.PostingId;
import com.velik.comments.PostingList;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;

public class CommentListPojo implements CommentList {
	private PostingsBySender<Comment> comments;
	private CommentListId id;
	private Finder finder;

	CommentListPojo(CommentListId id, Finder finder) {
		this.id = id;
		this.finder = finder;
		this.comments = new PostingsBySender<Comment>(finder);
	}

	@Override
	public boolean contains(PostingId id) {
		return comments.contains(id);
	}

	@Override
	public Iterator<Comment> iterator() {
		return comments.getPostings().iterator();
	}

	@Override
	public CommentListId getId() {
		return id;
	}

	@Override
	public Comment comment(String text, ProfileId posterId) {
		CommentPojo comment = new CommentPojo(finder, posterId);

		comment.setText(text);

		((FinderPojo) finder).register(comment);

		comments.add(comment);

		return comment;
	}

	@Override
	public PostingList<Comment> getCommentsInvolvingFavorites(Profile profile) {
		return comments.getPostingsInvolvingFavorites(profile);
	}

	@Override
	public Iterable<Comment> getComments(PostingList<Comment> exclude, int pageSize) {
		return comments.getPostings(exclude, pageSize);
	}

}

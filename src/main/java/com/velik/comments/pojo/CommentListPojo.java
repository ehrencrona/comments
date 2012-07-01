package com.velik.comments.pojo;

import java.io.Serializable;
import java.util.Iterator;

import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Finder;
import com.velik.comments.PostingId;
import com.velik.comments.PostingList;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.profileset.LazyUnionProfileSet;
import com.velik.comments.profileset.SingletonProfileSet;

public class CommentListPojo implements CommentList, Serializable {
	private static final long serialVersionUID = 1;

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
		CommentPojo comment = new CommentPojo(posterId, finder);

		comment.setText(text);

		((FinderPojo) finder).register(comment);

		comments.add(comment);

		return comment;
	}

	@Override
	public PostingList<Comment> getCommentsInvolvingFavorites(Profile profile) {
		PostingListPojo<Comment> result = new PostingListPojo<Comment>(finder);

		ProfileSet favorites = new LazyUnionProfileSet(profile.getFavorites(), new SingletonProfileSet(profile.getId()));

		for (Comment comment : this) {
			ProfileSet allRepliers = comment.getAllRepliers();

			if (favorites.contains(comment.getPosterId()) || allRepliers.intersects(favorites)) {
				result.add(comment.getId());
			}
		}

		return result;
	}

	@Override
	public Iterable<Comment> getComments(PostingList<Comment> exclude, int pageSize) {
		return comments.getPostings(exclude, pageSize);
	}

	@Override
	public String toString() {
		return id.toString();
	}
}

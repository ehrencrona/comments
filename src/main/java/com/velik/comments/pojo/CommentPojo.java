package com.velik.comments.pojo;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.velik.comments.Comment;
import com.velik.comments.Finder;
import com.velik.comments.PostingId;
import com.velik.comments.PostingList;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.Reply;
import com.velik.comments.exception.NoSuchProfileException;

public class CommentPojo extends PostingPojo implements Comment {
	private static final Logger LOGGER = Logger.getLogger(CommentPojo.class.getName());

	private PostingsBySender<Reply> replies;
	private PostingId id;

	public CommentPojo(Finder finder, ProfileId posterId) {
		super(finder, posterId);

		replies = new PostingsBySender<Reply>(finder);
	}

	@Override
	public String getSummarizedText() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PostingList<Reply> getRepliesInvolvingFavorites(Profile profile) {
		return replies.getPostingsInvolvingFavorites(profile);
	}

	@Override
	public Iterable<Reply> getReplies(PostingList<Reply> exclude) {
		return replies.getPostings(exclude, Integer.MAX_VALUE);
	}

	@Override
	public Reply reply(String text, ProfileId posterId) {
		ReplyPojo reply = new ReplyPojo(finder, posterId);
		reply.setText(text);

		((FinderPojo) finder).register(reply);

		replies.add(reply);

		for (ProfileId replierId : getAllRepliers()) {
			if (!replierId.equals(posterId)) {
				addToNewsfeed(reply, replierId);
			}
		}

		if (!getAllRepliers().contains(getPosterId())) {
			if (!getPosterId().equals(posterId)) {
				addToNewsfeed(reply, getPosterId());
			}
		}

		return reply;
	}

	protected void addToNewsfeed(ReplyPojo reply, ProfileId profileId) {
		try {
			((ProfilePojo) finder.getProfile(profileId)).addToNewsfeed(reply);
		} catch (NoSuchProfileException e) {
			LOGGER.log(Level.WARNING, "While informing posters of " + this + " about " + reply + ": " + e.getMessage(),
					e);
		}
	}

	@Override
	public PostingId getId() {
		return id;
	}

	public void setId(PostingId id) {
		this.id = id;
	}

	@Override
	public ProfileSet getAllRepliers() {
		return replies.getAllPosters();
	}
}

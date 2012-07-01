package com.velik.comments.pojo;

import com.velik.comments.Comment;
import com.velik.comments.Finder;
import com.velik.comments.PostingList;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.Reply;

public class CommentPojo extends PostingPojo implements Comment {
	private static final long serialVersionUID = 1;

	private PostingsBySender<Reply> replies;

	public CommentPojo(ProfileId posterId, Finder finder) {
		super(posterId, finder);

		replies = new PostingsBySender<Reply>(finder);
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
		ReplyPojo reply = new ReplyPojo(posterId, finder);
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
		ProfilePojo profile = (ProfilePojo) finder.getProfile(profileId);

		if (!profile.isAnonymous()) {
			profile.addToNewsfeed(reply);
		}
	}

	@Override
	public ProfileSet getAllRepliers() {
		return replies.getAllPosters();
	}
}

package com.velik.comments.pojo;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationId;
import com.velik.comments.exception.NoSuchCommentListException;
import com.velik.comments.exception.NoSuchObjectException;
import com.velik.comments.exception.NoSuchPostingException;
import com.velik.comments.exception.NoSuchProfileException;
import com.velik.comments.exception.NoSuchValuationException;
import com.velik.comments.iterator.ProfileIdIterable;

public class FinderPojo implements Finder {
	private static final Logger LOGGER = Logger.getLogger(FinderPojo.class.getName());

	private ObjectById<ProfileId, Profile> profiles = new ObjectById<ProfileId, Profile>();
	private ObjectById<PostingId, Posting> postings = new ObjectById<PostingId, Posting>();
	private ObjectById<ValuationId, Valuation> valuations = new ObjectById<ValuationId, Valuation>();
	private ObjectById<CommentListId, CommentList> commentLists = new ObjectById<CommentListId, CommentList>();

	private int nextId;

	@Override
	public CommentList getCommentList(CommentListId commentListId) throws NoSuchCommentListException {
		try {
			return commentLists.get(commentListId);
		} catch (NoSuchObjectException e) {
			throw new NoSuchCommentListException(e);
		}
	}

	@Override
	public Profile getProfile(ProfileId profileId) throws NoSuchProfileException {
		try {
			return profiles.get(profileId);
		} catch (NoSuchObjectException e) {
			throw new NoSuchProfileException(e);
		}
	}

	@Override
	public Posting getPosting(PostingId postingId) throws NoSuchPostingException {
		try {
			return postings.get(postingId);
		} catch (NoSuchObjectException e) {
			throw new NoSuchPostingException(e);
		}
	}

	void register(Posting posting) {
		((PostingPojo) posting).setId(new PostingId(nextId()));

		ProfileId posterId = posting.getPosterId();

		try {
			Profile profile = getProfile(posterId);

			((ProfilePojo) profile).registerOwnPosting(posting);

			for (Profile favoriteOf : new ProfileIdIterable(profile.getFavoriteOf(), this)) {
				((ProfilePojo) favoriteOf).addToNewsfeed(posting);
			}
		} catch (NoSuchProfileException e) {
			LOGGER.log(Level.WARNING, "The posting " + posting + " belonged to a poster " + posterId
					+ " that is unknown.");
		}

		postings.register(posting);
	}

	void register(Valuation valuation) {
		((ValuationPojo) valuation).setId(new ValuationId(nextId()));

		valuations.register(valuation);

		try {
			((ProfilePojo) getProfile(valuation.getValuer())).addGivenValuation(valuation);
		} catch (NoSuchProfileException e) {
			// fine.
		}

		try {
			((ProfilePojo) getProfile(valuation.getValuedProfile())).addReceivedValuation(valuation);
		} catch (NoSuchProfileException e) {
			// fine.
		}
	}

	private int nextId() {
		return nextId++;
	}

	@Override
	public Valuation getValuation(ValuationId valuationId) throws NoSuchValuationException {
		try {
			return valuations.get(valuationId);
		} catch (NoSuchObjectException e) {
			throw new NoSuchValuationException(e);
		}
	}

	@Override
	public Profile createProfile(String alias) {
		ProfilePojo profile = new ProfilePojo(new ProfileId(nextId()), this);

		profiles.register(profile);

		return profile;
	}

	@Override
	public CommentList createCommentList(CommentListId id) {
		CommentList result = new CommentListPojo(id, this);

		commentLists.register(result);

		return result;
	}
}

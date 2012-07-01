package com.velik.comments.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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

/**
 * TODO Concurrency.
 */
public class FinderPojo implements Finder, Serializable {
	private static final long serialVersionUID = 1;
	private static final Logger LOGGER = Logger.getLogger(FinderPojo.class.getName());

	private final Profile anonymousProfile;

	private ObjectById<ProfileId, Profile> profiles = new ObjectById<ProfileId, Profile>();
	private Map<String, Profile> profilesByAlias = new HashMap<String, Profile>();
	private ObjectById<PostingId, Posting> postings = new ObjectById<PostingId, Posting>();
	private ObjectById<ValuationId, Valuation> valuations = new ObjectById<ValuationId, Valuation>();
	private ObjectById<CommentListId, CommentList> commentLists = new ObjectById<CommentListId, CommentList>();

	private int nextId;

	public FinderPojo() {
		anonymousProfile = new ProfilePojo(ProfileId.ANONYMOUS, "anonymous", this);

		createProfile("sysadmin");
	}

	@Override
	public CommentList getCommentList(CommentListId commentListId) throws NoSuchCommentListException {
		try {
			return commentLists.get(commentListId);
		} catch (NoSuchObjectException e) {
			throw new NoSuchCommentListException(e);
		}
	}

	@Override
	public Profile getProfile(ProfileId profileId) {
		try {
			return profiles.get(profileId);
		} catch (NoSuchObjectException e) {
			if (!profileId.isAnonymous()) {
				LOGGER.log(Level.WARNING, e.getMessage());
			}

			return anonymousProfile;
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

		Profile profile = getProfile(posterId);

		if (!profile.isAnonymous()) {
			((ProfilePojo) profile).registerOwnPosting(posting);

			for (Profile favoriteOf : new ProfileIdIterable(profile.getFavoriteOf(), this)) {
				((ProfilePojo) favoriteOf).addToNewsfeed(posting);
			}
		}

		postings.register(posting);
	}

	void register(Valuation valuation) {
		((ValuationPojo) valuation).setId(new ValuationId(nextId()));

		valuations.register(valuation);

		((ProfilePojo) getProfile(valuation.getValuer())).addGivenValuation(valuation);
		((ProfilePojo) getProfile(valuation.getValuedProfile())).addReceivedValuation(valuation);
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
		ProfilePojo profile = new ProfilePojo(new ProfileId(nextId()), alias, this);

		profiles.register(profile);
		// TODO: check for existing with same alias.
		profilesByAlias.put(alias, profile);

		return profile;
	}

	@Override
	public CommentList createCommentList(CommentListId id) {
		// TODO: check for existing with same ID.
		CommentList result = new CommentListPojo(id, this);

		commentLists.register(result);

		return result;
	}

	@Override
	public ProfileId getProfile(String alias) throws NoSuchProfileException {
		Profile result = profilesByAlias.get(alias);

		if (result == null) {
			throw new NoSuchProfileException(new NoSuchObjectException("No profile with alias " + alias + "."));
		}

		return result.getId();
	}

	@Override
	public void persist() {

	}

	@Override
	public void initalize() {

	}
}

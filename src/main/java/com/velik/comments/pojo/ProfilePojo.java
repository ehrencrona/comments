package com.velik.comments.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationId;
import com.velik.comments.ValuationType;
import com.velik.comments.exception.NoSuchProfileException;
import com.velik.comments.iterator.PostingIdIterable;
import com.velik.comments.iterator.ValuationIdIterable;

public class ProfilePojo implements Profile {
	private static final Logger LOGGER = Logger.getLogger(ProfilePojo.class.getName());

	private ProfileId id;
	private String alias;
	private Set<ProfileId> favorites = new HashSet<ProfileId>();
	private Set<ProfileId> favoriteOf = new HashSet<ProfileId>();
	private List<PostingId> newsfeed = new ArrayList<PostingId>();
	private List<PostingId> ownPostings = new ArrayList<PostingId>();

	private List<ValuationId> receivedValuations = new ArrayList<ValuationId>();
	private List<ValuationId> givenValuations = new ArrayList<ValuationId>();

	private int points;
	private Finder finder;

	public ProfilePojo(ProfileId id, Finder finder) {
		this.finder = finder;
		this.id = id;
	}

	public ProfileId getId() {
		return id;
	}

	void setId(ProfileId id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public ProfileSet getFavorites() {
		return new ProfileSetPojo(favorites, finder);
	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public Valuation value(ValuationType type, int points, ProfileId valuer) {
		this.points += points;

		ValuationPojo valuation = new ValuationPojo();
		valuation.setType(type);
		valuation.setValue(points);
		valuation.setValuedProfile(getId());
		valuation.setValuer(valuer);

		((FinderPojo) finder).register(valuation);

		return valuation;
	}

	@Override
	public Iterable<Posting> getNewsfeed(int count) {
		return new PostingIdIterable<Posting>(newsfeed, finder);
	}

	@Override
	public Iterable<Posting> getOwnPostings() {
		return new PostingIdIterable<Posting>(ownPostings, finder);
	}

	@Override
	public Iterable<Valuation> getReceivedValuations(long since) {
		return new ValuationIdIterable(receivedValuations, finder);
	}

	@Override
	public void addFavorite(ProfileId profile) {
		favorites.add(profile);

		try {
			((ProfilePojo) finder.getProfile(profile)).favoriteOf.add(getId());
		} catch (NoSuchProfileException e) {
			LOGGER.log(Level.WARNING, "Adding favorite " + profile + " to " + this + " that could not be found.");
		}
	}

	void registerOwnPosting(Posting posting) {
		ownPostings.add(0, posting.getId());
	}

	public void addToNewsfeed(Posting posting) {
		newsfeed.add(0, posting.getId());
	}

	@Override
	public ProfileSet getFavoriteOf() {
		return new ProfileSetPojo(favoriteOf, finder);
	}

	void addGivenValuation(Valuation valuation) {
		givenValuations.add(valuation.getId());
	}

	void addReceivedValuation(Valuation valuation) {
		receivedValuations.add(valuation.getId());
	}

	@Override
	public Iterable<Valuation> getGivenValuations() {
		return new ValuationIdIterable(givenValuations, finder);
	}

	@Override
	public String toString() {
		return alias + " (" + id + ")";
	}
}

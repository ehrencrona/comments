package com.velik.comments.delegate;

import com.velik.comments.Posting;
import com.velik.comments.Profile;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationType;

public class DelegatingProfile implements Profile {
	protected Profile delegate;

	public DelegatingProfile(Profile profile) {
		this.delegate = profile;
	}

	public Valuation value(ValuationType type, int points, ProfileId valuer) {
		return delegate.value(type, points, valuer);
	}

	public ProfileId getId() {
		return delegate.getId();
	}

	public String getAlias() {
		return delegate.getAlias();
	}

	public boolean isAnonymous() {
		return delegate.isAnonymous();
	}

	public ProfileSet getFavoriteOf() {
		return delegate.getFavoriteOf();
	}

	public Iterable<Profile> getFavorites() {
		return delegate.getFavorites();
	}

	public ProfileSet getFavoritesAsSet() {
		return delegate.getFavoritesAsSet();
	}

	public void addFavorite(ProfileId profile) {
		delegate.addFavorite(profile);
	}

	public int getPoints() {
		return delegate.getPoints();
	}

	public Iterable<Posting> getNewsfeed(int count) {
		return delegate.getNewsfeed(count);
	}

	public Iterable<Posting> getOwnPostings() {
		return delegate.getOwnPostings();
	}

	public Iterable<Valuation> getReceivedValuations(long since) {
		return delegate.getReceivedValuations(since);
	}

	public Iterable<Valuation> getGivenValuations() {
		return delegate.getGivenValuations();
	}

	public boolean isFavoriteOrSelf(ProfileId posterId) {
		return delegate.isFavoriteOrSelf(posterId);
	}

	public String toString() {
		return delegate.toString();
	}
}

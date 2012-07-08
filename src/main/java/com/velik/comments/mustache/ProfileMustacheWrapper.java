package com.velik.comments.mustache;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.Profile;
import com.velik.comments.Valuation;
import com.velik.comments.delegate.DelegatingProfile;
import com.velik.comments.util.TransformingIterable;

public class ProfileMustacheWrapper extends DelegatingProfile {
	private Finder finder;

	public ProfileMustacheWrapper(Profile profile, Finder finder) {
		super(profile);

		this.finder = finder;
	}

	public Iterable<ValuationMustacheWrapper> getValuationHistory() {
		// TODO -1 ?
		return new TransformingIterable<Valuation, ValuationMustacheWrapper>(getReceivedValuations(-1)) {
			@Override
			protected ValuationMustacheWrapper transform(Valuation valuation) {
				return new ValuationMustacheWrapper(valuation, finder);
			}
		};
	}

	/**
	 * TODO Not so nice name. How to avoid name collisions in a general way?
	 */
	public Iterable<PostingMustacheWrapper> getOwnPostingWrappers() {
		return new TransformingIterable<Posting, PostingMustacheWrapper>(super.getOwnPostings()) {
			@Override
			protected PostingMustacheWrapper transform(Posting posting) {
				return new PostingMustacheWrapper(posting, finder);
			}
		};
	}

	public boolean isHasLikers() {
		return false;
	}

	public boolean isCurrentUserCanLike() {
		return false;
	}
}

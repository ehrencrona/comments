package com.velik.comments.mustache;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.Profile;
import com.velik.comments.Valuation;
import com.velik.comments.delegate.DelegatingValuation;
import com.velik.comments.exception.NoSuchPostingException;

public class ValuationMustacheWrapper extends DelegatingValuation {
	private static final Logger LOGGER = Logger.getLogger(ValuationMustacheWrapper.class.getName());

	private Finder finder;

	public ValuationMustacheWrapper(Valuation valuation, Finder finder) {
		super(valuation);

		this.finder = finder;
	}

	public Profile getValuer() {
		return finder.getProfile(getValuerId());
	}

	public Posting getValuedPosting() {
		try {
			return finder.getPosting(getValuedPostingId());
		} catch (NoSuchPostingException e) {
			LOGGER.log(Level.WARNING, "For valuation " + this + ": " + e.getMessage(), e);

			return Posting.EMPTY;
		}
	}

	public DateMustacheWrapper getDate() {
		return new DateMustacheWrapper(super.getDate());
	}
}

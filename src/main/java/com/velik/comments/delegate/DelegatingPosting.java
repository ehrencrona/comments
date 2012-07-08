package com.velik.comments.delegate;

import java.util.Date;

import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationType;

public class DelegatingPosting implements Posting {
	private Posting delegate;

	public DelegatingPosting(Posting delegate) {
		this.delegate = delegate;
	}

	public int getPoints() {
		return delegate.getPoints();
	}

	public Valuation value(ValuationType type, int points, ProfileId valuer) {
		return delegate.value(type, points, valuer);
	}

	public PostingId getId() {
		return delegate.getId();
	}

	public Date getDate() {
		return delegate.getDate();
	}

	public String getText() {
		return delegate.getText();
	}

	public String getSummarizedText() {
		return delegate.getSummarizedText();
	}

	public ProfileId getPosterId() {
		return delegate.getPosterId();
	}

	public ProfileSet getLikers() {
		return delegate.getLikers();
	}
}

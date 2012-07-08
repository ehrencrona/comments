package com.velik.comments.delegate;

import java.util.Date;

import com.velik.comments.PostingId;
import com.velik.comments.ProfileId;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationId;
import com.velik.comments.ValuationType;

public class DelegatingValuation implements Valuation {
	protected Valuation delegate;

	public DelegatingValuation(Valuation delegate) {
		this.delegate = delegate;
	}

	public ValuationId getId() {
		return delegate.getId();
	}

	public ValuationType getType() {
		return delegate.getType();
	}

	public ProfileId getValuerId() {
		return delegate.getValuerId();
	}

	public PostingId getValuedPostingId() {
		return delegate.getValuedPostingId();
	}

	public ProfileId getValuedProfileId() {
		return delegate.getValuedProfileId();
	}

	public int getValue() {
		return delegate.getValue();
	}

	public String toString() {
		return delegate.toString();
	}

	public Date getDate() {
		return delegate.getDate();
	}
}

package com.velik.comments.iterator;

import java.util.Iterator;

import com.velik.comments.Finder;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationId;

public class ValuationIdIterable implements Iterable<Valuation> {
	private Iterable<ValuationId> delegate;
	private Finder finder;

	public ValuationIdIterable(Iterable<ValuationId> delegate, Finder finder) {
		this.delegate = delegate;
		this.finder = finder;
	}

	@Override
	public Iterator<Valuation> iterator() {
		return new ValuationIdIterator(delegate.iterator(), finder);
	}

}

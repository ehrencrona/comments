package com.velik.comments.iterator;

import java.util.Iterator;

import com.velik.comments.Finder;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationId;
import com.velik.comments.exception.NoSuchObjectException;

public class ValuationIdIterator extends AbstractIdIterator<ValuationId, Valuation> {

	ValuationIdIterator(Iterator<ValuationId> delegate, Finder finder) {
		super(delegate, finder);
	}

	@Override
	protected Valuation getObject(ValuationId next) throws NoSuchObjectException {
		return finder.getValuation(next);
	}

}

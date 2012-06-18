package com.velik.comments.iterator;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.velik.comments.Finder;
import com.velik.comments.Id;
import com.velik.comments.exception.NoSuchObjectException;
import com.velik.comments.util.FetchingIterator;

abstract class AbstractIdIterator<I extends Id, T> extends FetchingIterator<T> {
	private static final Logger LOGGER = Logger.getLogger(AbstractIdIterator.class.getName());

	private Iterator<I> delegate;
	protected Finder finder;

	AbstractIdIterator(Iterator<I> delegate, Finder finder) {
		this.delegate = delegate;
		this.finder = finder;
	}

	@Override
	protected T fetch() {
		while (delegate.hasNext()) {
			try {
				return getObject(delegate.next());
			} catch (NoSuchObjectException e) {
				LOGGER.log(Level.WARNING, e.getMessage(), e);
			}
		}

		return null;
	}

	protected abstract T getObject(I next) throws NoSuchObjectException;
}
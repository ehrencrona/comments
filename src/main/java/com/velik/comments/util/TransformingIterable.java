package com.velik.comments.util;

import java.util.Iterator;

public abstract class TransformingIterable<T, U> implements Iterable<U> {
	private Iterable<T> delegate;

	public TransformingIterable(Iterable<T> delegate) {
		this.delegate = delegate;
	}

	protected abstract U transform(T next);

	@Override
	public String toString() {
		return delegate.toString();
	}

	@Override
	public Iterator<U> iterator() {
		return new TransformingIterator<T, U>(delegate.iterator()) {
			@Override
			protected U transform(T next) {
				return TransformingIterable.this.transform(next);
			}
		};
	}

}

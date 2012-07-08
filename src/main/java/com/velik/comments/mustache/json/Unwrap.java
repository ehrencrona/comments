package com.velik.comments.mustache.json;

import com.github.mustachejava.ObjectHandler;
import com.github.mustachejava.util.GuardException;
import com.github.mustachejava.util.Wrapper;

public class Unwrap {
	/**
	 * Copied from
	 * {@link com.github.mustachejava.reflect.ReflectionObjectHandler#unwrap}.
	 */
	protected static Object unwrap(ObjectHandler oh, int scopeIndex, Wrapper[] wrappers, Object[] scopes)
			throws GuardException {
		Object scope = oh.coerce(scopes[scopeIndex]);
		// The value may be buried by . notation
		if (wrappers != null) {
			for (Wrapper wrapper : wrappers) {
				scope = oh.coerce(wrapper.call(new Object[] { scope }));
			}
		}
		return scope;
	}

}

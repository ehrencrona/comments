package com.velik.comments.mustache.json;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

import com.github.mustachejava.MustacheException;
import com.github.mustachejava.ObjectHandler;
import com.github.mustachejava.reflect.ReflectionWrapper;
import com.github.mustachejava.util.GuardException;
import com.github.mustachejava.util.Wrapper;
import com.google.common.base.Predicate;

public class JsonMapReflectionWrapper extends ReflectionWrapper {

	private ObjectHandler oh;

	public JsonMapReflectionWrapper(int scopeIndex, Wrapper[] wrappers, Predicate<Object[]>[] guard,
			AccessibleObject method, Object[] arguments, ObjectHandler oh) {
		super(scopeIndex, wrappers, guard, method, arguments, oh);

		this.oh = oh;
	}

	@Override
	public Object call(Object[] scopes) throws GuardException {
		try {
			guardCall(scopes);
			Object scope = Unwrap.unwrap(oh, scopeIndex, wrappers, scopes);
			if (scope == null)
				return null;
			if (method == null) {
				return field.get(scope);
			} else {
				return method.invoke(scope, arguments);
			}
		} catch (InvocationTargetException e) {
			throw new MustacheException("Failed to execute method: " + method, e.getTargetException());
		} catch (IllegalAccessException e) {
			throw new MustacheException("Failed to execute method: " + method, e);
		}
	}

}

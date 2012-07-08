package com.velik.comments.mustache.json;

import java.lang.reflect.AccessibleObject;
import java.util.List;

import com.github.mustachejava.reflect.ReflectionObjectHandler;
import com.github.mustachejava.util.Wrapper;
import com.google.common.base.Predicate;
import com.velik.comments.json.JsonMap;

public class JsonObjectHandler extends ReflectionObjectHandler {
	@Override
	protected Wrapper findWrapper(int scopeIndex, Wrapper[] wrappers, List<Predicate<Object[]>> guards,
			Object scope, String name) {
		scope = coerce(scope);

		if (scope == null) {
			return null;
		}

		if (scope instanceof JsonMap) {
			JsonMap map = (JsonMap) scope;

			if (!map.containsKey(name)) {
				guards.add(new JsonMapGuard(this, scopeIndex, name, false, wrappers));
				return null;
			} else {
				guards.add(new JsonMapGuard(this, scopeIndex, name, true, wrappers));

				return createJsonWrapper(scopeIndex, wrappers, guards, MAP_METHOD, new Object[] { name });

			}
		}

		return super.findWrapper(scopeIndex, wrappers, guards, scope, name);
	}

	@SuppressWarnings("unchecked")
	protected Wrapper createJsonWrapper(int scopeIndex, Wrapper[] wrappers,
			List<? extends Predicate<Object[]>> guard, AccessibleObject member, Object[] arguments) {
		return new JsonMapReflectionWrapper(scopeIndex, wrappers, guard.toArray(new Predicate[guard.size()]),
				member, arguments, this);
	}
}
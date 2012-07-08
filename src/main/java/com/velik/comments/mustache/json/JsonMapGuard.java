package com.velik.comments.mustache.json;

import com.github.mustachejava.ObjectHandler;
import com.github.mustachejava.util.Wrapper;
import com.google.common.base.Predicate;
import com.velik.comments.json.JsonMap;

public class JsonMapGuard implements Predicate<Object[]> {
	private final ObjectHandler oh;
	private final int scopeIndex;
	private final String name;
	private final boolean contains;
	private final Wrapper[] wrappers;

	public JsonMapGuard(ObjectHandler oh, int scopeIndex, String name, boolean contains, Wrapper[] wrappers) {
		this.oh = oh;
		this.scopeIndex = scopeIndex;
		this.name = name;
		this.contains = contains;
		this.wrappers = wrappers;
	}

	@Override
	public boolean apply(Object[] objects) {
		Object scope = Unwrap.unwrap(oh, scopeIndex, wrappers, objects);
		if (scope instanceof JsonMap) {
			JsonMap map = (JsonMap) scope;
			if (contains) {
				return map.containsKey(name);
			} else {
				return !map.containsKey(name);
			}
		}
		return true;
	}

}

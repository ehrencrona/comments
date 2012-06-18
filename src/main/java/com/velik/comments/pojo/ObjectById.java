package com.velik.comments.pojo;

import java.util.HashMap;
import java.util.Map;

import com.velik.comments.ModelObject;
import com.velik.comments.exception.NoSuchObjectException;

public class ObjectById<I, T extends ModelObject<I>> {
	private Map<I, T> map = new HashMap<I, T>();

	public T get(I id) throws NoSuchObjectException {
		if (id == null) {
			throw new NoSuchObjectException("Invalid ID null.");
		}

		T result = map.get(id);

		if (result == null) {
			throw new NoSuchObjectException("Object with ID \"" + id + "\" was unknown.");
		}

		return result;
	}

	public void register(T object) {
		map.put(object.getId(), object);
	}
}

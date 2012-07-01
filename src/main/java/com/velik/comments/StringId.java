package com.velik.comments;

import java.io.Serializable;

public class StringId implements Serializable {
	private String id;

	StringId(String id) {
		this.id = id;
	}

	/**
	 * For serialization.
	 */
	StringId() {
	}

	public boolean equals(Object o) {
		return o.getClass().equals(getClass()) && ((StringId) o).id.equals(id);
	}

	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
		return id;
	}
}

package com.velik.comments;

public class StringId {
	private String id;

	StringId(String id) {
		this.id = id;
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

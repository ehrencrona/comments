package com.velik.comments;

public class Id {
	private int id;

	public Id(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		return o.getClass().equals(getClass()) && ((Id) o).id == id;
	}

	@Override
	public int hashCode() {
		return id * 4711;
	}

	@Override
	public String toString() {
		return Integer.toString(id);
	}

}

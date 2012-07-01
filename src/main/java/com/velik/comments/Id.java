package com.velik.comments;

import java.io.Serializable;

public class Id implements Serializable {
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

	public int getIntegerId() {
		return id;
	}
}

package com.velik.comments.json;

public enum PostingSize {
	FULL, SHORT, HIDDEN;

	public String toString() {
		return name().toLowerCase();
	}
}

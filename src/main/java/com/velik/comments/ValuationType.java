package com.velik.comments;

public enum ValuationType {

	FB_LIKE(true, 10), LIKE(true, 5), HATE(false, 5), READ(true, 1);

	private boolean positive;
	private int value;

	private ValuationType(boolean positive, int value) {
		this.positive = positive;
		this.value = value;
	}

	public boolean isPositive() {
		return positive;
	}

	public int getValue() {
		return value;
	}

}

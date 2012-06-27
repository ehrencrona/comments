package com.velik.comments.json;

import com.velik.comments.Posting;

public class SizeCalculator {
	private static final int FULL_POINTS_LIMIT = 10;
	private static final int HIDDEN_POINTS_LIMIT = 5;

	private Posting posting;

	public SizeCalculator(Posting posting) {
		this.posting = posting;
	}

	public PostingSize calculate() {
		if (posting.getPoints() >= FULL_POINTS_LIMIT) {
			return PostingSize.FULL;
		} else if (posting.getPoints() <= HIDDEN_POINTS_LIMIT) {
			return PostingSize.HIDDEN;
		} else {
			return PostingSize.SHORT;
		}
	}

}

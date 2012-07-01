package com.velik.comments.util;

import java.io.Serializable;

public class Tuple<S1, S2> implements Serializable {
	private S1 one;
	private S2 two;

	public Tuple(S1 one, S2 two) {
		this.one = one;
		this.two = two;
	}

	public S2 getTwo() {
		return two;
	}

	public S1 getOne() {
		return one;
	}

}

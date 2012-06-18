package com.velik.comments;

public interface Valued {

	int getPoints();

	Valuation value(ValuationType type, int points, ProfileId valuer);

}

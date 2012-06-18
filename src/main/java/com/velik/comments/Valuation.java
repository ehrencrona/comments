package com.velik.comments;

public interface Valuation extends ModelObject<ValuationId> {

	ValuationId getId();

	ValuationType getType();

	ProfileId getValuer();

	PostingId getValuedPosting();

	ProfileId getValuedProfile();

	int getValue();

}

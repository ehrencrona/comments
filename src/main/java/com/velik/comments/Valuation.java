package com.velik.comments;

import java.util.Date;

public interface Valuation extends ModelObject<ValuationId> {
	Date getDate();

	ValuationId getId();

	ValuationType getType();

	ProfileId getValuerId();

	PostingId getValuedPostingId();

	ProfileId getValuedProfileId();

	int getValue();

}

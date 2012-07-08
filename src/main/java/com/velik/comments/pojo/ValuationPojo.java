package com.velik.comments.pojo;

import java.io.Serializable;
import java.util.Date;

import com.velik.comments.PostingId;
import com.velik.comments.ProfileId;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationId;
import com.velik.comments.ValuationType;

public class ValuationPojo implements Valuation, Serializable {
	private static final long serialVersionUID = 2;

	private ValuationId id;
	private ValuationType type;
	private ProfileId valuer;
	private int value;
	private PostingId valuedPosting;
	private ProfileId valuedProfile;
	private Date date = new Date();

	public ValuationId getId() {
		return id;
	}

	public void setId(ValuationId id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public ValuationType getType() {
		return type;
	}

	public void setType(ValuationType type) {
		this.type = type;
	}

	public ProfileId getValuerId() {
		return valuer;
	}

	public void setValuer(ProfileId valuer) {
		this.valuer = valuer;
	}

	public ProfileId getValuedProfileId() {
		return valuedProfile;
	}

	public void setValuedProfile(ProfileId valuedProfile) {
		this.valuedProfile = valuedProfile;
	}

	public PostingId getValuedPostingId() {
		return valuedPosting;
	}

	public void setValuedPosting(PostingId valuedPosting) {
		this.valuedPosting = valuedPosting;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}

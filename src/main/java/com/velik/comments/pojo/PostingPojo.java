package com.velik.comments.pojo;

import java.util.Date;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationType;
import com.velik.comments.exception.NotAddedException;

public class PostingPojo implements Posting {
	private PostingId id;

	private Date date = new Date();
	private int points;
	private String text;
	private ProfileId posterId;

	private ProfileSetPojo likers;

	protected Finder finder;

	public PostingPojo(Finder finder, ProfileId posterId) {
		this.finder = finder;
		this.posterId = posterId;
		likers = new ProfileSetPojo(finder);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setText(String text) {
		this.text = text;
	}

	public PostingId getId() {
		if (id == null) {
			throw new NotAddedException();
		}

		return id;
	}

	public Date getDate() {
		return date;
	}

	public int getPoints() {
		return points;
	}

	@Override
	public Valuation value(ValuationType type, int points, ProfileId valuer) {
		this.points += points;

		ValuationPojo valuation = new ValuationPojo();
		valuation.setType(type);
		valuation.setValue(points);
		valuation.setValuedPosting(getId());
		valuation.setValuedProfile(getPosterId());
		valuation.setValuer(valuer);

		((FinderPojo) finder).register(valuation);

		if (ValuationType.LIKE.equals(type)) {
			likers.add(valuer);
		}

		return valuation;
	}

	public String getText() {
		return text;
	}

	public ProfileId getPosterId() {
		return posterId;
	}

	@Override
	public String getSummarizedText() {
		return text;
	}

	void setId(PostingId id) {
		this.id = id;
	}

	public String toString() {
		return '"' + getText() + '"' + " by " + getPosterId();
	}

	@Override
	public ProfileSet getLikers() {
		return likers;
	}
}

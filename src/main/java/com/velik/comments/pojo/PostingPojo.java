package com.velik.comments.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.velik.comments.Finder;
import com.velik.comments.Posting;
import com.velik.comments.PostingId;
import com.velik.comments.ProfileId;
import com.velik.comments.ProfileSet;
import com.velik.comments.Valuation;
import com.velik.comments.ValuationType;
import com.velik.comments.exception.NotAddedException;

public class PostingPojo implements Posting, Serializable {
	private static final long serialVersionUID = 1;
	private static final Logger LOGGER = Logger.getLogger(PostingPojo.class.getName());

	private PostingId id;

	private Date date = new Date();
	private int points = 100;
	private String text;
	private ProfileId posterId;

	private ProfileSetPojo likers;

	protected Finder finder;

	public PostingPojo(ProfileId posterId, Finder finder) {
		this.posterId = posterId;
		this.finder = finder;
		likers = new ProfileSetPojo();
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
		if (likers.contains(valuer)) {
			LOGGER.log(Level.WARNING, "Attempt to like already liked posting " + this + " by " + valuer + ".");
		}

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
		return text + "...";
	}

	void setId(PostingId id) {
		this.id = id;
	}

	public String toString() {
		return '"' + shorten(getText()) + '"' + " by " + getPosterId() + " (" + id + ")";
	}

	private String shorten(String text) {
		text = text.replace("\n", " ");

		if (text.length() > 50) {
			return text.substring(0, 50) + "...";
		} else {
			return text;
		}
	}

	@Override
	public ProfileSet getLikers() {
		return likers;
	}
}

package com.velik.comments;

import java.util.Date;

import com.velik.comments.pojo.ProfileSetPojo;

public interface Posting extends Valued, ModelObject<PostingId> {

	PostingId getId();

	Date getDate();

	String getText();

	String getSummarizedText();

	ProfileId getPosterId();

	ProfileSet getLikers();

	public static Posting EMPTY = new Posting() {

		@Override
		public int getPoints() {
			return 0;
		}

		@Override
		public Valuation value(ValuationType type, int points, ProfileId valuer) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PostingId getId() {
			return new PostingId(0);
		}

		@Override
		public Date getDate() {
			return new Date();
		}

		@Override
		public String getText() {
			return "";
		}

		@Override
		public String getSummarizedText() {
			return "";
		}

		@Override
		public ProfileId getPosterId() {
			return ProfileId.ANONYMOUS;
		}

		@Override
		public ProfileSet getLikers() {
			return new ProfileSetPojo();
		}
	};
}

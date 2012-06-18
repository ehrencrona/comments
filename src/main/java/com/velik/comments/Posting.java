package com.velik.comments;

import java.util.Date;

public interface Posting extends Valued, ModelObject<PostingId> {

	PostingId getId();

	Date getDate();

	String getText();

	String getSummarizedText();

	ProfileId getPosterId();

	ProfileSet getLikers();

}

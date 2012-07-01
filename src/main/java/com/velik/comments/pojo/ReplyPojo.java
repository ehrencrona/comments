package com.velik.comments.pojo;

import com.velik.comments.Finder;
import com.velik.comments.ProfileId;
import com.velik.comments.Reply;

public class ReplyPojo extends PostingPojo implements Reply {
	private static final long serialVersionUID = 1;

	public ReplyPojo(ProfileId posterId, Finder finder) {
		super(posterId, finder);
	}

}

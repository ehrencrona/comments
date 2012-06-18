package com.velik.comments.pojo;

import com.velik.comments.Finder;
import com.velik.comments.ProfileId;
import com.velik.comments.Reply;

public class ReplyPojo extends PostingPojo implements Reply {

	public ReplyPojo(Finder finder, ProfileId posterId) {
		super(finder, posterId);
	}

}

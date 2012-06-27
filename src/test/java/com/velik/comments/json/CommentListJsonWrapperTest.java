package com.velik.comments.json;

import junit.framework.Assert;

import org.junit.Test;

import com.velik.comments.AbstractTest;
import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Profile;

public class CommentListJsonWrapperTest extends AbstractTest {

	@Test
	public void testCommentWithReply() {
		CommentList cl = getFinder().createCommentList(new CommentListId("foo"));

		Profile poster = getFinder().createProfile("profile");

		Comment comment = cl.comment("foo", poster.getId());

		Profile replier = getFinder().createProfile("f1");
		comment.reply("reply", replier.getId());

		Assert.assertEquals(("{'c':[['2','full','','foo',1,[[],0],[['4','full','','reply',3,[[],0]]]]],"
				+ "'p':{1:['profile'],3:['f1']}}").replaceAll("'", "\""), new CommentListJsonWrapper(cl, poster,
				getFinder()).toJson());
	}
}

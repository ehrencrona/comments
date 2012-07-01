package com.velik.comments.json;

import junit.framework.Assert;

import org.junit.Test;

import com.velik.comments.AbstractTest;
import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Profile;
import com.velik.comments.size.FixedCommentSizeCalculator;

public class CommentListJsonWrapperTest extends AbstractTest {

	@Test
	public void testCommentWithReply() {
		CommentList cl = getFinder().createCommentList(new CommentListId("foo"));

		Profile poster = getFinder().createProfile("profile");

		Comment comment = cl.comment("foo", poster.getId());

		Profile replier = getFinder().createProfile("f1");
		comment.reply("reply", replier.getId());

		FixedCommentSizeCalculator size = FixedCommentSizeCalculator.FULL;

		CommentListJsonWrapper wrapper = new CommentListJsonWrapper(cl, poster, size, size);

		Assert.assertEquals(
				("[['2','full','','foo',1,[[],0],[['4','full','','reply',3,[[],0]]]]]").replaceAll("'", "\""),
				wrapper.toJson());

		Assert.assertEquals(("[[1,'profile'],[3,'f1']]").replace("'", "\""),
				new ProfileSetJsonWrapper(wrapper.getProfileCollector(), getFinder()).toJson());
	}
}

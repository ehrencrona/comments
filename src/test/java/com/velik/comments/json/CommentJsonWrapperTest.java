package com.velik.comments.json;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.velik.comments.AbstractTest;
import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Profile;
import com.velik.comments.ValuationType;
import com.velik.comments.pojo.ReplyPojo;

public class CommentJsonWrapperTest extends AbstractTest {

	private Comment comment;
	private Profile poster;

	@Before
	public void setUp() {
		CommentList cl = getFinder().createCommentList(new CommentListId("foo"));

		poster = getFinder().createProfile("profile");

		comment = cl.comment("foo", poster.getId());
	}

	@Test
	public void testLikes() {
		Profile favorite1 = getFinder().createProfile("f1");
		like(favorite1);
		poster.addFavorite(favorite1.getId());

		Profile favorite2 = getFinder().createProfile("f2");
		like(favorite2);
		poster.addFavorite(favorite2.getId());

		Profile nonFavorite1 = getFinder().createProfile("nf1");
		like(nonFavorite1);

		Profile nonFavorite2 = getFinder().createProfile("nf2");
		like(nonFavorite2);

		Assert.assertEquals(("['" + comment.getId() + "','full','','foo'," + poster.getId() + ",[[" + favorite1.getId()
				+ "," + favorite2.getId() + "],2],[]]").replaceAll("'", "\""), new CommentJsonWrapper(comment,
				PostingSize.FULL, poster).toJson());
	}

	protected void like(Profile liker) {
		comment.value(ValuationType.LIKE, 1, liker.getId());
	}

	@Test
	public void testCommentWithReplies() {
		Profile replier = getFinder().createProfile("replier");

		ReplyPojo reply = (ReplyPojo) comment.reply("answer", replier.getId());

		reply.setPoints(0);

		Assert.assertEquals(
				("['" + comment.getId() + "','full','','foo'," + poster.getId() + ",[[],0],[['" + reply.getId() + "']]]")
						.replaceAll("'", "\""), new CommentJsonWrapper(comment, PostingSize.FULL, poster).toJson());
	}

	@Test
	public void testBasicCommentFull() {
		Assert.assertEquals(
				("['" + comment.getId() + "','full','','foo'," + poster.getId() + ",[[],0],[]]").replaceAll("'", "\""),
				new CommentJsonWrapper(comment, PostingSize.FULL, poster).toJson());
	}

	@Test
	public void testBasicCommentShort() {
		Assert.assertEquals(("['" + comment.getId() + "','short','foo']").replaceAll("'", "\""),
				new CommentJsonWrapper(comment, PostingSize.SHORT, poster).toJson());
	}

	@Test
	public void testBasicCommentHidden() {
		Assert.assertEquals(("['" + comment.getId() + "']").replaceAll("'", "\""), new CommentJsonWrapper(comment,
				PostingSize.HIDDEN, poster).toJson());
	}
}

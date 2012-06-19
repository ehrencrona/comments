package com.velik.comments;

import static junit.framework.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.velik.comments.pojo.PostingListPojo;

public class CommentTest extends AbstractTest {

	private Comment comment;
	private Profile poster;

	@Before
	public void setUp() {
		poster = getFinder().createProfile("valuer");
		comment = getFinder().createCommentList(new CommentListId("foo")).comment("foo", poster.getId());
	}

	@Test
	public void testGetComment() throws Exception {
		Assert.assertEquals(comment, getFinder().getPosting(comment.getId()));
	}

	@Test
	public void testValuation() {
		Profile valuer = getFinder().createProfile("valuer");

		Valuation valuation = comment.value(ValuationType.LIKE, 10, valuer.getId());

		assertTrue(comment.getPoints() > 0);

		assertTrue(comment.getLikers().contains(valuer.getId()));

		assertIterableEquals(valuer.getGivenValuations(), valuation);
	}

	@Test
	public void testGetReplies() {
		Reply reply = comment.reply("foobar", poster.getId());

		PostingListPojo<Reply> exclude = new PostingListPojo<Reply>(getFinder());

		assertIterableEquals(comment.getReplies(exclude), reply);

		exclude.add(new PostingId(randomInt()));

		assertIterableEquals(comment.getReplies(exclude), reply);

		exclude.add(reply.getId());

		assertIterableEquals(comment.getReplies(exclude));
	}

	@Test
	public void testGetPosting() throws Exception {
		Reply reply = comment.reply("foobar", poster.getId());

		Assert.assertEquals(reply, getFinder().getPosting(reply.getId()));
	}

	@Test
	public void testRepliesInvolvingFavorites() {
		Reply reply = comment.reply("foobar", poster.getId());

		@SuppressWarnings("unused")
		Reply nonPosterReply = comment.reply("foobar", new ProfileId(randomInt()));

		Profile profile = getFinder().createProfile("foo");

		assertIterableEmpty(comment.getRepliesInvolvingFavorites(profile));

		profile.addFavorite(poster.getId());

		assertIterableEquals(comment.getRepliesInvolvingFavorites(profile), reply);
	}
}

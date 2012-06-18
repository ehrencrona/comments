package com.velik.comments;

import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ReplyTest extends AbstractTest {

	private Comment comment;
	private Reply reply;
	private Profile poster;

	@Before
	public void setUp() {
		poster = getFinder().createProfile("valuer");
		comment = getFinder().createCommentList(new CommentListId("foo")).comment("foo", poster.getId());
		reply = comment.reply("foo", poster.getId());
	}

	@Test
	public void testValuation() {
		Profile valuer = getFinder().createProfile("valuer");

		Valuation valuation = reply.value(ValuationType.LIKE, 10, valuer.getId());

		assertTrue(reply.getPoints() > 0);

		assertTrue(reply.getLikers().contains(valuer.getId()));

		assertIterableEquals(valuer.getGivenValuations(), valuation);

		assertIterableEquals(poster.getReceivedValuations(-1), valuation);

	}

}

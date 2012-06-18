package com.velik.comments;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProfileTest extends AbstractTest {

	private Profile profile;

	@Before
	public void setUp() {
		profile = getFinder().createProfile("alias");
	}

	@Test
	public void testValuation() {
		Profile valuer = getFinder().createProfile("valuer");

		Valuation valuation = profile.value(ValuationType.LIKE, 10, valuer.getId());

		Assert.assertTrue(profile.getPoints() > 0);

		assertIterableEquals(profile.getReceivedValuations(-1), valuation);

		assertIterableEquals(valuer.getGivenValuations(), valuation);
	}

	@Test
	public void testFavorites() {
		Profile favoriteProfile = getFinder().createProfile("alias2");

		profile.addFavorite(favoriteProfile.getId());

		Assert.assertTrue(profile.getFavorites().contains(favoriteProfile.getId()));

		Assert.assertTrue(favoriteProfile.getFavoriteOf().contains(profile.getId()));
		Assert.assertFalse(profile.getFavorites().contains(new ProfileId(randomInt())));
	}

	@Test
	public void testNewsfeedContainsComment() {
		assertIterableEmpty(profile.getNewsfeed(Integer.MAX_VALUE));

		Profile favorite = getFinder().createProfile("favorite");

		profile.addFavorite(favorite.getId());

		CommentList cl = getFinder().createCommentList(new CommentListId(randomString()));

		Comment comment = cl.comment("foobar", favorite.getId());

		assertIterableEquals(profile.getNewsfeed(Integer.MAX_VALUE), comment);
	}

	@Test
	public void testNewsfeedContainsReply() {
		assertIterableEmpty(profile.getNewsfeed(Integer.MAX_VALUE));

		CommentList cl = getFinder().createCommentList(new CommentListId(randomString()));

		Comment comment = cl.comment("foobar", profile.getId());

		Reply otherReply = comment.reply("otherreply", new ProfileId(randomInt()));
		@SuppressWarnings("unused")
		Reply myReply = comment.reply("myreply", profile.getId());

		assertIterableEquals(profile.getNewsfeed(Integer.MAX_VALUE), otherReply);
	}

	@Test
	public void testOwnPostings() {
		assertIterableEmpty(profile.getOwnPostings());

		CommentList cl = getFinder().createCommentList(new CommentListId(randomString()));

		Comment comment = cl.comment("foobar", profile.getId());

		@SuppressWarnings("unused")
		Reply otherReply = comment.reply("bar", new ProfileId(randomInt()));
		Reply myReply = comment.reply("bar", profile.getId());

		assertIterableEquals(profile.getOwnPostings(), myReply, comment);
	}
}

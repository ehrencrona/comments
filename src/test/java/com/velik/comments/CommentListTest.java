package com.velik.comments;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.velik.comments.pojo.PostingListPojo;

public class CommentListTest extends AbstractTest {
	private CommentList cl;
	private ProfileId poster;
	private CommentListId id = new CommentListId("foo");

	@Before
	public void setUp() {
		cl = getFinder().createCommentList(id);
		poster = new ProfileId(randomInt());
	}

	@Test
	public void testFinder() throws Exception {
		Assert.assertEquals(cl, getFinder().getCommentList(id));
	}

	@Test
	public void testComment() throws Exception {
		Comment c = cl.comment("foo", new ProfileId(randomInt()));

		Assert.assertTrue(cl.contains(c.getId()));

		assertIterableEquals(cl, c);

		Assert.assertEquals(c, getFinder().getPosting(c.getId()));
	}

	@Test
	public void testGetComments() {
		Comment c1 = cl.comment("foobar", poster);
		Comment c2 = cl.comment("foobar", poster);

		PostingListPojo<Comment> exclude = new PostingListPojo<Comment>(getFinder());

		assertIterableEquals(cl.getComments(exclude, 10), c1, c2);

		exclude.add(new PostingId(randomInt()));

		assertIterableEquals(cl.getComments(exclude, 10), c1, c2);

		exclude.add(c1.getId());

		assertIterableEquals(cl.getComments(exclude, 10), c2);

		exclude = new PostingListPojo<Comment>(getFinder());
		exclude.add(c2.getId());

		assertIterableEquals(cl.getComments(exclude, 10), c1);
	}

	@Test
	public void testRepliesInvolvingFavorites() {
		Comment c = cl.comment("foobar", poster);

		@SuppressWarnings("unused")
		Comment nonPosterComment = cl.comment("foobar", new ProfileId(randomInt()));

		Profile profile = getFinder().createProfile("foo");

		assertIterableEmpty(cl.getCommentsInvolvingFavorites(profile));

		profile.addFavorite(poster);

		assertIterableEquals(cl.getCommentsInvolvingFavorites(profile), c);
	}

}

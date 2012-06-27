package com.velik.comments.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.CommentListId;
import com.velik.comments.Profile;

public class TestSerialize {

	@Test
	public void testSerializeEmptyFinder() throws Exception {
		serializeAndDeserialize(new FinderPojo());
	}

	@Test
	public void testSerializeId() throws Exception {
		Assert.assertEquals("foo", ((CommentListId) serializeAndDeserialize(new CommentListId("foo"))).toString());
	}

	@Test
	public void testSerializeFullFinder() throws Exception {
		FinderPojo finder = new FinderPojo();

		Profile profile = finder.createProfile("user");
		CommentList cl = finder.createCommentList(new CommentListId("foobar"));

		Comment comment = cl.comment("foobar", profile.getId());

		comment.reply("reply", profile.getId());

		serializeAndDeserialize(finder);
	}

	protected Object serializeAndDeserialize(Object object) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		new ObjectOutputStream(baos).writeObject(object);

		return new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
	}
}

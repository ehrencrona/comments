package com.velik.comments;

import java.util.Iterator;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;

import com.velik.comments.pojo.FinderPojo;

public class AbstractTest {
	private Finder finder;

	@Before
	public void setUpFinder() {
		finder = new FinderPojo();
	}

	protected Finder getFinder() {
		return finder;
	}

	protected int randomInt() {
		return Math.abs(new Random().nextInt());
	}

	protected String randomString() {
		StringBuffer result = new StringBuffer(12);
		Random rnd = new Random();

		for (int i = 0; i < 12; i++) {
			result.append('a' + (rnd.nextInt() % 27));
		}

		return result.toString();
	}

	protected void assertIterableEmpty(Iterable<?> iterable) {
		Assert.assertFalse(iterable.iterator().hasNext());
	}

	protected <T> void assertIterableEquals(Iterable<T> iterable, T... objects) {
		Iterator<T> it = iterable.iterator();

		int at = 0;

		for (T object : objects) {
			Assert.assertTrue("List was only " + at + " elements long.", it.hasNext());

			Assert.assertEquals("Object " + at + " was not equal.", object, it.next());

			at++;
		}

		Assert.assertFalse(it.hasNext());
	}

}

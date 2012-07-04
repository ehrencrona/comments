package com.velik.comments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.velik.comments.exception.NoSuchProfileException;
import com.velik.comments.json.JsonArray;
import com.velik.comments.json.JsonMap;
import com.velik.comments.json.JsonParser;
import com.velik.comments.json.NoSuchValueException;
import com.velik.comments.json.ParseException;
import com.velik.comments.pojo.CommentPojo;
import com.velik.comments.pojo.FinderPojo;
import com.velik.comments.pojo.ReplyPojo;
import com.velik.comments.pojo.SerializedPojoFinder;

public class ImportComments {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			JsonArray comments = (JsonArray) new JsonParser(
					new BufferedReader(new FileReader(new File("esm.json"))).readLine()).parse();

			SerializedPojoFinder finder = new SerializedPojoFinder();

			finder.initalize();

			Random random = new Random();

			CommentList cl = finder.createCommentList(new CommentListId("singleton"));

			for (Object o : comments) {
				JsonMap comment = (JsonMap) o;

				ProfileId profileId = getProfileId(comment, finder);

				CommentPojo commentPojo = (CommentPojo) cl.comment(getText(comment), profileId);
				commentPojo.setPoints(Math.abs(random.nextInt(250)));

				like(commentPojo, finder.getDelegate());

				for (Object r : (JsonArray) comment.get("replies")) {
					JsonMap reply = (JsonMap) r;

					profileId = getProfileId(reply, finder);

					ReplyPojo replyPojo = (ReplyPojo) commentPojo.reply(getText(reply), profileId);

					replyPojo.setPoints(Math.abs(random.nextInt(250)));

					like(replyPojo, finder.getDelegate());
				}
			}

			finder.persist();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void like(Valued valued, FinderPojo finder) {
		List<ProfileId> profiles = new ArrayList<ProfileId>(100);

		for (Profile profile : finder.getProfiles()) {
			profiles.add(profile.getId());
		}

		Random random = new Random();

		int likes = random.nextInt((int) Math.sqrt(profiles.size()));

		likes = Math.max(likes * likes - random.nextInt(6), 0);

		System.out.println(likes);

		for (int i = 0; i < likes; i++) {
			ProfileId valuer = profiles.get(random.nextInt(profiles.size()));
			profiles.remove(valuer);

			valued.value(ValuationType.LIKE, 0, valuer);
		}
	}

	protected static String getText(JsonMap reply) throws NoSuchValueException {
		String title = ((String) reply.get("title")).trim();
		String text = ((String) reply.get("text")).trim();

		if (title.endsWith("...")) {
			title = title.substring(0, title.length() - 3).trim();
		}

		if (title.startsWith("...")) {
			text = text.substring(3).trim();
		}

		if (text.length() > 0 && Character.isLowerCase(text.charAt(0))) {
			text = title + " " + text;
		}

		return text;
	}

	private static ProfileId getProfileId(JsonMap comment, Finder finder) throws NoSuchValueException {
		String alias = (String) comment.get("author");

		try {
			return finder.getProfile(alias);
		} catch (NoSuchProfileException e) {
			return finder.createProfile(alias).getId();
		}

	}

}

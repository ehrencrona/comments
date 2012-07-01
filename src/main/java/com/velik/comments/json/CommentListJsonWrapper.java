package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

import com.velik.comments.Comment;
import com.velik.comments.CommentList;
import com.velik.comments.PostingList;
import com.velik.comments.Profile;
import com.velik.comments.ProfileSet;
import com.velik.comments.pojo.ProfileSetPojo;
import com.velik.comments.size.CommentSizeCalculator;

public class CommentListJsonWrapper extends JsonObject {
	private CommentList commentList;
	protected Profile profile;
	protected ProfileSet profileCollector = new ProfileSetPojo();

	private CommentSizeCalculator nonFavoriteSizeCalculator;
	private CommentSizeCalculator favoriteSizeCalculator;

	public CommentListJsonWrapper(CommentList commentList, Profile profile,
			CommentSizeCalculator favoriteSizeCalculator, CommentSizeCalculator nonFavoriteSizeCalcualtor) {
		this.commentList = commentList;
		this.profile = profile;

		this.nonFavoriteSizeCalculator = nonFavoriteSizeCalcualtor;
		this.favoriteSizeCalculator = favoriteSizeCalculator;
	}

	@Override
	public void print(Writer writer) throws IOException {
		JsonArray comments = array();

		PostingList<Comment> commentsInvolvingFavorites = commentList.getCommentsInvolvingFavorites(profile);

		for (Comment comment : commentsInvolvingFavorites) {
			add(comment, comments, favoriteSizeCalculator);
		}

		for (Comment comment : commentList.getComments(commentsInvolvingFavorites, -1)) {
			add(comment, comments, nonFavoriteSizeCalculator);
		}

		comments.print(writer);
	}

	protected void add(Comment comment, JsonArray comments, CommentSizeCalculator sizeCalculator) {
		CommentJsonWrapper commentWrapper = new CommentJsonWrapper(comment, profile, sizeCalculator);

		commentWrapper.setProfileCollector(profileCollector);

		comments.add(commentWrapper);
	}

	public void setProfileCollector(ProfileSet profileSet) {
		this.profileCollector = profileSet;
	}

	public ProfileSet getProfileCollector() {
		return profileCollector;
	}
}

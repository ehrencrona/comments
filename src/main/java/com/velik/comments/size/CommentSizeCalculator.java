package com.velik.comments.size;

import com.velik.comments.Comment;
import com.velik.comments.Profile;

public interface CommentSizeCalculator {
	CommentSize calculate(Comment posting, Profile profile);
}

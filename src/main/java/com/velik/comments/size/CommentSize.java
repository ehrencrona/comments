package com.velik.comments.size;

import com.velik.comments.json.PostingSize;

public interface CommentSize extends ReplySizeCalculator {
	PostingSize getPostingSize();
}

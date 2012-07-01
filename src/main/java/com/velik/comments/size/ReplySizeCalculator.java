package com.velik.comments.size;

import com.velik.comments.Profile;
import com.velik.comments.Reply;
import com.velik.comments.json.PostingSize;

public interface ReplySizeCalculator {
	PostingSize calculate(Reply posting, Profile profile);
}

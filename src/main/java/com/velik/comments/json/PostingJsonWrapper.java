package com.velik.comments.json;

import java.io.IOException;
import java.io.Writer;

import com.velik.comments.Posting;

public class PostingJsonWrapper<P extends Posting> extends JsonObject {

	protected P posting;

	public PostingJsonWrapper(P posting) {
		this.posting = posting;
	}

	@Override
	public void print(Writer writer) throws IOException {
		toJsonObject().print(writer);
	}

	protected JsonArray toJsonObject() {
		return new JsonArray(posting.getId(), "full", posting.getSummarizedText(), posting.getText(), array(
				posting.getPosterId(), "poster name"));
	}

}

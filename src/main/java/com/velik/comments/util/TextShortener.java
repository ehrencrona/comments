package com.velik.comments.util;

public class TextShortener {

	private String text;

	public TextShortener(String text) {
		this.text = text;
	}

	public String shorten(int length) {
		StringBuffer result = new StringBuffer();

		if (text.length() <= length) {
			return text;
		}

		int i = length - 20;

		result.append(text.substring(0, i));

		int lastSpace = -1;

		while (text.charAt(i) != '.' && i < length + 20 && i < text.length()) {
			if (text.charAt(i) == ' ') {
				lastSpace = i;
			}

			result.append(text.charAt(i++));
		}

		if (text.charAt(i) != '.' && lastSpace != -1) {
			result.setLength(lastSpace);
		}

		if (i == text.length()) {
			return text;
		}

		return result.toString() + "...";
	}

}

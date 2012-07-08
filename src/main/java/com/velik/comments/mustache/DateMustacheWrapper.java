package com.velik.comments.mustache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateMustacheWrapper extends Date {
	private static SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	private static DateFormat userFormat = DateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,
			SimpleDateFormat.SHORT, Locale.GERMANY);

	static {
		utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public DateMustacheWrapper(Date date) {
		super((date != null ? date.getTime() : System.currentTimeMillis()));
	}

	public String getUserFormatted() {
		return userFormat.format(this);
	}

	public String getUTCFormatted() {
		return utcFormat.format(this);
	}
}

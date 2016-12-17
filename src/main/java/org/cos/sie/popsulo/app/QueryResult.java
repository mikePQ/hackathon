package org.cos.sie.popsulo.app;

import java.util.Date;

public class QueryResult {

	public QueryResult(String videoId, String title, String author, Date publishingDate) {
		this.videoId = videoId;
		this.title = title;
		this.author = author;
		this.publishingDate = publishingDate;
	}

	private String videoId;
	private String title;
	private String author;
	private Date publishingDate;

	public String getVideoId() {
		return videoId;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public Date getPublishingDate() {
		return publishingDate;
	}
}

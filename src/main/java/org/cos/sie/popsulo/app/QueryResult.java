package org.cos.sie.popsulo.app;

import javafx.scene.image.Image;

import java.util.Date;

public class QueryResult {

	public QueryResult(String videoId, String title, String author, Date publishingDate, Image miniature, String fileUrl) {
		this.videoId = videoId;
		this.title = title;
		this.author = author;
		this.publishingDate = publishingDate;
		this.miniature = miniature;
		this.fileUrl = fileUrl;
	}

	private String videoId;
	private String title;
	private String author;
	private Date publishingDate;
	private Image miniature;
	private String fileUrl;

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

	public Image getMiniature() {
		return miniature;
	}

	public String getFileUrl() {
		return fileUrl;
	}
}

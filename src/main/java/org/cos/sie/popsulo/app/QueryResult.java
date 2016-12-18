package org.cos.sie.popsulo.app;

import javafx.scene.image.Image;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryResult {

	public QueryResult(String videoId, String title, String author, Date publishingDate, Image miniature, String fileUrl) {
		this.videoId = videoId;
		this.title = title;
		this.author = author;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		this.publishingDate = format.format(publishingDate);
		this.miniature = miniature;
		this.fileUrl = fileUrl;
	}
	public QueryResult(){
   };

	private String videoId;
	private String title;
	private String author;
	private String publishingDate;
	private transient Image miniature;
	private transient String fileUrl;
	private transient Boolean cached;

	public Boolean getCached() {
		return cached;
	}

	public void setCached(Boolean cached) {
		this.cached = cached;
	}

	private transient String miniatureUrl;

	public String getMiniatureUrl() {
		return miniatureUrl;
	}

	public void setMiniatureUrl(String miniatureUrl) {
		this.miniatureUrl = miniatureUrl;
	}

	public String getVideoId() {
		return videoId;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublishingDate() {
		return publishingDate;
	}

	public Image getMiniature() {
		return miniature;
	}

	public void setMiniature(Image miniature) {
		this.miniature = miniature;
	}

	public String getFileUrl() {
		return fileUrl;
	}
}

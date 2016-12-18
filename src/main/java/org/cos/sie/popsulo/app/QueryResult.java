package org.cos.sie.popsulo.app;

import javafx.scene.image.Image;
import org.cos.sie.popsulo.LocalDiskCache;

import java.io.File;
import java.net.MalformedURLException;
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
		this.numberOfViews = 1;
	}
	public QueryResult(){
   };

	private String videoId;
	private String title;
	private String author;
	private String publishingDate;
    private String miniatureUrl;
	private int numberOfViews;
	private transient Image miniature;
	private transient String fileUrl;
	private transient Boolean cached;

	public Boolean getCached() {
		return cached;
	}

	public void setCached(Boolean cached) {
		this.cached = cached;
	}

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

	public int getNumberOfViews() {
		return numberOfViews;
	}

	public void incNoOfViews() {
		++numberOfViews;
	}

	public Image getMiniature() {
		return miniature;
	}

	public void setMiniature(Image miniature) {
		this.miniature = miniature;
	}

	public String getFileCache()
		throws MalformedURLException
	{
		if (LocalDiskCache.getInstance().isQueryResultInCache(videoId)) {
				File file = new File(LocalDiskCache.ldcPATH + "/" + videoId + ".mp3");
			    return file.toURI().toURL().toString();
		}
		return null;
	}

    public void initCachedState()
    {
        miniature = new Image(miniatureUrl);
        cached = true;
    }
}

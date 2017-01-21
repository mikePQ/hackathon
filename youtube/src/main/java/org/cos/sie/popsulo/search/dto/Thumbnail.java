package org.cos.sie.popsulo.search.dto;

import javafx.scene.image.Image;

public class Thumbnail {
	private String url;
	private Integer width;
	private Integer height;

	private Image thumbnail;

	public String getUrl() {
		return url;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public Image getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}
}

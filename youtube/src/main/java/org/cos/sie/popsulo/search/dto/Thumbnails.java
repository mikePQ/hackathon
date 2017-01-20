package org.cos.sie.popsulo.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Thumbnails {

	@JsonProperty("default")
	private Thumbnail defaultThumbnail;
	private Thumbnail medium;
	private Thumbnail high;

	public Thumbnail getDefaultThumbnail() {
		return defaultThumbnail;
	}

	public Thumbnail getMedium() {
		return medium;
	}

	public Thumbnail getHigh() {
		return high;
	}
}

package org.cos.sie.popsulo.search.dto;

import javafx.scene.image.Image;

public class VideoInfo {
	private String kind;
	private String etag;
	private Identifier id;
	private Snippet snippet;

	public String getKind() {
		return kind;
	}

	public String getEtag() {
		return etag;
	}

	public Identifier getId() {
		return id;
	}

	public Snippet getSnippet() {
		return snippet;
	}

	public Image getMiniature() {
		return snippet.getThumbnails().getDefaultThumbnail().getThumbnail();
	}

	public String getVideoId() {
		return id.getVideoId();
	}

	public String getTitle() {
		return snippet.getTitle();
	}

	public String getAuthor() {
		return getSnippet().getChannelTitle();
	}
}

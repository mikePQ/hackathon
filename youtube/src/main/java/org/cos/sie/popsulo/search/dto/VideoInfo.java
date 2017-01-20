package org.cos.sie.popsulo.search.dto;

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
}

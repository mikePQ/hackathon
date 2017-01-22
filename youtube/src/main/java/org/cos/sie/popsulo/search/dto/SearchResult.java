package org.cos.sie.popsulo.search.dto;

import java.util.List;

public class SearchResult {
	private String kind;
	private String etag;
	private String nextPageToken;
	private String regionCode;
	private PageInfo pageInfo;

	private List<VideoResult> items;

	public String getKind() {
		return kind;
	}

	public String getEtag() {
		return etag;
	}

	public String getNextPageToken() {
		return nextPageToken;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public List<VideoResult> getItems() {
		return items;
	}
}

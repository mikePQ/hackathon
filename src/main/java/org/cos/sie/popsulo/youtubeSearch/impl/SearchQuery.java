package org.cos.sie.popsulo.youtubeSearch.impl;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.List;

public class SearchQuery {
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private YouTube.Search.List search;

	private final static String retrievedFields =
			"items(" + "id/kind," + "id/videoId," + "id/channelId," + "snippet/title," + " snippet/channelTitle,"
					+ " snippet/publishedAt," + " snippet/thumbnails/default/url)";

	private static final String fileName = "YouTubeCache";

	private static final String searchedObjectType = "video";

	private final static String apiKey = "AIzaSyBnfhEl2amzE41PfLOkQFe4WxvkrDlgtbY";

	public SearchQuery() {
		YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request)
					throws IOException {
				//nothing happens when request is initalized
			}
		}).setApplicationName(fileName).build();

		try {
			search = youtube.search().list("id,snippet");
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		search.setKey(apiKey);
		search.setType(searchedObjectType);
		search.setFields(retrievedFields);
	}

	//" Video Id" getLink using : "https://www.youtube.com/watch?v="+ rId.getVideoId()
	// " Title: " + singleVideo.getSnippet().getTitle()
	// "Channel title " + singleVideo.getSnippet().getChannelTitle()
	// "Published Date" + singleVideo.getSnippet().getPublishedAt());

	public List<SearchResult> searchForVideos(String queryTerm, long numberOfVideos)
			throws IOException {
		search.setMaxResults(numberOfVideos);
		search.setQ(queryTerm);
		SearchListResponse searchResponse = search.execute();
		return searchResponse.getItems();
	}

}

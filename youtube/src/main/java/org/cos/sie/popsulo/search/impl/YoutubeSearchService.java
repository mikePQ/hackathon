package org.cos.sie.popsulo.search.impl;

import javafx.scene.image.Image;
import org.cos.sie.popsulo.search.SearchService;
import org.cos.sie.popsulo.search.dto.SearchResult;
import org.cos.sie.popsulo.search.dto.Thumbnail;
import org.cos.sie.popsulo.search.dto.Thumbnails;
import org.cos.sie.popsulo.search.dto.VideoInfo;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class YoutubeSearchService implements SearchService {

	private static final Logger logger = LoggerFactory.getLogger(YoutubeSearchService.class);

	private static final String BASE_RESOURCE_NAME = "https://www.googleapis.com/youtube/v3";
	private static final String PATH = "search";
	private static final String API_KEY = "AIzaSyBnfhEl2amzE41PfLOkQFe4WxvkrDlgtbY";
	private static final String QUERIED_PARTS = "id,snippet";

	public YoutubeSearchService() {
		wsClient = ClientBuilder.newClient();
		wsClient.register(JacksonFeature.class);
	}
	private Client wsClient;

	@Override
	public List<VideoInfo> searchYoutube(String query) {
		logger.info("Requested query for: " + query);
		WebTarget webTarget = wsClient.target(BASE_RESOURCE_NAME).path(PATH);

		Response response = webTarget
				.queryParam("part", QUERIED_PARTS)
				.queryParam("key", API_KEY)
				.queryParam("q", query)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		logger.info("Finished query");

		SearchResult result = response.readEntity(SearchResult.class);
		downloadThumbnails(result);

		List<VideoInfo> items = result.getItems();

		logger.info("Returned " + items.size() + " elements");
		return items;
	}


	private void downloadThumbnails(SearchResult searches) {
		List<VideoInfo> videos = searches.getItems();

		for ( VideoInfo video : videos ) {
			fillThumbnails(video);
		}
	}

	private void fillThumbnails(VideoInfo video) {
		Thumbnails thumbnails = video.getSnippet().getThumbnails();
		downloadThumbnail(thumbnails.getDefaultThumbnail());
		downloadThumbnail(thumbnails.getMedium());
		downloadThumbnail(thumbnails.getHigh());
	}

	private void downloadThumbnail(Thumbnail defaultThumbnail) {
		String thumbnailUrl = defaultThumbnail.getUrl();
		Image thumbnail = new Image(thumbnailUrl);
		defaultThumbnail.setThumbnail(thumbnail);
	}
}

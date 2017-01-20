package org.cos.sie.popsulo.search.impl;

import org.cos.sie.popsulo.search.SearchService;
import org.cos.sie.popsulo.search.dto.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class YoutubeSearchService implements SearchService {

	private static final Logger logger = LoggerFactory.getLogger(YoutubeSearchService.class);

	private static final String BASE_RESOURCE_NAME = "https://www.googleapis.com/youtube/v3";
	private static final String PATH = "search";
	private static final String API_KEY = "AIzaSyBnfhEl2amzE41PfLOkQFe4WxvkrDlgtbY";

	private Client wsClient = ClientBuilder.newClient();

	@Override
	public SearchResult searchYoutube(String query) {
		WebTarget webTarget = wsClient.target(BASE_RESOURCE_NAME).path(PATH);

		Response response = webTarget
				.queryParam("part", "id,snippet")
				.queryParam("key", API_KEY)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		return response.readEntity(SearchResult.class);
	}


//		jersey 1.x version
//	WebResource resource = wsClient.resource(BASE_RESOURCE_NAME);
//		ClientResponse clientResponse = resource
//				.queryParam("part", "id,snippet")
//				.queryParam("key", API_KEY)
//				.queryParam("chart", "mostPopular")
//				.accept(MediaType.APPLICATION_JSON)
//				.get(ClientResponse.class);
//
//		String responseString = clientResponse.getEntity(String.class);
//
//		System.out.println(responseString);

}

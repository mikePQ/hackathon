package org.cos.sie.popsulo.youtubeSearch.impl;

import com.google.api.services.youtube.model.SearchResult;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.youtubeSearch.SearchQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultSearchQueryService implements SearchQueryService {

	private static final Logger logger = LoggerFactory.getLogger(DefaultSearchQueryService.class);


	private static final int DEFAULT_NUMBER_COUNT = 20;


	private SearchQuery searchQuery;

	public DefaultSearchQueryService() {
		searchQuery = new SearchQuery();
	}

	@Override
	public List<QueryResult> queryYoutube(String queryString) throws IOException {
		try {
			List<SearchResult> youTubeResults = searchQuery.searchForVideos(queryString, DEFAULT_NUMBER_COUNT);
			return youTubeResults.stream()
					.map(DefaultSearchQueryService::convertToQueryResult)
					.collect(Collectors.toList());
		} catch ( IOException exc ) {
			logger.error("Failed to call youtube service - IOException caught");
			throw exc;
		}
	}

	private static QueryResult convertToQueryResult(SearchResult searchResult) {
		String videoId = searchResult.getId().getVideoId();
		String title = searchResult.getSnippet().getTitle();
		String channel = searchResult.getSnippet().getChannelTitle();
		Date publishedDate = new Date(searchResult.getSnippet().getPublishedAt().getValue());

		return new QueryResult(videoId, title, channel, publishedDate);
	}

}

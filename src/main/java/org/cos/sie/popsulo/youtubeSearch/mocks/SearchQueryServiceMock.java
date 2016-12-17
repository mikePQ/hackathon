package org.cos.sie.popsulo.youtubeSearch.mocks;

import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.youtubeSearch.SearchQueryService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SearchQueryServiceMock implements SearchQueryService {

	private static final List<QueryResult> exampleQueryResults = new ArrayList<>();

	static {
		exampleQueryResults.add(new QueryResult("1","asdf", "ziutek", new Date()));
		exampleQueryResults.add(new QueryResult("2", "koty", "janusz", new Date()));
		exampleQueryResults.add(new QueryResult("3","koteczki", "janusz", new Date()));
		exampleQueryResults.add(new QueryResult("4","kotełki", "kazik", new Date()));
		exampleQueryResults.add(new QueryResult("5", "heheszki", "ziutek", new Date()));
	}

	@Override
	public List<QueryResult> queryYoutube(String queryString) {
		return exampleQueryResults.stream()
				.filter(queryResult -> queryResult.getTitle().startsWith(queryString))
				.collect(Collectors.toList());
	}
}

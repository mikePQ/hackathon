package org.cos.sie.popsulo.mocks;

import org.cos.sie.popsulo.QueryResult;
import org.cos.sie.popsulo.SearchQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchQueryServiceMock implements SearchQueryService {

	private static final List<QueryResult> exampleQueryResults = new ArrayList<>();

	static {
		exampleQueryResults.add(new QueryResult("asdf", "ziutek"));
		exampleQueryResults.add(new QueryResult("koty", "janusz"));
		exampleQueryResults.add(new QueryResult("koteczki", "janusz"));
		exampleQueryResults.add(new QueryResult("kotełki", "kazik"));
		exampleQueryResults.add(new QueryResult("heheszki", "ziutek"));
	}

	@Override
	public List<QueryResult> queryYoutube(String queryString) {
		return exampleQueryResults.stream()
				.filter(queryResult -> queryResult.getTitle().startsWith(queryString))
				.collect(Collectors.toList());
	}
}

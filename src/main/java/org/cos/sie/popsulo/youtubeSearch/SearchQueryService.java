package org.cos.sie.popsulo.youtubeSearch;

import org.cos.sie.popsulo.app.QueryResult;

import java.io.IOException;
import java.util.List;

public interface SearchQueryService {

	List<QueryResult> queryYoutube(String queryString) throws IOException;
}

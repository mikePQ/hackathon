package org.cos.sie.popsulo.search;


import org.cos.sie.popsulo.search.dto.VideoResult;

import java.util.List;

public interface SearchService {

	List<VideoResult> searchYoutube(String query);
}

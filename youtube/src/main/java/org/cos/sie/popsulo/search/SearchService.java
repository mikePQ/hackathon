package org.cos.sie.popsulo.search;


import org.cos.sie.popsulo.search.dto.VideoInfo;

import java.util.List;

public interface SearchService {

	List<VideoInfo> searchYoutube(String query);
}

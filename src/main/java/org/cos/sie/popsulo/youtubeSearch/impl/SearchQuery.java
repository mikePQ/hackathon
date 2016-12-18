package org.cos.sie.popsulo.youtubeSearch.impl;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import org.cos.sie.popsulo.Auth;
import org.cos.sie.popsulo.LocalDiskCache;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SearchQuery
{
    private YouTube youtube;

    private YouTube.Search.List search;

    private final static String retrivedFields =
        "items(" + "id/kind," + "id/videoId," + "id/channelId," + "snippet/title," + " snippet/channelTitle,"
            + " snippet/publishedAt," + " snippet/thumbnails/default/url)";

    private static final String fileName = "YouTubeCache";

    private static final String searchedObjectType = "video";

    private final static String apiKey = "AIzaSyBnfhEl2amzE41PfLOkQFe4WxvkrDlgtbY";

    public SearchQuery()
    {
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer()
        {
            public void initialize(HttpRequest request)
                throws IOException
            {
                //nothing happens when request is initalized
            }
        }).setApplicationName(fileName).build();

        try {
            search = youtube.search().list("id,snippet");
        } catch (IOException e) {
            e.printStackTrace();
        }
        search.setKey(apiKey);
        search.setType(searchedObjectType);
        search.setFields(retrivedFields);
    }

    //" Video Id" getLink using : "https://www.youtube.com/watch?v="+ rId.getVideoId()
    // " Title: " + singleVideo.getSnippet().getTitle()
    // "Channel title " + singleVideo.getSnippet().getChannelTitle()
    // "Published Date" + singleVideo.getSnippet().getPublishedAt());

    public List<SearchResult> searchForVideos(String queryTerm, long numberOfVideos)
        throws IOException
    {
        search.setMaxResults(numberOfVideos);
        search.setQ(queryTerm);
        SearchListResponse searchResponse = search.execute();
        return searchResponse.getItems();
    }

}

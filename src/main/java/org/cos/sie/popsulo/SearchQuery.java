package org.cos.sie.popsulo;
    import com.google.api.client.googleapis.json.GoogleJsonResponseException;
    import com.google.api.client.http.HttpRequest;
    import com.google.api.client.http.HttpRequestInitializer;
    import com.google.api.client.http.HttpTransport;
    import com.google.api.client.http.javanet.NetHttpTransport;
    import com.google.api.client.json.JsonFactory;
    import com.google.api.client.json.jackson2.JacksonFactory;
    import com.google.api.services.youtube.YouTube;
    import com.google.api.services.youtube.model.ResourceId;
    import com.google.api.services.youtube.model.SearchListResponse;
    import com.google.api.services.youtube.model.SearchResult;
    import com.google.api.services.youtube.model.Thumbnail;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.net.URL;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Properties;

public class SearchQuery
{
    private YouTube youtube;
    private YouTube.Search.List search;

    private final static String retrivedFields = "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)";
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        search.setKey(apiKey);
        search.setType(searchedObjectType);
        search.setFields(retrivedFields);
    }

    public void searchForVideos(String queryTerm, long numberOfVideos)
    {
        try {
            // Call the API and print results.
            search.setMaxResults(numberOfVideos);
            search.setQ(queryTerm);
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        SearchQuery search = new SearchQuery();
        search.searchForVideos("ASD", 5);
        search.searchForVideos("KUPA", 10);
    }

    /*
     * Do debugowania, pozniej WYPIERDOL
     */

    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query)
    {

        System.out.println("Videos for search on \"" + query + "\".");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                System.out.println(" Video Id" + rId.getVideoId() + " Title: " + singleVideo.getSnippet().getTitle());
            }
        }
    }
}

package org.cos.sie.popsulo;

import com.github.axet.vget.VGet;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.converter.FormatConverter;
import org.cos.sie.popsulo.converter.OutputFormat;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Date;

/**
 * Created by Karol on 2016-12-17.
 */
public class LocalDiskCache
{
    private static LocalDiskCache instance = null;

    private static final String ldcPATH = "./LDC";

    private static final String pathSeperator = "/";

    private static final String urlConstPart = "https://www.youtube.com/watch?v=";

    public LocalDiskCache()
    {
        File ldcDir = new File(ldcPATH);
        if (!ldcDir.exists())
            ldcDir.mkdir();
    }

    public static LocalDiskCache getInstance()
    {
        if (instance == null) {
            instance = new LocalDiskCache();
        }
        return instance;
    }

    public void cacheQueryResult(QueryResult queryResult)
    {
        final String videoID = queryResult.getVideoId();
        if (isQueryResultInCache(videoID))
            return;
        saveVideo(queryResult, videoID);
        convertCacheToMp3(queryResult, videoID);
        JsonMaker.createJsonFile(queryResult);
    }

    private void convertCacheToMp3(QueryResult queryResult, String pathToResultCache)
    {
        FormatConverter formatConverter = null;
        try {
            formatConverter = new FormatConverter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert formatConverter != null;
        formatConverter.convertCachedResult(ldcPATH + pathSeperator + pathToResultCache);

    }

    private static void saveVideo(QueryResult queryResult, String videoID)
    {
        try {
            VGet v = new VGet(new URL(urlConstPart + videoID), new File(ldcPATH));
            v.download();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        changeNameToHash(queryResult.getTitle(), videoID);
        //Video was not cached before, we need to process it
    }

    public static boolean isQueryResultInCache(String videoID)
    {
        final String path = ldcPATH + pathSeperator + videoID;
        File videoFile = new File(path);
        return videoFile.exists();
    }

    private static void changeNameToHash(String title, String videoID)
    {
        File fileRenamed = new File(ldcPATH + pathSeperator + title + ".mp4");
        File fileToRename = new File(ldcPATH + pathSeperator + videoID + ".mp4");
        fileRenamed.renameTo(fileToRename);
    }

    static class JsonMaker
    {
        private final static String videoId = "videoId";

        private final static String title = "title";

        private final static String author = "author";

        private final static String publishingDate = "publishingDate";

        public static void createJsonFile(QueryResult queryResult)
        {
            try (FileWriter writer = new FileWriter(ldcPATH + pathSeperator + queryResult.getVideoId())) {
                Gson gson = new GsonBuilder().create();
                gson.toJson(queryResult, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static QueryResult getQueryResultFromJson(String videoID)
        {
            QueryResult queryResult = null;
            try (Reader reader = new FileReader(ldcPATH + pathSeperator + videoID)) {
                Gson gson = new GsonBuilder().create();
                queryResult = gson.fromJson(reader, QueryResult.class);
                return queryResult;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return queryResult;
        }
    }
}

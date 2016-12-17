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
        final String pathToResultCache = ldcPATH + pathSeperator + videoID;

        boolean isFileCached = saveVideo(queryResult, pathToResultCache, videoID);

        if (isFileCached)
            return;

        convertCacheToMp3(queryResult, pathToResultCache);
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
        formatConverter.convertCachedResult(pathToResultCache);

    }

    private static boolean saveVideo(QueryResult queryResult, String path, String videoID)
    {
        final String url = urlConstPart + videoID;
        File videoFile = new File(path);
        if (videoFile.exists()) {
            //Video was already cached
            return true;
        }
        try {
            VGet v = new VGet(new URL(url), new File(ldcPATH));
            v.download();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        changeNameToHash(queryResult.getTitle(), videoID);
        //Video was not cached before, we need to process it
        return false;
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

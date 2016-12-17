package org.cos.sie.popsulo;

import com.github.axet.vget.VGet;
import com.google.api.services.youtube.model.SearchResult;
import org.cos.sie.popsulo.converter.FormatConverter;
import org.cos.sie.popsulo.converter.OutputFormat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Created by Karol on 2016-12-17.
 */
public class LocalDiskCache
{
    private static LocalDiskCache instance = null;

    private static final String ldcPATH = ".";

    private static final String pathSeperator = "/";

    private static final String urlConstPart = "https://www.youtube.com/watch?v=";

    public static LocalDiskCache getInstance()
    {
        if (instance == null) {
            instance = new LocalDiskCache();
        }
        return instance;
    }

    public static void cacheQueryResult(SearchResult queryResult)
    {
        final String videoID = queryResult.getId().getVideoId();
        final String pathToResultCacheDir = ldcPATH + pathSeperator + videoID;

        boolean isFileCached = saveVideo(queryResult, pathToResultCacheDir, videoID);

        if (isFileCached)
            return;

        convertCacheToMp3(queryResult, pathToResultCacheDir);
    }

    private static void convertCacheToMp3(SearchResult queryResult, String pathToResultCacheDir)
    {
        FormatConverter formatConverter = null;
        try {
            formatConverter = new FormatConverter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert formatConverter != null;
        final String pathToCachedVid = pathToResultCacheDir + pathSeperator +
            queryResult.getSnippet().getTitle();
        formatConverter.convertCachedResult(pathToCachedVid);
    }

    private static boolean saveVideo(SearchResult queryResult, String path, String videoID)
    {
        final String url = urlConstPart + videoID;
        File directoryPath = new File(path);
        if (directoryPath.exists()) {
            //Video was already cached
            return true;
        }
        directoryPath.mkdir();
        try {
            VGet v = new VGet(new URL(url), new File(path));
            v.download();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Video was not cached before, we need to process it
        return false;
    }
}

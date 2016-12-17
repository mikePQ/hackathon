package org.cos.sie.popsulo;

import com.github.axet.vget.VGet;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
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

    public void cacheQueryResult(SearchResult queryResult)
    {
        final String videoID = queryResult.getId().getVideoId();
        final String pathToResultCacheDir = ldcPATH + pathSeperator + videoID;

        boolean isFileCached = saveVideo(queryResult, pathToResultCacheDir, videoID);

        if (true)
            return;

        convertCacheToMp3(queryResult, pathToResultCacheDir);
    }

    private void convertCacheToMp3(SearchResult queryResult, String pathToResultCacheDir)
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
        appendHashToTitle(queryResult.getSnippet().getTitle(), videoID);
        //Video was not cached before, we need to process it
        return false;
    }

    private static void appendHashToTitle(String title, String videoID)
    {
        File fileRenamed = new File(ldcPATH + pathSeperator + title);
        File fileToRename = new File(ldcPATH + pathSeperator + title + videoID);
        assert (fileToRename.exists());
        fileRenamed.renameTo(fileToRename);
    }
}

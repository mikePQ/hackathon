package org.cos.sie.popsulo;

import com.github.axet.vget.VGet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ProgressBar;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.converter.FormatConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Karol on 2016-12-17.
 */
public class LocalDiskCache {
	private static final Logger logger = LoggerFactory.getLogger(LocalDiskCache.class);

	private static LocalDiskCache instance = null;

	public static final String ldcPATH = "./LDC";

	private static final String ldcTempFolder = "./LDC/temp";

	private static final String pathSeperator = "/";

	private static final String urlConstPart = "https://www.youtube.com/watch?v=";

	private Map<String, QueryResult> vidIDs = new HashMap<String, QueryResult>();

	public LocalDiskCache() {
		File ldcDir = new File(ldcPATH);
		if ( !ldcDir.exists()) {
			ldcDir.mkdir();
			File ldcTempDir = new File(ldcTempFolder);
			ldcTempDir.mkdir();
		}
		storeFileNamesInList();
		System.out.println("No of videos in cache: " + vidIDs.size());
	}

	private void storeFileNamesInList() {
		File folder = new File(ldcPATH);
		File[] listOfFiles = folder.listFiles();

		for ( int i = 0; i < listOfFiles.length; i++ ) {
			if ( listOfFiles[i].isFile() ) {
				String fileName = listOfFiles[i].getName();
				logger.info("Found candidate " + fileName + " for cache");
				int indexOfExtension = fileName.indexOf('.');
				if ( indexOfExtension != -1 ) {
					String extension = fileName.substring(indexOfExtension + 1);
					logger.info("Extension of file was: " + extension);
					if ( "mp3".equals(extension) ) {
						String videoId = fileName.substring(0, indexOfExtension);
						logger.info("Added file with id: " + videoId + " to cache");
						vidIDs.put(videoId, JsonMaker.getQueryResultFromJson(videoId));
					}
				}
			}
		}
	}

	public static LocalDiskCache getInstance() {
		if ( instance == null ) {
			instance = new LocalDiskCache();
		}
		return instance;
	}

	public void cacheQueryResult(QueryResult queryResult) {
		final String videoID = queryResult.getVideoId();
		if ( isQueryResultInCache(videoID) ) {
			logger.info("Video with id: " + videoID + " found in cache");
			vidIDs.get(videoID).incNoOfViews();
			return;
		}
		saveVideoMiniature(queryResult);
		saveVideo(queryResult, videoID);
		vidIDs.put(videoID, JsonMaker.getQueryResultFromJson(videoID));
		convertCacheToMp3(queryResult, videoID);
		JsonMaker.createJsonFile(queryResult);
	}

	private void saveVideoMiniature(QueryResult queryResult) {
		String format = "jpg";
		String filename = ldcPATH + pathSeperator + queryResult.getVideoId() + ".jpg";
		File file = new File(filename);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(queryResult.getMiniature(), null), format, file);
		} catch ( IOException exc ) {
			logger.error("Failed to save file due to: " + exc.getMessage(), exc);
		}
	}

	private void convertCacheToMp3(QueryResult queryResult, String pathToResultCache) {
		FormatConverter formatConverter = null;
		try {
			formatConverter = new FormatConverter();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		assert formatConverter != null;
		formatConverter.convertCachedResult(ldcPATH + pathSeperator + pathToResultCache);
	}

	private static void saveVideo(QueryResult queryResult, String videoID) {
		logger.info("Download of \"" + queryResult.getTitle() + "\" requested");
		try {
			VGet v = new VGet(new URL(urlConstPart + videoID), new File(ldcTempFolder));
			v.download();
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
		changeNameToHash(queryResult.getTitle(), videoID);
		logger.info("Download of \"" + queryResult.getTitle() + "\" done");
		//Video was not cached before, we need to process it
	}

	public boolean isQueryResultInCache(String videoID) {
		return vidIDs.containsKey(videoID);
	}

	private static void changeNameToHash(String title, String videoID) {
		File ldcTempFolderFile = new File(ldcTempFolder);
		File fileResult = null;
		for (final File fileEntry : ldcTempFolderFile.listFiles()) {
			if (fileEntry.getAbsolutePath().endsWith(".webm")){
				fileResult = fileEntry;
				break;
			}
			fileResult = fileEntry;
		}
		File fileUsedToRenaming = new File(ldcPATH + pathSeperator + videoID + ".mp4");
		fileResult.renameTo(fileUsedToRenaming);
	}

	static class JsonMaker {
		private final static String videoId = "videoId";

		private final static String title = "title";

		private final static String author = "author";

		private final static String publishingDate = "publishingDate";

		public static void createJsonFile(QueryResult queryResult) {
			try ( FileWriter writer = new FileWriter(ldcPATH + pathSeperator + queryResult.getVideoId()) ) {
				Gson gson = new GsonBuilder().create();
				gson.toJson(queryResult, writer);
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}

		public static QueryResult getQueryResultFromJson(String videoID) {
			QueryResult queryResult = null;
			try ( Reader reader = new FileReader(ldcPATH + pathSeperator + videoID) ) {
				Gson gson = new GsonBuilder().create();
				queryResult = gson.fromJson(reader, QueryResult.class);
				return queryResult;
			} catch ( IOException e ) {
				e.printStackTrace();
			}
			return queryResult;
		}
	}
}

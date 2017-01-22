//package org.cos.sie.popsulo.youtubeSearch;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.cos.sie.popsulo.app.QueryResult;
//
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Reader;
//
//public class JsonMaker {
//
//	public static void createJsonFile(QueryResult queryResult, String filePath) {
//		try ( FileWriter writer = new FileWriter(filePath) ) {
//			Gson gson = new GsonBuilder().create();
//			gson.toJson(queryResult, writer);
//		} catch ( IOException e ) {
//			e.printStackTrace();
//		}
//	}
//
//	public static QueryResult getQueryResultFromJson(String videoIdPath) {
//		QueryResult queryResult = null;
//		try ( Reader reader = new FileReader(videoIdPath) ) {
//			Gson gson = new GsonBuilder().create();
//			queryResult = gson.fromJson(reader, QueryResult.class);
//			return queryResult;
//		} catch ( IOException e ) {
//			e.printStackTrace();
//		}
//		return queryResult;
//	}
//}

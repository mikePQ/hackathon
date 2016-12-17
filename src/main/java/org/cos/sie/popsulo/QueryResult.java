package org.cos.sie.popsulo;

public class QueryResult {

	public QueryResult(String title, String author) {
		this.title = title;
		this.author = author;
	}

	private String title;
	private String author;

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}
}

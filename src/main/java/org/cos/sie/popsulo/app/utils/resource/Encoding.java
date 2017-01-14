package org.cos.sie.popsulo.app.utils.resource;

public enum Encoding {
	UTF8("UTF-8");

	String encodingName;

	Encoding(String encodingName) {
		this.encodingName = encodingName;
	}

	public String getEncodingName() {
		return encodingName;
	}
}

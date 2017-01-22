package org.cos.sie.popsulo.download;

import java.net.URI;
import java.net.URL;

public class YouTubeInfo extends VideoInfo {
	private StreamInfo vq;
	private URI playerURI;

	public YouTubeInfo(URL web) {
		super(web);
	}

	public StreamInfo getVideoQuality() {
		return vq;
	}

	public void setStreamInfo(StreamInfo vq) {
		this.vq = vq;
	}

	public URI getPlayerURI() {
		return playerURI;
	}

	public void setPlayerURI(URI playerURI) {
		this.playerURI = playerURI;
	}
}
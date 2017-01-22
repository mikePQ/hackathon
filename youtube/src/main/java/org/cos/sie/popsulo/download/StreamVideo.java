package org.cos.sie.popsulo.download;

import org.cos.sie.popsulo.download.enums.Container;
import org.cos.sie.popsulo.download.enums.Encoding;
import org.cos.sie.popsulo.download.enums.YoutubeQuality;

public class StreamVideo extends StreamInfo {
	public Encoding video;
	public YoutubeQuality vq;

	public StreamVideo() {
	}

	public StreamVideo(Container c, Encoding v, YoutubeQuality vq) {
		super(c);

		this.vq = vq;
		this.video = v;
	}

	public String toString() {
		return c.toString() + " " + video.toString() + "(" + vq.toString() + ")";
	}
}
package org.cos.sie.popsulo.download;

import org.cos.sie.popsulo.download.enums.AudioQuality;
import org.cos.sie.popsulo.download.enums.Container;
import org.cos.sie.popsulo.download.enums.Encoding;

public class StreamAudio extends StreamInfo {
	public Encoding encoding;
	public AudioQuality audioQuality;

	public StreamAudio() {
	}

	public StreamAudio(Container c, Encoding encoding, AudioQuality q) {
		super(c);
		this.encoding = encoding;
		audioQuality = q;
	}

	public String toString() {
		return c.toString() + " " + encoding.toString() + " " + audioQuality.toString();
	}
}
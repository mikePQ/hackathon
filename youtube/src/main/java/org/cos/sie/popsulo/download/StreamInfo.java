package org.cos.sie.popsulo.download;

import org.cos.sie.popsulo.download.enums.Container;

public class StreamInfo {
	public Container c;

	public StreamInfo() {
	}

	public StreamInfo(Container c) {
		this.c = c;
	}

	public String toString() {
		return c.toString();
	}
}
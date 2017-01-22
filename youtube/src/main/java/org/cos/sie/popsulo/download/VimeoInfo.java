package org.cos.sie.popsulo.download;

import java.net.URL;

public class VimeoInfo extends VideoInfo {
    // keep it in order hi->lo
    public enum VimeoQuality {
        pHi, pLow
    }

    private VimeoQuality vq;

    public VimeoInfo(URL web) {
        super(web);
    }

    public VimeoQuality getVideoQuality() {
        return vq;
    }

    public void setVideoQuality(VimeoQuality vq) {
        this.vq = vq;
    }
}
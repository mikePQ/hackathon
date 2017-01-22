package org.cos.sie.popsulo.download.exceptions;

import com.github.axet.wget.info.ex.DownloadError;

import java.io.IOException;

public class DownloadEmptyTitle extends DownloadError {
    private static final long serialVersionUID = 7835308901669107488L;

    public DownloadEmptyTitle() {
    }

    public DownloadEmptyTitle(IOException e) {
        super(e);
    }

    public DownloadEmptyTitle(String str) {
        super(str);
    }
}

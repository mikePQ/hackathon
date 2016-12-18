package org.cos.sie.popsulo;

import com.github.axet.vget.VGet;

import java.io.File;
import java.net.URL;

public class DummyDownload {

    public static void main(String[] args) {
        try {
            String url = "https://www.youtube.com/watch?v=lvyLSlcpkr4";
            String path = "D:\\youtube";
            VGet v = new VGet(new URL(url), new File(path));
            v.download();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

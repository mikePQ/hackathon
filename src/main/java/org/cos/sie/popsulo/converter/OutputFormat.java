package org.cos.sie.popsulo.converter;

public enum OutputFormat
{
    MP3("mp3"), MP4("mp4");

    private final String id;
    OutputFormat(String id)
    {
        this.id = id;
    }

    @Override public String toString()
    {
        return id;
    }
}

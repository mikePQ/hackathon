package org.cos.sie.popsulo.converter;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FormatConverter
{
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    public FormatConverter()
        throws IOException
    {
        this.ffmpeg = new FFmpeg("ffmpeg");
        this.ffprobe = new FFprobe("ffprobe");
    }

    public boolean convert(File inputFile, File outputFile, OutputFormat outputFormat)
    {
        try {
            FFmpegProbeResult inputProbeResult = ffprobe.probe(inputFile.getAbsolutePath());
            String sOutputPath = outputFile.getAbsolutePath();
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputProbeResult)
                .overrideOutputFiles(true)
                .addOutput(sOutputPath)
                .setFormat(outputFormat.toString())
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

            executor.createJob(builder).run();
            return true;
        } catch (IOException e) {
            logger.warn("Cannot convert file", e);
        }
        return false;
    }

    public static void main(String[] args)
        throws IOException
    {
        String sInputPath = "D:\\youtube\\Jesteśmy zgubieni.mp4";
        String sOutputPath = "D:\\youtube\\mp3\\2.mp3";
        FormatConverter converter = new FormatConverter();
        converter.convert(new File(sInputPath), new File(sOutputPath), OutputFormat.MP3);
    }

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
    private static final Logger logger = LoggerFactory.getLogger(FormatConverter.class);
}

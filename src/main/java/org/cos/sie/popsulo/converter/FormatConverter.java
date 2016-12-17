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
import java.nio.file.Files;
import java.nio.file.Paths;

public class FormatConverter
{
    private static final Logger logger = LoggerFactory.getLogger(FormatConverter.class);

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    public FormatConverter()
        throws IOException
    {
        this.ffmpeg = new FFmpeg("ffmpeg");
        this.ffprobe = new FFprobe("ffprobe");
    }

    public void convertCachedResult(String pathToCachedVid)
    {
        final String cachedMP4 = pathToCachedVid + ".mp4";
        convert(cachedMP4, pathToCachedVid + ".mp3", OutputFormat.MP3);
        try {
            Files.delete(Paths.get(cachedMP4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean convert(String inputFile, String outputFile, OutputFormat outputFormat)
    {
        try {
            FFmpegProbeResult inputProbeResult = ffprobe.probe(inputFile);
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputProbeResult)
                .overrideOutputFiles(true)
                .addOutput(outputFile)
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

    // getters for testing purposes
    FFmpeg getFfmpeg()
    {
        return ffmpeg;
    }

    FFprobe getFfprobe()
    {
        return ffprobe;
    }
}

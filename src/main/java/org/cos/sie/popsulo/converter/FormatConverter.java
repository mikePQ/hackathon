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
    private static final Logger logger = LoggerFactory.getLogger(FormatConverter.class);

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

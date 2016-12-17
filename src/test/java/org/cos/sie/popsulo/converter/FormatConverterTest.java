package org.cos.sie.popsulo.converter;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.*;

public class FormatConverterTest
{
    @Test public void testCreate() throws IOException
    {
        FormatConverter converter = new FormatConverter();
        assertNotNull(converter);
        assertNotNull(converter.getFfmpeg());
        assertNotNull(converter.getFfprobe());
    }

    @Test public void testConvert()
        throws Exception
    {
        FormatConverter converter = new FormatConverter();
        URI inputUri = FormatConverterTest.class.getResource("testInput.mp4").toURI();
        File inputFile = new File(inputUri);
        String outputFile = new File(inputFile.getParent() + "\\testOutput.mp3").getAbsolutePath();
        converter.convert(inputFile.getAbsolutePath(), outputFile, OutputFormat.MP3);
    }
}
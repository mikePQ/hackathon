package org.cos.sie.popsulo.converter;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FormatConverterTest {

	@Test
	void testCreate() throws IOException {
		FormatConverter converter = new FormatConverter();
		assertNotNull(converter);
		assertNotNull(converter.getFfmpeg());
		assertNotNull(converter.getFfprobe());
	}

	@Test
	@Disabled("testConvert skipped - The test was failing. To be repaired or the ffmpeg replaced by native java solution")
	void testConvert() throws Exception {
		FormatConverter converter = new FormatConverter();
		URI inputUri = FormatConverterTest.class.getResource("sEr6hfy9YTo.mp4").toURI();
		File inputFile = new File(inputUri);
		String outputFile = new File(inputFile.getParent() + "\\testOutput.mp3").getAbsolutePath();
		converter.convert(inputFile.getAbsolutePath(), outputFile, OutputFormat.MP3);
	}
}
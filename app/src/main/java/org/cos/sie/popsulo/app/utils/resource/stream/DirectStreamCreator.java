package org.cos.sie.popsulo.app.utils.resource.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class DirectStreamCreator implements StreamCreator {
	@Override
	public Optional<InputStream> create(ClassLoader loader, String resourceName) throws IOException {
		return Optional.ofNullable(loader.getResourceAsStream(resourceName));
	}
}

package org.cos.sie.popsulo.app.utils.resource.stream;

import org.cos.sie.popsulo.app.utils.resource.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class URLStreamCreator implements StreamCreator {

	@Override
	public Optional<InputStream> create(ClassLoader loader, String resourceName) throws IOException {
		URL url = getResourcesURL(loader, resourceName)
				.orElseThrow(() -> new ResourceNotFoundException(resourceName));

		URLConnection connection = openURLConnection(url)
				.orElseThrow(() -> new ResourceNotFoundException(resourceName));

		return getInputStreamForResource(connection);
	}

	private Optional<URL> getResourcesURL(ClassLoader loader, String resourceName) {
		return Optional.ofNullable(loader.getResource(resourceName));
	}

	private Optional<URLConnection> openURLConnection(URL url) throws IOException {
		return Optional.ofNullable(url
				.openConnection());
	}

	private Optional<InputStream> getInputStreamForResource(URLConnection connection) throws IOException {
		connection.setUseCaches(false);
		return Optional.ofNullable(connection.getInputStream());
	}
}

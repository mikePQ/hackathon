package org.cos.sie.popsulo.app.utils.resource;

import org.cos.sie.popsulo.app.utils.resource.exceptions.ResourceNotFoundException;
import org.cos.sie.popsulo.app.utils.resource.stream.DirectStreamCreator;
import org.cos.sie.popsulo.app.utils.resource.stream.StreamCreator;
import org.cos.sie.popsulo.app.utils.resource.stream.URLStreamCreator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceControl extends ResourceBundle.Control {

	private Encoding resourceEncoding;

	public ResourceControl(@Nonnull Encoding encoding) {
		this.resourceEncoding = encoding;
	}

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
		String resourceName = createResourceBundleResourceName(baseName, locale);

		StreamCreator creator = getStreamCreator(reload);
		InputStream stream = creator.create(loader, resourceName)
				.orElseThrow(() -> new ResourceNotFoundException(resourceName));

		return loadResourceBundle(stream);
	}

	private StreamCreator getStreamCreator(boolean reload) {
		if ( reload ) {
			return new URLStreamCreator();
		} else {
			return new DirectStreamCreator();
		}
	}

	private ResourceBundle loadResourceBundle(InputStream stream) throws IOException {
		try {
			return new PropertyResourceBundle(new InputStreamReader(stream, getResourceEncodingName()));
		} finally {
			stream.close();
		}
	}

	private String createResourceBundleResourceName(String baseName, Locale locale) {
		String bundleName = toBundleName(baseName, locale);
		return toResourceName(bundleName, "properties");
	}

	private String getResourceEncodingName() {
		return resourceEncoding.getEncodingName();
	}
}

package org.cos.sie.popsulo.app.utils.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public abstract class AbstractResourcesControl extends ResourceBundle.Control {
	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, "properties");

		ResourceBundle resourceBundle = null;
		InputStream stream = null;

		if ( reload ) {
			URL url = loader.getResource(resourceName);
			if ( url != null ) {
				URLConnection connection = url.openConnection();
				if ( connection != null ) {
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		} else {
			stream = loader.getResourceAsStream(resourceName);
		}

		if ( stream != null ) {
			try {
				resourceBundle = new PropertyResourceBundle(new InputStreamReader(stream, getEncoding()));
			} finally {
				stream.close();
			}
		}
		return resourceBundle;
	}

	protected abstract String getEncoding();
}

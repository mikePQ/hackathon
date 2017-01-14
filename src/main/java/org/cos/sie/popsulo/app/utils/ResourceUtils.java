package org.cos.sie.popsulo.app.utils;

import org.cos.sie.popsulo.app.utils.resource.Encoding;
import org.cos.sie.popsulo.app.utils.resource.ResourceControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ResourceUtils {

	private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);
	private static final String PROPERTIES_FILENAME = "Key.properties";

	private ResourceBundle messageResources;

	private ResourceUtils() {
		messageResources = loadLabelsForDefaultLocale();
	}

	public static ResourceBundle loadLabelsForDefaultLocale() {
		Locale defaultLocale = Locale.forLanguageTag("pl-PL");
		try {
			return ResourceBundle.getBundle("messages.Message", defaultLocale, new ResourceControl(Encoding.UTF8));
		} catch ( MissingResourceException exc ) {
			throw new IllegalStateException("Could not load resource bundle");
		}
	}

	public static Properties getKeyProperties()
	{
		Properties properties = new Properties();
		try {
			InputStream in = ResourceUtils.class.getResourceAsStream(PROPERTIES_FILENAME);
			properties.load(in);
		} catch (IOException e) {
			logger.error("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
		}
        return properties;
	}

	public String getItem(String key) {
		return messageResources.getString(key);
	}
}

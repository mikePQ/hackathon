package org.cos.sie.popsulo.app.utils;

import org.cos.sie.popsulo.app.utils.resource.UTF8ResourcesControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceUtils {

	private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

	private static ResourceUtils instace;

	private ResourceBundle messageResources;

	public static ResourceUtils getInstace() {
		if ( instace == null ) {
			instace = new ResourceUtils();
		}
		return instace;
	}

	private ResourceUtils() {
		messageResources = loadLabelsForDefaultLocale();
	}

	public static ResourceBundle loadLabelsForDefaultLocale() {
		Locale defaultLocale = Locale.forLanguageTag("pl-PL");
		try {
			return ResourceBundle.getBundle("messages.Message", defaultLocale, new UTF8ResourcesControl());
		} catch ( MissingResourceException exc ) {
			throw new IllegalStateException("Could not load resource bundle");
		}
	}

	public String getItem(String key) {
		return messageResources.getString(key);
	}
}

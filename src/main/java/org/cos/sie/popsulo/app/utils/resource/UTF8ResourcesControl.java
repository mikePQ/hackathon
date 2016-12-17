package org.cos.sie.popsulo.app.utils.resource;

/**
 * A hack used to load UTF-8 characters from Message_??.properties
 */
public class UTF8ResourcesControl extends AbstractResourcesControl {

	private static final String UTF8_NAME = "UTF-8";

	@Override
	protected String getEncoding() {
		return UTF8_NAME;
	}
}

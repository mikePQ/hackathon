package org.cos.sie.popsulo.app.utils.resource.exceptions;

public class ResourceNotFoundException extends IllegalStateException {

	private final static String RESOURCE_NOT_FOUND = "Resource bundle not found: ";

	public ResourceNotFoundException(String resourceName) {
		super(RESOURCE_NOT_FOUND + resourceName);
	}
}

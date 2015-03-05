package org.eclipse.wst.json.core.internal.validation;

import org.eclipse.wst.json.core.internal.validation.core.ValidationInfo;

public class JSONValidationInfo extends ValidationInfo implements
		JSONValidationReport {

	/**
	 * Constructor.
	 * 
	 * @param uri
	 *            The URI of the file this report describes.
	 */
	public JSONValidationInfo(String uri) {
		super(uri);
	}

	@Override
	public boolean isGrammarEncountered() {
		return false;
	}
}

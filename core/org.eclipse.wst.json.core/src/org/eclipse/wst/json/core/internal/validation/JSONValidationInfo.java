package org.eclipse.wst.json.core.internal.validation;

import org.eclipse.json.IValidationReporter;
import org.eclipse.wst.json.core.internal.validation.core.ValidationInfo;
import org.eclipse.wst.json.core.validation.AnnotationMsg;

public class JSONValidationInfo extends ValidationInfo implements
		JSONValidationReport, IValidationReporter {

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

	@Override
	public void addMessage(String message, int line, int column, int offset) {
		boolean isEndOfText = message.startsWith("Unexpected end of input");		
		AnnotationMsg annotation = new AnnotationMsg(
				ProblemIDsJSON.MissingEndBracket, null, 1);//isEndOfText ? 1 : 1);
		if (line > 1 && column == 0) {
			
		}
		int o = offset -1; //isEndOfText ? offset - 1 : offset -1;// -1;// - ( (line -2)* 2);
		// if (column == 0) o=o-2;
		super.addError(message, line - 1, o, getFileURI(), "null",
				new Object[] { annotation });
	}
}

package org.eclipse.json;

public interface IValidationReporter {

	void addMessage(String message, int line, int column, int offset);

}

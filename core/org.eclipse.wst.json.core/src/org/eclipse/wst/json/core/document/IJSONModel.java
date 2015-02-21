package org.eclipse.wst.json.core.document;

import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

public interface IJSONModel extends IStructuredModel {

	/**
	 * Returns the JSON Document.
	 * 
	 * @return the JSON Document.
	 */
	IJSONDocument getDocument();

}

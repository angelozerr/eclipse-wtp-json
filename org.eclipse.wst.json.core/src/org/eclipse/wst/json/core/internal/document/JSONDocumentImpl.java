package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONModel;
import org.eclipse.wst.json.core.document.IJSONNode;

public class JSONDocumentImpl extends JSONStructuredDocumentRegionContainer
		implements IJSONDocument {

	private JSONModelImpl fModel = null;

	JSONDocumentImpl() {
		super();
	}

	JSONDocumentImpl(JSONDocumentImpl that) {
		super(that);
	}

	public IJSONModel getModel() {
		return fModel;
	}

	void setModel(JSONModelImpl model) {
		this.fModel = model;
	}

	@Override
	public boolean isDocument() {
		return true;
	}

	@Override
	public IJSONNode cloneNode(boolean deep) {
		JSONDocumentImpl cloned = new JSONDocumentImpl(this);

		if (deep)
			cloneChildNodes(cloned, deep);

		return cloned;
	}
}

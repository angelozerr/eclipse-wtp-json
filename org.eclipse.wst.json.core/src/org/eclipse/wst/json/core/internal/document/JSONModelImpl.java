package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONModel;
import org.eclipse.wst.sse.core.internal.model.AbstractStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;

public class JSONModelImpl extends AbstractStructuredModel implements
		IJSONModel {

	private JSONDocumentImpl document = null;

	public JSONModelImpl() {
		this.document = new JSONDocumentImpl();
	}

	@Override
	public IJSONDocument getDocument() {
		return document;
	}

	@Override
	public IndexedRegion getIndexedRegion(int offset) {
		if (this.document == null)
			return null;
		return null;
	}

}

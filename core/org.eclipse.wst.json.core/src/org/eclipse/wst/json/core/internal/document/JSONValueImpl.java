package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONValue;

public abstract class JSONValueImpl extends JSONNodeImpl implements IJSONValue {

	protected JSONValueImpl() {
		super();
	}

	/**
	 * NodeImpl constructor
	 * 
	 * @param that
	 *            NodeImpl
	 */
	protected JSONValueImpl(JSONValueImpl that) {
		super(that);
	}

	@Override
	public String getSimpleValue() {
		if (getStartStructuredDocumentRegion() == null) {
			return null;
		}
		return getStartStructuredDocumentRegion().getText();
	}

	@Override
	public String getValueRegionType() {
		if (getStartStructuredDocumentRegion() == null) {
			return null;
		}
		return getStartStructuredDocumentRegion().getType();
	}
}

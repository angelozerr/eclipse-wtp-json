package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONModel;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;
import org.eclipse.wst.json.core.document.JSONException;

public class JSONDocumentImpl extends JSONNodeContainer implements IJSONDocument {

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
	public IJSONObject createJSONObject() {
		JSONObjectImpl object = new JSONObjectImpl();
		object.setOwnerDocument(this);
		return object;
	}

	@Override
	public short getNodeType() {
		return DOCUMENT_NODE;
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeValue() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJSONNode cloneNode(boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}

}

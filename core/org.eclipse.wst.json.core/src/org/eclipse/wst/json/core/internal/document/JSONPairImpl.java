package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONPair;
import org.eclipse.wst.json.core.document.IJSONValue;
import org.eclipse.wst.json.core.document.JSONException;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

public class JSONPairImpl extends JSONNodeImpl implements IJSONPair {

	private String name;
	private ITextRegion nameRegion = null;
	private JSONObjectImpl ownerObject = null;
	private ITextRegion fValueRegion = null;

	@Override
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
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

	@Override
	public short getNodeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setOwnerObject(JSONObjectImpl ownerObject) {
		this.ownerObject = ownerObject;
	}

	public JSONObjectImpl getOwnerObject() {
		return ownerObject;
	}

	@Override
	public IJSONValue getValue() {
		// TODO Auto-generated method stub
		return null;
	}
}

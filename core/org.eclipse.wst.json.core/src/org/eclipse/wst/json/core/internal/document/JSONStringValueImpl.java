package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONStringValue;
import org.eclipse.wst.json.core.document.JSONException;

public class JSONStringValueImpl extends JSONStructureImpl implements
		IJSONStringValue {

	@Override
	public short getNodeType() {
		return VALUE_STRING_NODE;
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

package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONPair;
import org.eclipse.wst.json.core.document.IJSONValue;
import org.eclipse.wst.json.core.document.JSONException;
import org.eclipse.wst.json.core.regions.JSONRegionContexts;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

public class JSONPairImpl extends JSONStructureImpl implements IJSONPair {

	private String name;
	private ITextRegion nameRegion = null;
	private ITextRegion equalRegion = null;
	private JSONObjectImpl ownerObject = null;
	private ITextRegion fValueRegion = null;
	private IJSONValue value;

	@Override
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		name = name.substring(1, name.length() - 1); // remove start/end quote
		String oldName = this.name;
		IJSONValue value = null;
		int startOffset = 0;
		if (this.ownerObject != null) {
			value = getValue();
			startOffset = this.ownerObject.getStartOffset();
			this.ownerObject.notify(REMOVE, this, value, null, startOffset);
		}
		this.name = name;
		if (this.ownerObject != null) {
			this.ownerObject.notify(ADD, this, null, value, startOffset);
		}
		notify(CHANGE, null, oldName, name, getStartOffset());

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
		return PAIR_NODE;
	}

	public void setOwnerObject(JSONObjectImpl ownerObject) {
		this.ownerObject = ownerObject;
	}

	public JSONObjectImpl getOwnerObject() {
		return ownerObject;
	}

	@Override
	public IJSONValue getValue() {
		return value;
	}

	public void setNameRegion(ITextRegion nameRegion) {
		this.nameRegion = nameRegion;
	}

	@Override
	public ITextRegion getNameRegion() {
		return nameRegion;
	}

	@Override
	public int getStartOffset() {
		if (this.ownerObject == null)
			return 0;
		int offset = this.ownerObject.getStartOffset();
		if (this.nameRegion != null) {
			return (offset + this.nameRegion.getStart());
		}
		if (this.equalRegion != null) {
			return (offset + this.equalRegion.getStart());
		}
		if (this.value != null) {
			return (offset + this.value.getStartOffset());
		}
		return 0;
	}

	@Override
	public int getEndOffset() {
		if (this.ownerObject == null)
			return 0;
		int offset = this.ownerObject.getStartOffset();
		if (this.value != null) {
			return (offset + this.value.getEndOffset());
		}
		if (this.equalRegion != null) {
			return (offset + this.equalRegion.getEnd());
		}
		if (this.nameRegion != null) {
			return (offset + this.nameRegion.getEnd());
		}
		return 0;
	}

	public void setEqualRegion(ITextRegion equalRegion) {
		this.equalRegion = equalRegion;
	}

	public void setValueRegion(ITextRegion valueRegion) {
		this.fValueRegion = valueRegion;
	}

	@Override
	public short getNodeValueType() {
		if (value != null) {
			return value.getNodeType();
		}
		return -1;
	}

	@Override
	public String getSimpleValue() {
		if (ownerObject == null) {
			return null;
		}
		IStructuredDocumentRegion ownerRegion = this.ownerObject
				.getStartStructuredDocumentRegion();
		if (this.fValueRegion != null)
			return StructuredDocumentRegionUtil.getAttrValue(ownerRegion,
					this.fValueRegion);
		return JSONNodeImpl.EMPTY_STRING;
	}

	@Override
	public String getValueRegionType() {
		if (this.fValueRegion != null) {
			return fValueRegion.getType();
		}
		return null;
	}

	public void setValue(IJSONValue value) {
		this.value = value;
		((JSONValueImpl)value).setParentNode(ownerObject);
	}
}

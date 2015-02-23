package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;
import org.eclipse.wst.json.core.document.JSONException;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;

public class JSONObjectImpl extends JSONNodeContainer
		implements IJSONObject {

	private IStructuredDocumentRegion endStructuredDocumentRegion = null;
	
	public JSONObjectImpl() {
	}

	public JSONObjectImpl(JSONObjectImpl object) {
		super(object);
	}

	@Override
	public IJSONNode cloneNode(boolean deep) {
		JSONObjectImpl cloned = new JSONObjectImpl(this);

		if (deep)
			cloneChildNodes(cloned, deep);

		return cloned;
	}

	@Override
	public boolean isDocument() {
		return false;
	}

	@Override
	public short getNodeType() {
		return IJSONNode.OBJECT_NODE;
	}
	
	public boolean hasEndTag() {
		return (getLastStructuredDocumentRegion() != null);
	}

	public boolean isContainer() {
		return true;
	}

	public boolean isEmptyTag() {
		return false;
	}

	public boolean hasStartTag() {
		return (getStructuredDocumentRegion() != null);
	}

	public int getStartEndOffset() {
		IStructuredDocumentRegion flatNode = getStructuredDocumentRegion();
		if (flatNode != null)
			return flatNode.getEnd();
		return super.getStartOffset();
	}

	@Override
	public String getNodeName() {
		return "object";
	}

	@Override
	public String getNodeValue() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
	
	void setStartStructuredDocumentRegion(IStructuredDocumentRegion flatNode) {
		setStructuredDocumentRegion(flatNode);
	}

	void setEndStructuredDocumentRegion(IStructuredDocumentRegion flatNode) {
		this.endStructuredDocumentRegion = flatNode;	
	}
	
//	@Override
//	public boolean getBoolean(String paramString) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean getBoolean(String paramString, boolean paramBoolean) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public int getInt(String paramString) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int getInt(String paramString, int paramInt) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public JsonArray getJsonArray(String paramString) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public JsonNumber getJsonNumber(String paramString) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public JsonObject getJsonObject(String paramString) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public JsonString getJsonString(String paramString) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getString(String paramString) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getString(String paramString1, String paramString2) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isNull(String paramString) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public ValueType getValueType() {
//		return ValueType.OBJECT;
//	}
//
//	@Override
//	public void clear() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean containsKey(Object arg0) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean containsValue(Object arg0) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Set<java.util.Map.Entry<String, JsonValue>> entrySet() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public JsonValue get(Object arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isEmpty() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Set<String> keySet() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public JsonValue put(String key, JsonValue value) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void putAll(Map<? extends String, ? extends JsonValue> arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public JsonValue remove(Object arg0) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int size() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public Collection<JsonValue> values() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}

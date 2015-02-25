package org.eclipse.wst.json.core.internal.document;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;
import org.eclipse.wst.json.core.document.IJSONPair;
import org.eclipse.wst.json.core.document.JSONException;

public class JSONObjectImpl extends JSONStructureImpl implements IJSONObject {

	private final List<IJSONPair> pairs;

	public JSONObjectImpl() {
		this(null);
	}

	public JSONObjectImpl(JSONObjectImpl object) {
		super(object);
		pairs = new ArrayList<IJSONPair>();
	}

	@Override
	public IJSONNode cloneNode(boolean deep) {
		JSONObjectImpl cloned = new JSONObjectImpl(this);

		if (deep)
			cloneChildNodes(cloned, deep);

		return cloned;
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

	@Override
	public String getNodeName() {
		return "object";
	}

	@Override
	public String getNodeValue() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	public IJSONObject add(IJSONPair newAttr) {
		if (newAttr == null)
			return null;
		JSONPairImpl attr = (JSONPairImpl) newAttr;
		if (attr.getOwnerObject() != null)
			return null;

		// if (this.attrNodes == null)
		// this.attrNodes = new NodeListImpl();
		// this.attrNodes.appendNode(attr);
		attr.setOwnerObject(this);
		pairs.add(newAttr);
		notifyPairReplaced(attr, null);
		return this;
	}

	protected void notifyPairReplaced(IJSONPair newPair, IJSONPair oldPair) {
		JSONDocumentImpl document = (JSONDocumentImpl) getContainerDocument();
		if (document == null)
			return;
		JSONModelImpl model = (JSONModelImpl) document.getModel();
		if (model == null)
			return;
		model.pairReplaced(this, newPair, oldPair);
	}

	@Override
	public IJSONObject remove(IJSONPair pair) {
		pairs.remove(pair);
		notifyPairReplaced(null, pair);
		return this;
	}

	public List<IJSONPair> getPairs() {
		return pairs;
	}

	// @Override
	// public boolean getBoolean(String paramString) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public boolean getBoolean(String paramString, boolean paramBoolean) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public int getInt(String paramString) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public int getInt(String paramString, int paramInt) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public JsonArray getJsonArray(String paramString) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public JsonNumber getJsonNumber(String paramString) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public JsonObject getJsonObject(String paramString) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public JsonString getJsonString(String paramString) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public String getString(String paramString) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public String getString(String paramString1, String paramString2) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public boolean isNull(String paramString) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public ValueType getValueType() {
	// return ValueType.OBJECT;
	// }
	//
	// @Override
	// public void clear() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public boolean containsKey(Object arg0) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public boolean containsValue(Object arg0) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public Set<java.util.Map.Entry<String, JsonValue>> entrySet() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public JsonValue get(Object arg0) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public boolean isEmpty() {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// public Set<String> keySet() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public JsonValue put(String key, JsonValue value) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public void putAll(Map<? extends String, ? extends JsonValue> arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public JsonValue remove(Object arg0) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public int size() {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public Collection<JsonValue> values() {
	// // TODO Auto-generated method stub
	// return null;
	// }

}

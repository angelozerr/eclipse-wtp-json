package org.eclipse.wst.json.core.document;

public interface IJSONPair extends IJSONStructure {

	String getName();
	
	IJSONValue getValue();

	short getNodeValueType();
	
	String getSimpleValue();

	String getValueRegionType();

	IJSONObject getOwnerObject();
}

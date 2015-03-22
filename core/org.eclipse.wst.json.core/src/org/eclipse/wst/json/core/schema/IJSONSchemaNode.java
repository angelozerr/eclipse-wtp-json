package org.eclipse.wst.json.core.schema;

public interface IJSONSchemaNode {

	IJSONSchemaNode getParent();

	IJSONSchemaProperty[] getProperties();
}

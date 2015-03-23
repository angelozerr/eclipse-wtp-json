package org.eclipse.json.schema;

public interface IJSONSchemaNode {

	IJSONSchemaNode getParent();

	IJSONSchemaProperty[] getProperties();
}

package org.eclipse.wst.json.core.schema;

public interface IJSONSchemaProperty extends IJSONSchemaNode {

	IJSONSchemaProperty[] EMPTY_PROPERTY = new IJSONSchemaProperty[0];

	String getName();

	String getDescription();

	String getType();

}

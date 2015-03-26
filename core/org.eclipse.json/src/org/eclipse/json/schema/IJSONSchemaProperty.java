package org.eclipse.json.schema;

public interface IJSONSchemaProperty extends IJSONSchemaNode {

	IJSONSchemaProperty[] EMPTY_PROPERTY = new IJSONSchemaProperty[0];

	String getName();

	String getDescription();

	JSONSchemaType[] getType();

	JSONSchemaType getFirstType();

}

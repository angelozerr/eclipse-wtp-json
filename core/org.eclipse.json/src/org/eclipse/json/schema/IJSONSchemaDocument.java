package org.eclipse.json.schema;


public interface IJSONSchemaDocument extends IJSONSchemaNode, IJSONSchemaProperty {

	IJSONSchemaProperty getProperty(IJSONPath path);

}

package org.eclipse.wst.json.core.schema;

import org.eclipse.wst.json.core.document.IJSONNode;

public interface IJSONSchemaDocument extends IJSONSchemaNode {

	IJSONSchemaProperty[] getProperties(IJSONNode node);

}

package org.eclipse.wst.json.core.schema;

import java.io.IOException;

public interface IJSONSchemaProcessor {

	IJSONSchemaDocument getSchema(String uri) throws IOException;

	// IJSONProperty findProperty(IJSONPath path, IJSONSchema schema);
}

package org.eclipse.json.schema;

import java.io.IOException;

public interface IJSONSchemaProcessor {

	IJSONSchemaDocument getSchema(String uri) throws IOException;

}

package org.eclipse.wst.json.schemaprocessor.internal;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.json.impl.schema.JSONSchemaDocument;
import org.eclipse.json.schema.IJSONSchemaDocument;
import org.eclipse.json.schema.IJSONSchemaProcessor;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;

public class JSONSchemaProcessor implements IJSONSchemaProcessor {

	@Override
	public IJSONSchemaDocument getSchema(String uriString) throws IOException {
		String physicalLocation = URIResolverPlugin.createResolver()
				.resolvePhysicalLocation("", "", uriString);
		URL url = new URL(physicalLocation);
		return new JSONSchemaDocument(new InputStreamReader(url.openStream()));
	}
	// @Override
	// public IJSONProperty findProperty(IJSONPath path, IJSONSchema schema) {
	// // TODO Auto-generated method stub
	// return null;
	// }
}

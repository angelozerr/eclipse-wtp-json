package org.eclipse.wst.json.schemaprocessor.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.json.core.schema.IJSONSchemaDocument;
import org.eclipse.wst.json.core.schema.IJSONSchemaProcessor;

import com.eclipsesource.json.JsonObject;

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

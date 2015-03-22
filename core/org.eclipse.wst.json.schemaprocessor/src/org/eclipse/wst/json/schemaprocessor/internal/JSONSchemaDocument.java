package org.eclipse.wst.json.schemaprocessor.internal;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.schema.IJSONSchemaDocument;
import org.eclipse.wst.json.core.schema.IJSONSchemaProperty;

import com.eclipsesource.json.JsonObject;

public class JSONSchemaDocument extends JSONSchemaNode implements
		IJSONSchemaDocument {

	public JSONSchemaDocument(Reader reader) throws IOException {
		super(JsonObject.readFrom(reader), null);
	}

	@Override
	public IJSONSchemaProperty[] getProperties(IJSONNode node) {
		return getProperties();
	}

}

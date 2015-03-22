package org.eclipse.wst.json.schemaprocessor.internal;

import org.eclipse.wst.json.core.schema.IJSONSchemaNode;
import org.eclipse.wst.json.core.schema.IJSONSchemaProperty;

import com.eclipsesource.json.JsonObject;

public class JSONSchemaProperty extends JSONSchemaNode implements
		IJSONSchemaProperty {

	private final String name;

	public JSONSchemaProperty(String name, JsonObject jsonObject,
			IJSONSchemaNode parent) {
		super(jsonObject, parent);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

}

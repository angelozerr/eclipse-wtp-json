package org.eclipse.json.impl.schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.json.schema.IJSONSchemaNode;
import org.eclipse.json.schema.IJSONSchemaProperty;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;

public class JSONSchemaNode implements IJSONSchemaNode {

	private final IJSONSchemaNode parent;
	private final Map<String, IJSONSchemaProperty> properties;

	public JSONSchemaNode(JsonObject jsonObject, IJSONSchemaNode parent) {
		this.parent = parent;
		this.properties = new HashMap<String, IJSONSchemaProperty>();
		walk(jsonObject, this);
	}

	private static void walk(JsonObject json, JSONSchemaNode schemaNode) {
		Member member = null;
		JsonObject properties = (JsonObject) json.get("properties");
		if (properties != null) {
			Iterator<Member> members = properties.iterator();
			while (members.hasNext()) {
				member = members.next();
				schemaNode.addProperty(new JSONSchemaProperty(member.getName(),
						(JsonObject) member.getValue(), schemaNode));
			}
		}
	}

	private void addProperty(IJSONSchemaProperty property) {
		properties.put(property.getName(), property);
	}

	@Override
	public IJSONSchemaNode getParent() {
		return parent;
	}

	@Override
	public IJSONSchemaProperty[] getProperties() {
		return properties.values().toArray(IJSONSchemaProperty.EMPTY_PROPERTY);
	}

}

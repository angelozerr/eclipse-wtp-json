package org.eclipse.json.impl.schema;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.json.schema.IJSONSchemaNode;
import org.eclipse.json.schema.IJSONSchemaProperty;
import org.eclipse.json.schema.JSONSchemaType;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class JSONSchemaProperty extends JSONSchemaNode implements
		IJSONSchemaProperty {

	private final String name;
	private final String description;
	private JSONSchemaType[] type;

	public JSONSchemaProperty(String name, JsonObject jsonObject,
			IJSONSchemaNode parent) {
		super(jsonObject, parent);
		this.name = name;
		this.description = jsonObject.getString("description", null);
		this.type = getType(jsonObject.get("type"));
	}

	private JSONSchemaType[] getType(JsonValue value) {
		if (value == null) {
			return JSONSchemaType.EMPTY_TYPES;
		}
		JSONSchemaType t = null;
		List<JSONSchemaType> types = new ArrayList<JSONSchemaType>();
		if (value.isString()) {
			t = JSONSchemaType.getType(value.asString());
			if (t != null) {
				types.add(t);
			}
		} else if (value.isArray()) {
			JsonArray array = (JsonArray) value;
			for (JsonValue item : array) {
				t = JSONSchemaType.getType(item.asString());
				if (t != null) {
					types.add(t);
				}
			}
		}
		return types.toArray(JSONSchemaType.EMPTY_TYPES);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public JSONSchemaType[] getType() {
		return type;
	}

}

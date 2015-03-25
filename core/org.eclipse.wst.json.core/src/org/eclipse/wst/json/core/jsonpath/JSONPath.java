package org.eclipse.wst.json.core.jsonpath;

public class JSONPath {

	private final String[] segments;

	public JSONPath(String expression) {
		this.segments = expression.split("[.]");
	}

	public String[] getSegments() {
		return segments;
	}
}

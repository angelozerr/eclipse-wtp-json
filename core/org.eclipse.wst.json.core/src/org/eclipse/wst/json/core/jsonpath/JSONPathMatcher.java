package org.eclipse.wst.json.core.jsonpath;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONPair;

public class JSONPathMatcher {

	public static boolean isMatch(IJSONNode node, JSONPath path) {
		String name = null;
		String segment = null;
		String[] segments = path.getSegments();
		for (int i = segments.length - 1; i >= 0; i--) {
			segment = segments[i];
			name = getName(node);
			if (name == null) {
				return false;
			}
			if (!(segment.equals(name))) {
				return false;
			}
			node = node.getParentNode();
		}
		return true;
	}

	private static String getName(IJSONNode node) {
		if (node == null) {
			return null;
		}
		if (node.getNodeType() == IJSONNode.PAIR_NODE) {
			return ((IJSONPair) node).getName();
		}
		IJSONNode parent = node.getParentOrPairNode();
		if (parent == null) {
			return null;
		}
		switch (parent.getNodeType()) {
		case IJSONNode.DOCUMENT_NODE:
			return "$";
		case IJSONNode.PAIR_NODE:
			return ((IJSONPair) parent).getName();
		default:
			return null;
		}
	}
}

package org.eclipse.wst.json.core.document;

public interface IJSONNode {

	IJSONNode cloneNode(boolean deep);

	IJSONDocument getOwnerDocument();

	IJSONNode getFirstChild();

	IJSONNode getLastChild();

	IJSONNode getPreviousSibling();

	IJSONNode getNextSibling();

	IJSONNode getParentNode();

	boolean isDocument();
}

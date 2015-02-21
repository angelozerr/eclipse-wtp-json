/**
 *  Copyright (c) 2015-present Angelo ZERR.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.core.document;

/**
 * The JSON node API.
 *
 */
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

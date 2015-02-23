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

import org.eclipse.wst.json.core.internal.document.JSONObjectImpl;

/**
 * JSON Document API.
 *
 */
public interface IJSONDocument extends IJSONNode {

	/**
	 * Returns the SSE JSON model.
	 * 
	 * @return the SSE JSON model.
	 */
	IJSONModel getModel();

	IJSONObject createJSONObject();
}

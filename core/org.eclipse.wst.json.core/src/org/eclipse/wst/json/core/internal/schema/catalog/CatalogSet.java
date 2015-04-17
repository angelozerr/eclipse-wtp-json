/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.core.internal.schema.catalog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.json.core.internal.Logger;

public class CatalogSet {
	protected Map uriResourceMap = new HashMap();
	protected Map catalogPersistenceLocations = new HashMap();

	public CatalogSet() {
		super();
	}

	/**
	 * Find a Catalog with the given ID. If one is not found, create one at the
	 * given URI.
	 * 
	 * @param id
	 * @param uri
	 *            - the URI, the parent of this file path must already exist
	 * @return
	 */
	public Catalog lookupOrCreateCatalog(String id, String uri) {
		Catalog catalog = getCatalog(id, uri);
		if (catalog == null) {
			catalog = new Catalog(this, id, uri);
			try {
				catalog.load();
				uriResourceMap.put(uri, catalog);
			} catch (Exception e) {
				// we catch and log all exceptions, to disallow
				// one bad extension interfering with others
				Logger.logException(
						"error loading catalog: " + id + " " + uri, e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return catalog;
	}

	private Catalog getCatalog(String id, String uri) {
		return (Catalog) uriResourceMap.get(uri);
	}

	public void putCatalogPersistenceLocation(String logicalURI,
			String actualURI) {
		catalogPersistenceLocations.put(logicalURI, actualURI);
	}

	// Never used?
	public String getCatalogPersistenceLocation(String id) {
		return (String) catalogPersistenceLocations.get(id);
	}

	public void clearResourceCache() {// Clearing only uriResourceMap is
										// required
		uriResourceMap.clear();
	}
}

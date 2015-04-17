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

import org.eclipse.wst.json.core.schema.catalog.ICatalogElement;
import org.eclipse.wst.json.core.schema.catalog.ICatalogEntry;

public class CatalogEntry extends CatalogElement implements ICatalogEntry,
		Cloneable {
	int entryType = ICatalogEntry.ENTRY_TYPE_SCHEMA;
	String key;
	String uri;

	protected CatalogEntry(int anEntryType) {
		super(ICatalogElement.TYPE_ENTRY);
		entryType = anEntryType;
	}

	protected CatalogEntry() {
		super(ICatalogElement.TYPE_ENTRY);
	}

	public void setEntryType(int value) {
		entryType = value;
	}

	public int getEntryType() {
		return entryType;
	}

	public void setKey(String value) {
		key = value;
	}

	public String getKey() {
		return key;
	}

	public String getURI() {
		return uri;
	}

	public void setURI(String value) {
		uri = value;
	}

	public Object clone() {
		CatalogEntry entry = (CatalogEntry) super.clone();
		entry.setEntryType(entryType);
		entry.setKey(key);
		entry.setURI(uri);
		return entry;
	}
}

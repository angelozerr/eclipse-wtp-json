package org.eclipse.wst.json.core.internal.schema.catalog;

import org.eclipse.wst.json.core.schema.catalog.ICatalog;
import org.eclipse.wst.json.core.schema.catalog.ICatalogElement;
import org.eclipse.wst.json.core.schema.catalog.ICatalogEvent;

public class CatalogEvent implements ICatalogEvent {
	protected ICatalog catalog;
	protected ICatalogElement catalogElement;
	protected int eventType;

	public CatalogEvent(Catalog catalog, ICatalogElement element, int eventType) {
		this.catalog = catalog;
		this.catalogElement = element;
		this.eventType = eventType;
	}

	public ICatalog getCatalog() {
		return catalog;
	}

	public ICatalogElement getCatalogElement() {
		return catalogElement;
	}

	public int getEventType() {
		return eventType;
	}
}

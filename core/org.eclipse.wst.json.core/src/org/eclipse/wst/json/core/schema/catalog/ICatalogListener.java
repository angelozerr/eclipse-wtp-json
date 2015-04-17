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
package org.eclipse.wst.json.core.schema.catalog;

import java.util.EventListener;


/**
 * The clients of the catalog that want to listen to catalog changes should
 * implement this interface.
 * 
 * @see ICatalog, ICatalogEvent,
 * 
 */
public interface ICatalogListener extends EventListener
{
    /**
     * This method allows to react to catalog events
     * 
     * @param event -
     *            an event that client should react to
     */
    public void catalogChanged(ICatalogEvent event);
}

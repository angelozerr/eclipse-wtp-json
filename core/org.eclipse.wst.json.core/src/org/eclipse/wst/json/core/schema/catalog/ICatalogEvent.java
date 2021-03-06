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



/**
 * 
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * 
 */
public interface ICatalogEvent
{
    /** */
    public static final int CHANGED = 0;

    public static final int ELEMENT_ADDED = 1;

    /** */
    public static final int ELEMENT_REMOVED = 2;

    /** */
    public static final int ELEMENT_CHANGED = 3;

    /**
     * 
     * @return
     */
    public int getEventType();

    /**
     * 
     * @return
     */
    public ICatalog getCatalog();

    /**
     * 
     * @return
     */
    public ICatalogElement getCatalogElement();
}

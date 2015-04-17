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
public interface IRewriteEntry extends ICatalogElement
{
    /** The rewriteSystem Catalog type. */
    int REWRITE_TYPE_SYSTEM = 21;

    /** The URI Catalog Entry type. */
    int REWRITE_TYPE_URI = 22;

    /**
     * 
     * @param entryType
     */
    void setEntryType(int entryType);

    /**
     * 
     * @return
     */
    int getEntryType();

    /**
     * 
     * @param key
     */
    void setStartString(String startString);

    /**
     * 
     * @return
     */
    String getStartString();

    /**
     * 
     * @return
     */
    String getRewritePrefix();

    /**
     * 
     * @param uri
     */
    void setRewritePrefix(String uri);
}

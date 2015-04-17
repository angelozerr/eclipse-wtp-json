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
public interface ISuffixEntry extends ICatalogElement
{
    /** The rewriteSystem Catalog type. */
    int SUFFIX_TYPE_SYSTEM = 41;

    /** The URI Catalog Entry type. */
    int SUFFIX_TYPE_URI = 42;

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
    void setSuffix(String suffixString);

    /**
     * 
     * @return
     */
    String getSuffix();

    /**
     * 
     * @return
     */
    String getURI();

    /**
     * 
     * @param uri
     */
    void setURI(String uri);
}

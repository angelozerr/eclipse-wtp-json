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
package org.eclipse.wst.json.core.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

/**
 * Messages for JSON Core Plugin.
 *
 */
public class JSONCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.json.core.internal.JSONCoreMessages";//$NON-NLS-1$

	private static ResourceBundle fResourceBundle;
	
	public static String JSONFilesPreferencePage_ExtensionError;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, JSONCoreMessages.class);
	}

	private JSONCoreMessages() {
		// cannot create new instance
	}

	public static ResourceBundle getResourceBundle() {
		try {
			if (fResourceBundle == null) {
				fResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
			}
		} catch (MissingResourceException x) {
			fResourceBundle = null;
		}
		return fResourceBundle;
	}
}

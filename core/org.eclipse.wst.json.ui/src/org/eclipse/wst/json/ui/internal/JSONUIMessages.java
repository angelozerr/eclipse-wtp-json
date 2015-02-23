package org.eclipse.wst.json.ui.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class JSONUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.json.ui.internal.JSONUIMessages";//$NON-NLS-1$
	private static ResourceBundle fResourceBundle;

	public static String refreshoutline_0;
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, JSONUIMessages.class);
	}

	private JSONUIMessages() {
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

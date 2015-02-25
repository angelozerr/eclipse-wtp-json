package org.eclipse.wst.json.ui.internal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class JSONUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.wst.json.ui.internal.JSONUIMessages";//$NON-NLS-1$

	private static ResourceBundle fResourceBundle;

	public static String refreshoutline_0;

	public static String Sample_JSON_doc;
	public static String SyntaxColoringPage_0;
	public static String SyntaxColoringPage_2;
	public static String SyntaxColoringPage_3;
	public static String SyntaxColoringPage_4;
	public static String SyntaxColoringPage_5;
	public static String SyntaxColoringPage_6;
	public static String CURLY_BRACE_UI_;
	public static String COLON_UI_;
	public static String COMMA_UI_;
	public static String NORMAL_UI_;
	public static String OBJECT_KEY_UI_;
	public static String VALUE_BOOLEAN_UI_;
	public static String VALUE_NULL_UI_;
	public static String VALUE_NUMBER_UI_;
	public static String VALUE_STRING_UI_;
	
	public static String EmptyFilePreferencePage_0;

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

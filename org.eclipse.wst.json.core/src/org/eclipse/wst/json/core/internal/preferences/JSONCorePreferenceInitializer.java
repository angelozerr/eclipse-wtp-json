
package org.eclipse.wst.json.core.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.wst.json.core.internal.JSONCorePlugin;
import org.eclipse.wst.sse.core.internal.encoding.CommonCharsetNames;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;

/**
 * Sets default values for CSS Core preferences
 */
public class JSONCorePreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope().getNode(JSONCorePlugin.getDefault().getBundle().getSymbolicName());

		// formatting preferences
		node.putInt(JSONCorePreferenceNames.LINE_WIDTH, 72);
		node.putBoolean(JSONCorePreferenceNames.CLEAR_ALL_BLANK_LINES, false);
		node.put(JSONCorePreferenceNames.INDENTATION_CHAR, JSONCorePreferenceNames.TAB);
		node.putInt(JSONCorePreferenceNames.INDENTATION_SIZE, 1);

		// cleanup preferences
		node.putBoolean(JSONCorePreferenceNames.QUOTE_ATTR_VALUES, true);
		node.putBoolean(JSONCorePreferenceNames.FORMAT_SOURCE, true);

		// code generation preferences
		node.put(CommonEncodingPreferenceNames.INPUT_CODESET, ""); //$NON-NLS-1$
		String defaultEnc = "UTF-8";//$NON-NLS-1$
		String systemEnc = System.getProperty("file.encoding"); //$NON-NLS-1$
		if (systemEnc != null) {
			defaultEnc = CommonCharsetNames.getPreferredDefaultIanaName(systemEnc, "UTF-8");//$NON-NLS-1$
		}
		node.put(CommonEncodingPreferenceNames.OUTPUT_CODESET, defaultEnc);
		node.put(CommonEncodingPreferenceNames.END_OF_LINE_CODE, ""); //$NON-NLS-1$

		// this could be made smarter by actually looking up the content
		// type's valid extensions
		node.put(JSONCorePreferenceNames.DEFAULT_EXTENSION, "css"); //$NON-NLS-1$

		// additional css core preferences
		node.putInt(JSONCorePreferenceNames.FORMAT_PROP_PRE_DELIM, 0);
		node.putInt(JSONCorePreferenceNames.FORMAT_PROP_POST_DELIM, 1);
		node.put(JSONCorePreferenceNames.FORMAT_QUOTE, "\"");//$NON-NLS-1$
		node.put(JSONCorePreferenceNames.FORMAT_BETWEEN_VALUE, " ");//$NON-NLS-1$
		node.putBoolean(JSONCorePreferenceNames.FORMAT_SPACE_BETWEEN_SELECTORS, true);
		node.putBoolean(JSONCorePreferenceNames.FORMAT_QUOTE_IN_URI, true);
		node.putBoolean(JSONCorePreferenceNames.WRAPPING_ONE_PER_LINE, true);
		node.putBoolean(JSONCorePreferenceNames.WRAPPING_PROHIBIT_WRAP_ON_ATTR, true);
		node.putBoolean(JSONCorePreferenceNames.WRAPPING_NEWLINE_ON_OPEN_BRACE, false);
		node.putInt(JSONCorePreferenceNames.CASE_IDENTIFIER, JSONCorePreferenceNames.UPPER);
		node.putInt(JSONCorePreferenceNames.CASE_SELECTOR, JSONCorePreferenceNames.LOWER);
		node.putInt(JSONCorePreferenceNames.CASE_PROPERTY_NAME, JSONCorePreferenceNames.LOWER);
		node.putInt(JSONCorePreferenceNames.CASE_PROPERTY_VALUE, JSONCorePreferenceNames.LOWER);

	}

}

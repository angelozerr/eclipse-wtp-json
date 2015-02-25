package org.eclipse.wst.json.ui.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wst.json.core.regions.JSONRegionContexts;
import org.eclipse.wst.json.ui.internal.style.IStyleConstantsJSON;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ColorHelper;
import org.eclipse.wst.sse.ui.internal.util.EditorUtility;

public class ColorTypesHelper {

	/** Contains region to style mapping */
	private static final Map<String, String> fColorTypes;

	static {
		fColorTypes = new HashMap<String, String>();
		fColorTypes.put(JSONRegionContexts.JSON_OBJECT_OPEN,
				IStyleConstantsJSON.CURLY_BRACE);
		fColorTypes.put(JSONRegionContexts.JSON_OBJECT_CLOSE,
				IStyleConstantsJSON.CURLY_BRACE);
		fColorTypes.put(JSONRegionContexts.JSON_ARRAY_OPEN,
				IStyleConstantsJSON.CURLY_BRACE);
		fColorTypes.put(JSONRegionContexts.JSON_ARRAY_CLOSE,
				IStyleConstantsJSON.CURLY_BRACE);
		fColorTypes.put(JSONRegionContexts.JSON_COLON,
				IStyleConstantsJSON.COLON);
		fColorTypes.put(JSONRegionContexts.JSON_COMMA,
				IStyleConstantsJSON.COMMA);

		fColorTypes.put(JSONRegionContexts.JSON_OBJECT_KEY,
				IStyleConstantsJSON.OBJECT_KEY);
		fColorTypes.put(JSONRegionContexts.JSON_VALUE_STRING,
				IStyleConstantsJSON.VALUE_STRING);
		fColorTypes.put(JSONRegionContexts.JSON_VALUE_NUMBER,
				IStyleConstantsJSON.VALUE_NUMBER);
		fColorTypes.put(JSONRegionContexts.JSON_VALUE_BOOLEAN,
				IStyleConstantsJSON.VALUE_BOOLEAN);
		fColorTypes.put(JSONRegionContexts.JSON_VALUE_NULL,
				IStyleConstantsJSON.VALUE_NULL);
	}

	public static String getColor(String type) {
		return fColorTypes.get(type);
	}

	public static String getNewStyle(PropertyChangeEvent event) {
		return null;
	}
}

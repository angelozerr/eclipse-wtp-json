package org.eclipse.wst.json.ui.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.json.ui.internal.JSONUIPlugin;
import org.eclipse.wst.json.ui.internal.style.IStyleConstantsJSON;
import org.eclipse.wst.sse.ui.internal.preferences.ui.ColorHelper;

/**
 * Sets default values for JSON UI preferences
 */
public class JSONUIPreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JSONUIPlugin.getDefault().getPreferenceStore();
		ColorRegistry registry = PlatformUI.getWorkbench().getThemeManager()
				.getCurrentTheme().getColorRegistry();

		// JSON Style Preferences
		String NOBACKGROUNDBOLD = " | null | false"; //$NON-NLS-1$
		String JUSTITALIC = " | null | false | true"; //$NON-NLS-1$
		String JUSTBOLD = " | null | true";
		String styleValue = "null" + NOBACKGROUNDBOLD; //$NON-NLS-1$
		store.setDefault(IStyleConstantsJSON.NORMAL, styleValue);

		styleValue = "null" + NOBACKGROUNDBOLD; //$NON-NLS-1$
		store.setDefault(IStyleConstantsJSON.COLON, styleValue);
		store.setDefault(IStyleConstantsJSON.CURLY_BRACE, styleValue);

	}

}

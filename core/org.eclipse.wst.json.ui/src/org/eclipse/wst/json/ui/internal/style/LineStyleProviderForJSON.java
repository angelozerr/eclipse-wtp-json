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
package org.eclipse.wst.json.ui.internal.style;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.wst.json.core.regions.JSONRegionContexts;
import org.eclipse.wst.json.ui.internal.ColorTypesHelper;
import org.eclipse.wst.json.ui.internal.JSONUIPlugin;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.internal.provisional.style.AbstractLineStyleProvider;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;

/**
 * Line style provider for JSON.
 */
public class LineStyleProviderForJSON extends AbstractLineStyleProvider
		implements LineStyleProvider {

	public LineStyleProviderForJSON() {
		super();
	}

	@Override
	protected TextAttribute getAttributeFor(ITextRegion region) {
		if (region != null) {
			String type = region.getType();
			if (type != null) {
				return getAttributeFor(type);
			}
		}
		return (TextAttribute) getTextAttributes().get(
				IStyleConstantsJSON.NORMAL);
	}

	/**
	 * Look up the TextAttribute for the given region context. Might return null
	 * for unusual text.
	 * 
	 * @param type
	 * @return
	 */
	protected TextAttribute getAttributeFor(String type) {
		return (TextAttribute) getTextAttributes().get(
				ColorTypesHelper.getColor(type));
	}

	@Override
	protected void handlePropertyChange(PropertyChangeEvent event) {
		String styleKey = ColorTypesHelper.getNewStyle(event);

		/*
		 * if (event != null) { String prefKey = event.getProperty(); // check
		 * if preference changed is a style preference if
		 * (IStyleConstantsCSS.ATMARK_RULE.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.ATMARK_RULE; } else if
		 * (IStyleConstantsCSS.COLON.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.COLON; } else if
		 * (IStyleConstantsCSS.COMMENT.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.COMMENT; } else if
		 * (IStyleConstantsCSS.CURLY_BRACE.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.CURLY_BRACE; } else if
		 * (IStyleConstantsCSS.ERROR.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.ERROR; } else if
		 * (IStyleConstantsCSS.MEDIA.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.MEDIA; } else if
		 * (IStyleConstantsCSS.NORMAL.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.NORMAL; } else if
		 * (IStyleConstantsCSS.ATTRIBUTE_DELIM.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.ATTRIBUTE_DELIM; } else if
		 * (IStyleConstantsCSS.ATTRIBUTE_NAME.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.ATTRIBUTE_NAME; } else if
		 * (IStyleConstantsCSS.ATTRIBUTE_OPERATOR.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.ATTRIBUTE_OPERATOR; } else if
		 * (IStyleConstantsCSS.ATTRIBUTE_VALUE.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.ATTRIBUTE_VALUE; } else if
		 * (IStyleConstantsCSS.COMBINATOR.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.COMBINATOR; } else if
		 * (IStyleConstantsCSS.PROPERTY_NAME.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.PROPERTY_NAME; } else if
		 * (IStyleConstantsCSS.PROPERTY_VALUE.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.PROPERTY_VALUE; } else if
		 * (IStyleConstantsCSS.SELECTOR.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.SELECTOR; } else if
		 * (IStyleConstantsCSS.UNIVERSAL.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.UNIVERSAL; } else if
		 * (IStyleConstantsCSS.ID.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.ID; } else if
		 * (IStyleConstantsCSS.PSEUDO.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.PSEUDO; } else if
		 * (IStyleConstantsCSS.SELECTOR_CLASS.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.SELECTOR_CLASS; } else if
		 * (IStyleConstantsCSS.SEMI_COLON.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.SEMI_COLON; } else if
		 * (IStyleConstantsCSS.STRING.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.STRING; } else if
		 * (IStyleConstantsCSS.URI.equals(prefKey)) { styleKey =
		 * IStyleConstantsCSS.URI; } } else { // this is around for old
		 * deprecated preferencesChanged() method // TODO remove when
		 * preferencesChanged() is removed loadColors();
		 * super.handlePropertyChange(event); }
		 */

		if (styleKey != null) {
			// overwrite style preference with new value
			addTextAttribute(styleKey);
			super.handlePropertyChange(event);
		}
	}

	@Override
	public void release() {
		super.release();
	}

	@Override
	public void loadColors() {
		addTextAttribute(IStyleConstantsJSON.NORMAL);
		addTextAttribute(IStyleConstantsJSON.CURLY_BRACE);
		addTextAttribute(IStyleConstantsJSON.COLON);
		addTextAttribute(IStyleConstantsJSON.COMMA);

		addTextAttribute(IStyleConstantsJSON.OBJECT_KEY);
		addTextAttribute(IStyleConstantsJSON.VALUE_STRING);
		addTextAttribute(IStyleConstantsJSON.VALUE_NUMBER);
		addTextAttribute(IStyleConstantsJSON.VALUE_BOOLEAN);
		addTextAttribute(IStyleConstantsJSON.VALUE_NULL);
		/*
		 * addTextAttribute(IStyleConstantsCSS.ATMARK_RULE);
		 * addTextAttribute(IStyleConstantsCSS.COLON);
		 * addTextAttribute(IStyleConstantsCSS.COMMENT);
		 * addTextAttribute(IStyleConstantsCSS.CURLY_BRACE);
		 * addTextAttribute(IStyleConstantsCSS.ERROR);
		 * addTextAttribute(IStyleConstantsCSS.MEDIA);
		 * addTextAttribute(IStyleConstantsCSS.NORMAL);
		 * addTextAttribute(IStyleConstantsCSS.PROPERTY_NAME);
		 * addTextAttribute(IStyleConstantsCSS.PROPERTY_VALUE);
		 * addTextAttribute(IStyleConstantsCSS.SELECTOR);
		 * addTextAttribute(IStyleConstantsCSS.UNIVERSAL);
		 * addTextAttribute(IStyleConstantsCSS.ATTRIBUTE_DELIM);
		 * addTextAttribute(IStyleConstantsCSS.ATTRIBUTE_NAME);
		 * addTextAttribute(IStyleConstantsCSS.ATTRIBUTE_OPERATOR);
		 * addTextAttribute(IStyleConstantsCSS.ATTRIBUTE_VALUE);
		 * addTextAttribute(IStyleConstantsCSS.COMBINATOR);
		 * addTextAttribute(IStyleConstantsCSS.ID);
		 * addTextAttribute(IStyleConstantsCSS.SELECTOR_CLASS);
		 * addTextAttribute(IStyleConstantsCSS.PSEUDO);
		 * addTextAttribute(IStyleConstantsCSS.SEMI_COLON);
		 * addTextAttribute(IStyleConstantsCSS.STRING);
		 * addTextAttribute(IStyleConstantsCSS.URI);
		 */
	}

	protected IPreferenceStore getColorPreferences() {
		return JSONUIPlugin.getDefault().getPreferenceStore();
	}
}

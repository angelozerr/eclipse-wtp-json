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
package org.eclipse.wst.json.core.internal.formatter;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.wst.json.core.internal.JSONCorePlugin;
import org.eclipse.wst.json.core.internal.preferences.JSONCorePreferenceNames;

public class JSONFormattingPreferences {
	public final static String PRESERVE = JSONFormattingConstraints.PRESERVE;
	public final static String COLLAPSE = JSONFormattingConstraints.COLLAPSE;
	public final static String IGNORE = JSONFormattingConstraints.IGNORE;

	public final static String INDENT = JSONFormattingConstraints.INDENT;
	public final static String NEW_LINE = JSONFormattingConstraints.NEW_LINE;
	public final static String INLINE = JSONFormattingConstraints.INLINE;

	private int fMaxLineWidth = 72;
	private boolean fAlignFinalBracket = false;
	private boolean fSpaceBeforeEmptyCloseTag = true;
	private boolean fIndentMultipleAttributes = false;

	private String fPCDataWhitespaceStrategy = JSONFormattingConstraints.PRESERVE;
	private String fTextIndentStrategy = JSONFormattingConstraints.INLINE;
	private String fTextWhitespaceStrategy = JSONFormattingConstraints.COLLAPSE;
	private String fElementIndentStrategy = JSONFormattingConstraints.INDENT;
	private String fElementWhitespaceStrategy = JSONFormattingConstraints.IGNORE;
	private String fMixedIndentStrategy = JSONFormattingConstraints.INDENT;
	private String fMixedWhitespaceStrategy = JSONFormattingConstraints.IGNORE;
	private String fOneIndent = "\t"; //$NON-NLS-1$
	private boolean fClearAllBlankLines = false;

	public JSONFormattingPreferences() {
		Preferences preferences = JSONCorePlugin.getDefault()
				.getPluginPreferences();
		if (preferences != null) {

			setMaxLineWidth(preferences
					.getInt(JSONCorePreferenceNames.LINE_WIDTH));
//			setIndentMultipleAttributes(preferences
//					.getBoolean(JSONCorePreferenceNames.SPLIT_MULTI_ATTRS));
//			setAlignFinalBracket(preferences
//					.getBoolean(JSONCorePreferenceNames.ALIGN_END_BRACKET));
//			setSpaceBeforeEmptyCloseTag(preferences
//					.getBoolean(JSONCorePreferenceNames.SPACE_BEFORE_EMPTY_CLOSE_TAG));

			char indentChar = ' ';
			String indentCharPref = preferences
					.getString(JSONCorePreferenceNames.INDENTATION_CHAR);
			if (JSONCorePreferenceNames.TAB.equals(indentCharPref)) {
				indentChar = '\t';
			}
			int indentationWidth = preferences
					.getInt(JSONCorePreferenceNames.INDENTATION_SIZE);

			StringBuffer indent = new StringBuffer();
			for (int i = 0; i < indentationWidth; i++) {
				indent.append(indentChar);
			}
			setOneIndent(indent.toString());
			setClearAllBlankLines(preferences
					.getBoolean(JSONCorePreferenceNames.CLEAR_ALL_BLANK_LINES));
		}
	}

	public int getMaxLineWidth() {
		return fMaxLineWidth;
	}

	public boolean getAlignFinalBracket() {
		return fAlignFinalBracket;
	}

	public boolean getSpaceBeforeEmptyCloseTag() {
		return fSpaceBeforeEmptyCloseTag;
	}

	public boolean getIndentMultipleAttributes() {
		return fIndentMultipleAttributes;
	}

	public String getPCDataWhitespaceStrategy() {
		return fPCDataWhitespaceStrategy;
	}

	public String getTextIndentStrategy() {
		return fTextIndentStrategy;
	}

	public String getTextWhitespaceStrategy() {
		return fTextWhitespaceStrategy;
	}

	public String getElementIndentStrategy() {
		return fElementIndentStrategy;
	}

	public String getElementWhitespaceStrategy() {
		return fElementWhitespaceStrategy;
	}


	public void setIndentMultipleAttributes(boolean indentMultipleAttributes) {
		fIndentMultipleAttributes = indentMultipleAttributes;
	}

	public void setAlignFinalBracket(boolean alignFinalBracket) {
		fAlignFinalBracket = alignFinalBracket;
	}

	public String getMixedIndentStrategy() {
		return fMixedIndentStrategy;
	}

	public void setMixedIndentStrategy(String mixedIndentStrategy) {
		fMixedIndentStrategy = mixedIndentStrategy;
	}

	public String getMixedWhitespaceStrategy() {
		return fMixedWhitespaceStrategy;
	}

	public void setMixedWhitespaceStrategy(String mixedWhitespaceStrategy) {
		fMixedWhitespaceStrategy = mixedWhitespaceStrategy;
	}

	public String getOneIndent() {
		return fOneIndent;
	}

	protected void setMaxLineWidth(int maxLineWidth) {
		fMaxLineWidth = maxLineWidth;
	}

	protected void setOneIndent(String oneIndent) {
		fOneIndent = oneIndent;
	}

	public boolean getClearAllBlankLines() {
		return fClearAllBlankLines;
	}

	public void setClearAllBlankLines(boolean clearAllBlankLines) {
		fClearAllBlankLines = clearAllBlankLines;
	}
}

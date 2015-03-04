/**
 *  Copyright (c) 2015 - present Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.ui.internal.text;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.internal.text.html.BrowserInformationControlInput;

/**
 * Browser input for JSON hover.
 *
 */
public class JSONBrowserInformationControlInput extends
		BrowserInformationControlInput {

	private final String fHtml;
	private final int fLeadingImageWidth;

	/**
	 * Creates a new browser information control input.
	 *
	 * @param previous
	 *            previous input, or <code>null</code> if none available
	 * @param element
	 *            the element, or <code>null</code> if none available
	 * @param html
	 *            HTML contents, must not be null
	 * @param leadingImageWidth
	 *            the indent required for the element image
	 */
	public JSONBrowserInformationControlInput(
			JSONBrowserInformationControlInput previous, String html,
			int leadingImageWidth) {
		super(previous);
		Assert.isNotNull(html);
		fHtml = html;
		fLeadingImageWidth = leadingImageWidth;
	}

	/*
	 * @see org.eclipse.jface.internal.text.html.BrowserInformationControlInput#
	 * getLeadingImageWidth()
	 * 
	 * @since 3.4
	 */
	@Override
	public int getLeadingImageWidth() {
		return fLeadingImageWidth;
	}

	/*
	 * @see org.eclipse.jface.internal.text.html.BrowserInput#getHtml()
	 */
	@Override
	public String getHtml() {
		return fHtml;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.infoviews.BrowserInput#getInputElement()
	 */
	@Override
	public Object getInputElement() {
		return (Object) fHtml;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.infoviews.BrowserInput#getInputName()
	 */
	@Override
	public String getInputName() {
		return "";
	}

}

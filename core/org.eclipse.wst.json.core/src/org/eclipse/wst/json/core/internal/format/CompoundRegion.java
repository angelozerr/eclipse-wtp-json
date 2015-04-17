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
package org.eclipse.wst.json.core.internal.format;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

class CompoundRegion {

	CompoundRegion(IStructuredDocumentRegion documentRegion, ITextRegion textRegion) {
		super();
		this.fDocumentRegion = documentRegion;
		this.fTextRegion = textRegion;
	}

	IStructuredDocumentRegion getDocumentRegion() {
		return fDocumentRegion;
	}

	ITextRegion getTextRegion() {
		return fTextRegion;
	}

	String getType() {
		return fTextRegion.getType();
	}

	String getText() {
		return fDocumentRegion.getText(fTextRegion);
	}
	
	// Bug 218993: Added to get text with whitespace for cleanup
	// without formatting
	String getFullText() {
		return fDocumentRegion.getFullText(fTextRegion);
	}

	int getStartOffset() {
		return fDocumentRegion.getStartOffset(fTextRegion);
	}

	int getEndOffset() {
		return fDocumentRegion.getEndOffset(fTextRegion);
	}


	private IStructuredDocumentRegion fDocumentRegion;
	private ITextRegion fTextRegion;

}

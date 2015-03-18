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
package org.eclipse.wst.json.ui.contentassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.ui.internal.JSONUIMessages;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.w3c.dom.Node;

public abstract class AbstractJSONCompletionProposalComputer implements
		ICompletionProposalComputer {

	private String fErrorMessage;
	private ITextViewer fTextViewer;

	public AbstractJSONCompletionProposalComputer() {
		fErrorMessage = null;
		fTextViewer = null;
	}

	@Override
	public List computeCompletionProposals(
			CompletionProposalInvocationContext context,
			IProgressMonitor monitor) {

		ITextViewer textViewer = context.getViewer();
		int documentPosition = context.getInvocationOffset();

		setErrorMessage(null);

		fTextViewer = textViewer;

		IndexedRegion treeNode = ContentAssistUtils.getNodeAt(textViewer,
				documentPosition);

		IJSONNode node = null;
		ContentAssistRequest contentAssistRequest = null;

		IStructuredDocumentRegion sdRegion = getStructuredDocumentRegion(documentPosition);
		ITextRegion completionRegion = getCompletionRegion(documentPosition,
				node);

		String matchString = getMatchString(sdRegion, completionRegion,
				documentPosition);

		contentAssistRequest = new ContentAssistRequest();
		
		/*
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=123892 Only set this
		 * error message if nothing else was already set
		 */
		if (contentAssistRequest.getProposals().size() == 0
				&& getErrorMessage() == null) {
			setErrorMessage(JSONUIMessages.Content_Assist_not_availab_UI_);
		}

		ICompletionProposal[] props = contentAssistRequest
				.getCompletionProposals();
		return (props != null) ? Arrays.asList(props) : new ArrayList(0);
	}

	/**
	 * @param errorMessage
	 *            the reason why computeProposals was not able to find any
	 *            completions.
	 */
	protected void setErrorMessage(String errorMessage) {
		fErrorMessage = errorMessage;
	}

	/**
	 * Return the region whose content's require completion. This is something
	 * of a misnomer as sometimes the user wants to be prompted for contents of
	 * a non-existant ITextRegion, such as for enumerated attribute values
	 * following an '=' sign.
	 */
	private ITextRegion getCompletionRegion(int documentPosition,
			IJSONNode domnode) {
		if (domnode == null) {
			return null;
		}

		return null;
	}

	private String getMatchString(IStructuredDocumentRegion parent,
			ITextRegion aRegion, int offset) {

		return "";
	}

	/**
	 * StructuredTextViewer must be set before using this.
	 */
	private IStructuredDocumentRegion getStructuredDocumentRegion(int pos) {
		return ContentAssistUtils.getStructuredDocumentRegion(fTextViewer, pos);
	}
}

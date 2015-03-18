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
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.ui.internal.contentassist.ProposalComparator;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

public class ContentAssistRequest {

	protected IStructuredDocumentRegion documentRegion = null;
	protected List macros = new ArrayList();
	protected String matchString;
	protected IJSONNode node = null;
	protected IJSONNode parent = null;
	protected List proposals = new ArrayList();
	protected ITextRegion region = null;
	protected int replacementBeginPosition;
	protected int replacementLength;

	public void addProposal(ICompletionProposal newProposal) {
		proposals.add(newProposal);
	}

	public ICompletionProposal[] getCompletionProposals() {
		ICompletionProposal results[] = null;
		if ((getProposals().size() > 0)/* || (getMacros().size() > 0) */) {
			List allProposals = new ArrayList();
			//if (!shouldSeparate()) {
				allProposals.addAll(getProposals());
				// should be empty, as all macros should have gone into the
				// proposal list
				// allProposals.addAll(getMacros());
				allProposals = sortProposals(allProposals);
//			} else {
//				allProposals.addAll(sortProposals(getProposals()));
//				//allProposals.addAll(sortProposals(getMacros()));
//			}

			results = new ICompletionProposal[allProposals.size()];
			for (int i = 0; i < allProposals.size(); i++) {
				results[i] = (ICompletionProposal) allProposals.get(i);
			}
		}
		return results;
	}

	public IStructuredDocumentRegion getDocumentRegion() {
		return documentRegion;
	}

	public java.util.List getProposals() {
		return proposals;
	}

	protected List sortProposals(List proposalsIn) {
		Collections.sort(proposalsIn, new ProposalComparator());
		return proposalsIn;

	}

	/**
	 * 
	 * @return java.lang.String
	 */
	// public java.lang.String toString() {
	//		return "Node: " + getNode() //$NON-NLS-1$
	//					+ "\nParent: " + getParent() //$NON-NLS-1$
	//					+ "\nStructuredDocumentRegion: " + StringUtils.escape(getDocumentRegion().toString()) //$NON-NLS-1$
	//					+ "\nRegion: " + getRegion() //$NON-NLS-1$
	//					+ "\nMatch string: '" + StringUtils.escape(getMatchString()) + "'" //$NON-NLS-2$//$NON-NLS-1$
	//					+ "\nOffsets: [" + getReplacementBeginPosition() + "-" + (getReplacementBeginPosition() + getReplacementLength()) + "]\n"; //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	// }

}

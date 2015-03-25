package org.eclipse.wst.json.npm.ui.internal.contentassist;

import org.eclipse.wst.json.ui.contentassist.ContentAssistRequest;
import org.eclipse.wst.json.ui.contentassist.ICompletionProposalCollector;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;

public class NPMDependencyCompletionProposalCollector implements
		ICompletionProposalCollector {

	@Override
	public void addProposals(ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context, TargetType target) {
		System.err.println(target);
	}

}

package org.eclipse.wst.json.ui.contentassist;

import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;

public interface ICompletionProposalCollector {

	public enum TargetType {
		key, value;
	}

	void addProposals(ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context, TargetType target);
}

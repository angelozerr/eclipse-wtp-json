package org.eclipse.wst.json.ui.internal.contentassist;

import java.util.Collection;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.jsonpath.JSONPath;
import org.eclipse.wst.json.core.jsonpath.JSONPathMatcher;
import org.eclipse.wst.json.ui.contentassist.ContentAssistRequest;
import org.eclipse.wst.json.ui.contentassist.ICompletionProposalCollector;
import org.eclipse.wst.json.ui.contentassist.ICompletionProposalCollector.TargetType;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;

public class CompletionProposalMatcher {

	private final Collection<TargetType> targets;
	private final JSONPath[] paths;
	private final ICompletionProposalCollector collector;

	public CompletionProposalMatcher(Collection<TargetType> targets,
			JSONPath[] paths, ICompletionProposalCollector collector) {
		this.targets = targets;
		this.paths = paths;
		this.collector = collector;
	}

	public void addProposalsIfMatch(ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context, TargetType target) {
		if (isMatchTarget(target)) {
			// match target
			if (isMatchPaths(contentAssistRequest.getNode())) {
				// match paths
				collector.addProposals(contentAssistRequest, context, target);
			}
		}
	}

	private boolean isMatchPaths(IJSONNode node) {
		for (int i = 0; i < paths.length; i++) {
			if (JSONPathMatcher.isMatch(node, paths[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isMatchTarget(TargetType target) {
		return targets.contains(target);
	}

}

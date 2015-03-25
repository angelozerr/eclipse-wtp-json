/**
 *  Copyright (c) 2015-spresent Angelo ZERR.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.ui.internal.contentassist;

import java.io.IOException;

import org.eclipse.json.schema.IJSONSchemaDocument;
import org.eclipse.json.schema.IJSONSchemaProperty;
import org.eclipse.json.schema.JSONSchemaType;
import org.eclipse.wst.json.core.JSONCorePlugin;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.ui.contentassist.AbstractJSONCompletionProposalComputer;
import org.eclipse.wst.json.ui.contentassist.ContentAssistRequest;
import org.eclipse.wst.json.ui.contentassist.ICompletionProposalCollector.TargetType;
import org.eclipse.wst.json.ui.contentassist.JSONKeyCompletionProposal;
import org.eclipse.wst.json.ui.contentassist.JSONRelevanceConstants;
import org.eclipse.wst.json.ui.internal.Logger;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;

public class JSONCompletionProposalComputer extends
		AbstractJSONCompletionProposalComputer {

	@Override
	public void sessionStarted() {
		// default is to do nothing
	}

	@Override
	protected void addObjectKeyProposals(
			ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		collectProposalsFromSchema(contentAssistRequest, context);
		collectProposalsFromExtensionPoint(contentAssistRequest, context);
	}

	/**
	 * Collect completion proposals from
	 * "org.eclipse.wst.json.ui.completionProposalCollectors" extension point.
	 * 
	 * @param contentAssistRequest
	 * @param context
	 */
	private void collectProposalsFromExtensionPoint(
			ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		CompletionProposalCollectorsRegistryReader.getInstance().addProposals(
				contentAssistRequest, context, TargetType.key);
	}

	/**
	 * Collect completion proposals from JSON Schema.
	 * 
	 * @param contentAssistRequest
	 * @param context
	 */
	private void collectProposalsFromSchema(
			ContentAssistRequest contentAssistRequest,
			CompletionProposalInvocationContext context) {
		try {
			IJSONNode node = contentAssistRequest.getNode();
			IJSONSchemaDocument schemaDocument = JSONCorePlugin.getDefault()
					.getSchemaDocument(node);
			if (schemaDocument != null) {
				String matchString = contentAssistRequest.getMatchString();
				if (matchString == null) {
					matchString = ""; //$NON-NLS-1$
				}
				if ((matchString.length() > 0)
						&& (matchString.startsWith("\""))) { //$NON-NLS-1$ //$NON-NLS-2$
					matchString = matchString.substring(1);
				}
				// Loop for each properties of the JSON Schema.
				IJSONSchemaProperty parentProperty = schemaDocument
						.getProperty(node.getPath());
				if (parentProperty != null) {
					for (IJSONSchemaProperty property : parentProperty
							.getProperties()) {
						boolean showProperty = beginsWith(property.getName(),
								matchString.trim());
						if (showProperty) {
							String replacementString = getRequiredName(node,
									property);
							String additionalProposalInfo = property
									.getDescription();
							JSONKeyCompletionProposal proposal = new JSONKeyCompletionProposal(
									replacementString,
									contentAssistRequest
											.getReplacementBeginPosition(),
									contentAssistRequest.getReplacementLength(),
									replacementString.length() - 2, null,
									property.getName(), null,
									additionalProposalInfo,
									JSONRelevanceConstants.R_OBJECT_KEY);
							contentAssistRequest.addProposal(proposal);
						}
					}
				}
			}
		} catch (IOException e) {
			Logger.logException(e);
		}
	}

	private String getRequiredName(IJSONNode parent,
			IJSONSchemaProperty property) {
		StringBuilder name = new StringBuilder("\"");
		name.append(property.getName());
		name.append("\"");
		if (property.getType() != null && property.getType().length > 0) {
			JSONSchemaType type = property.getType()[0];
			name.append(":");
			switch (type) {
			case Array:
				name.append("[");
				name.append("]");
				break;
			case Boolean:
				name.append("false");
				break;
			case Null:
				name.append("null");
				break;
			case Object:
				name.append("{");
				name.append("}");
				break;
			case String:
				name.append("\"\"");
				break;
			default:
				break;
			}
		}
		return name.toString();
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public void sessionEnded() {
		// default is to do nothing
	}

}

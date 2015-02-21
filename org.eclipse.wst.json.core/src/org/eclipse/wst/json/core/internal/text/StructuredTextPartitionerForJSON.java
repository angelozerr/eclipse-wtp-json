package org.eclipse.wst.json.core.internal.text;

import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.wst.json.core.text.IJSONPartitions;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.text.rules.StructuredTextPartitioner;
import org.eclipse.wst.sse.core.text.IStructuredPartitions;

public class StructuredTextPartitionerForJSON extends StructuredTextPartitioner {

	public final static String[] legalTypes = new String[] {
			IJSONPartitions.JSON, IStructuredPartitions.DEFAULT_PARTITION };

	public StructuredTextPartitionerForJSON() {
		super();
	}

	@Override
	public String getDefaultPartitionType() {
		return IJSONPartitions.JSON;
	}

	@Override
	public String[] getLegalContentTypes() {
		return legalTypes;
	}

	public IDocumentPartitioner newInstance() {
		return new StructuredTextPartitionerForJSON();
	}

}

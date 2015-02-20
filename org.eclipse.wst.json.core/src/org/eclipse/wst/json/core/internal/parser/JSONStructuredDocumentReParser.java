package org.eclipse.wst.json.core.internal.parser;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredTextReParser;
import org.eclipse.wst.sse.core.internal.text.StructuredDocumentReParser;

public class JSONStructuredDocumentReParser extends StructuredDocumentReParser {

	@Override
	public IStructuredTextReParser newInstance() {
		return new JSONStructuredDocumentReParser();
	}
}

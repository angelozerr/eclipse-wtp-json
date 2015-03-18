package org.eclipse.wst.json.core.document;

import org.eclipse.wst.json.core.TestUtil;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.junit.Assert;
import org.junit.Test;

public class JSONArrayDocumentChangesTest {

	@Test
	public void testInsertCloseArray() {
		IJSONModel model = TestUtil.loadModel("[");
		IJSONDocument document = model.getDocument();

		// Document contains an Array which is not closed
		Assert.assertNotNull(document.getFirstChild());
		Assert.assertTrue(document.getFirstChild() instanceof IJSONArray);
		IJSONArray array = (IJSONArray) document.getFirstChild();
		Assert.assertFalse(array.isClosed());

		IStructuredDocument structuredDocument = model.getStructuredDocument();
		// insert at end ']'
		structuredDocument.replaceText(structuredDocument, 1, 0, "]");
		Assert.assertTrue(array.isClosed());
	}
}

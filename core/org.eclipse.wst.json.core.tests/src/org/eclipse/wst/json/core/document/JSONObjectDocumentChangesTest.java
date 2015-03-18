package org.eclipse.wst.json.core.document;

import org.eclipse.wst.json.core.TestUtil;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.junit.Assert;
import org.junit.Test;

public class JSONObjectDocumentChangesTest {

	@Test
	public void insertCloseObject() {
		IJSONModel model = TestUtil.loadModel("{");
		IJSONDocument document = model.getDocument();

		// Document contains an Object which is not closed
		Assert.assertNotNull(document.getFirstChild());
		Assert.assertTrue(document.getFirstChild() instanceof IJSONObject);
		IJSONObject object = (IJSONObject) document.getFirstChild();
		Assert.assertFalse(object.isClosed());

		IStructuredDocument structuredDocument = model.getStructuredDocument();
		// insert at end '}'
		structuredDocument.replaceText(structuredDocument, 1, 0, "}");
		Assert.assertTrue(object.isClosed());
	}
	
	@Test
	public void removeObject() {
		IJSONModel model = TestUtil.loadModel("{}");
		IJSONDocument document = model.getDocument();

		// Document contains an Object which is not closed
		Assert.assertNotNull(document.getFirstChild());
		Assert.assertTrue(document.getFirstChild() instanceof IJSONObject);
		IJSONObject object = (IJSONObject) document.getFirstChild();
		Assert.assertTrue(object.isClosed());

		IStructuredDocument structuredDocument = model.getStructuredDocument();
		// remove {}
		structuredDocument.replaceText(structuredDocument, 0, 2, "");
		Assert.assertNull(document.getFirstChild());
	}
	

	@Test
	public void insertSpaces() {
		IJSONModel model = TestUtil.loadModel("{}");
		IJSONDocument document = model.getDocument();

		// Document contains an Object which is not closed
		Assert.assertNotNull(document.getFirstChild());
		Assert.assertTrue(document.getFirstChild() instanceof IJSONObject);
		IJSONObject object = (IJSONObject) document.getFirstChild();
		Assert.assertTrue(object.isClosed());

		IStructuredDocument structuredDocument = model.getStructuredDocument();
		// remove {}
		structuredDocument.replaceText(structuredDocument, 1, 0, "\r\n");
		Assert.assertNotNull(document.getFirstChild());
		Assert.assertTrue(document.getFirstChild() instanceof IJSONObject);
		IJSONObject object2 = (IJSONObject) document.getFirstChild();
		Assert.assertTrue(object2.isClosed());
	}
}

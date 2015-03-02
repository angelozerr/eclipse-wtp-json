package org.eclipse.wst.json.core.document;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.wst.json.core.TestUtil;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.junit.Assert;
import org.junit.Test;

public class JSONSampleDocumentTest {

	@Test
	public void loadSample() throws Exception {

		IJSONModel model = (IJSONModel) TestUtil.createModel();
		IStructuredDocument structuredDocument = model.getStructuredDocument();
		IJSONDocument document = model.getDocument();
		assertNull(document.getFirstChild());

		String actual = TestUtil.toString(JSONSampleDocumentTest.class
				.getResourceAsStream("sample.json"));
		structuredDocument.set(actual);
		assertNotNull(document.getFirstChild());
		Assert.assertTrue(document.getFirstChild() instanceof IJSONObject);

		String s = TestUtil.toString(document);
		System.err.println(actual);
		System.err.println(s);
	}
}

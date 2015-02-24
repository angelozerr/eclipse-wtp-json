package org.eclipse.wst.json.core.document;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.eclipse.wst.json.core.contenttype.ContentTypeIdForJSON;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.junit.Assert;
import org.junit.Test;

public class JSONDocumentTest {

	@Test
	public void testModel() throws Exception {

		IStructuredModel model = createModel();
		Assert.assertTrue(model instanceof IJSONModel);

		IJSONModel jsonModel = (IJSONModel) model;
		IJSONDocument document = jsonModel.getDocument();
		Assert.assertNotNull(document);
	}

	@Test
	public void testDocument() throws Exception {

		IJSONModel model = (IJSONModel) createModel();
//		IStructuredDocument doc = model.getModelHandler()
//				.getDocumentLoader()
//				.createNewStructuredDocument(
//						"test.json",
//						JSONDocumentTest.class
//								.getResourceAsStream("sample.json"));
		IJSONDocument document = model.getDocument();

	}

	private IStructuredModel createModel() {
		IModelManager manager = StructuredModelManager.getModelManager();
		return manager
				.createUnManagedStructuredModelFor(ContentTypeIdForJSON.ContentTypeID_JSON);

	}
}

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
	public void addSpaceBeforeStartObject() throws Exception {

		IJSONModel model = (IJSONModel) createModel();
		IStructuredDocument structuredDocument = model.getStructuredDocument();		
		IJSONDocument document = model.getDocument();
		assertNull(document.getFirstChild());
		
		// Add a JSON Object
		structuredDocument.set("{}");
		assertNotNull(document.getFirstChild());
		Assert.assertTrue(document.getFirstChild() instanceof IJSONObject);
		
		IJSONObject object1 = (IJSONObject)document.getFirstChild();
		// object is closed
		Assert.assertNotNull(object1.getEndStructuredDocumentRegion());
		
		// Add space
		structuredDocument.replaceText(structuredDocument, 0, 0, " ");
		assertNotNull(document.getFirstChild());
		IJSONObject object2 = (IJSONObject)document.getFirstChild();
		Assert.assertNotNull(object2.getEndStructuredDocumentRegion());
		
	}

	private IStructuredModel createModel() {
		IModelManager manager = StructuredModelManager.getModelManager();
		return manager
				.createUnManagedStructuredModelFor(ContentTypeIdForJSON.ContentTypeID_JSON);

	}
}

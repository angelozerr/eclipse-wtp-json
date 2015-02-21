package org.eclipse.wst.json.core.internal.modelhandler;

import org.eclipse.wst.json.core.internal.document.JSONModelImpl;
import org.eclipse.wst.json.core.internal.encoding.JSONDocumentLoader;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.model.AbstractModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;


/**
 * 
 */
public class JSONModelLoader extends AbstractModelLoader {
	/**
	 * CSSLoader constructor comment.
	 */
	public JSONModelLoader() {
		super();
	}

	/*
	 * @see IModelLoader#newModel()
	 */
	public IStructuredModel newModel() {
		IStructuredModel model = new JSONModelImpl();
		// now done in create
		// model.setStructuredDocument(createNewStructuredDocument());
		// model.setFactoryRegistry(defaultFactoryRegistry());
		return model;
	}

	public IModelLoader newInstance() {
		return new JSONModelLoader();
	}

	public IDocumentLoader getDocumentLoader() {
		if (documentLoaderInstance == null) {
			documentLoaderInstance = new JSONDocumentLoader();
		}
		return documentLoaderInstance;
	}
}

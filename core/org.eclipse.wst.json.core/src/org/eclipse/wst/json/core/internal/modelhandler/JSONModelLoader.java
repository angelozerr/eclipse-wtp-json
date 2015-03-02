package org.eclipse.wst.json.core.internal.modelhandler;

import org.eclipse.wst.json.core.internal.document.JSONModelImpl;
import org.eclipse.wst.json.core.internal.encoding.JSONDocumentLoader;
import org.eclipse.wst.sse.core.internal.document.IDocumentLoader;
import org.eclipse.wst.sse.core.internal.model.AbstractModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

/**
 * JSON Model loader.
 */
public class JSONModelLoader extends AbstractModelLoader {

	/**
	 * JSON Loader constructor comment.
	 */
	public JSONModelLoader() {
		super();
	}

	/*
	 * @see IModelLoader#newModel()
	 */
	public IStructuredModel newModel() {
		return new JSONModelImpl();
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

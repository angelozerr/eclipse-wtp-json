package org.eclipse.wst.json.ui.internal.registry;

import org.eclipse.wst.json.core.modelhandler.IIModelHandlerForJSON;
import org.eclipse.wst.sse.core.internal.ltk.modelhandler.IDocumentTypeHandler;
import org.eclipse.wst.sse.core.internal.model.FactoryRegistry;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.sse.ui.internal.provisional.registry.AdapterFactoryProvider;
import org.eclipse.wst.sse.ui.internal.util.Assert;

public class AdapterFactoryProviderJSON implements AdapterFactoryProvider {

	@Override
	public boolean isFor(IDocumentTypeHandler contentTypeDescription) {
		return (contentTypeDescription instanceof IIModelHandlerForJSON);
	}

	public void addAdapterFactories(IStructuredModel structuredModel) {
		// add the normal content based factories to model's registry
		addContentBasedFactories(structuredModel);
	}
	
	protected void addContentBasedFactories(IStructuredModel structuredModel) {
		FactoryRegistry factoryRegistry = structuredModel.getFactoryRegistry();
		Assert.isNotNull(factoryRegistry, "Program Error: client caller must ensure model has factory registry"); //$NON-NLS-1$
		INodeAdapterFactory factory = null;

		factory = factoryRegistry.getFactoryFor(IJFaceNodeAdapter.class);
		if (factory == null) {
			//factory = new JFaceNodeAdapterFactoryCSS(IJFaceNodeAdapter.class, true);
			//factoryRegistry.addFactory(factory);
		}
	}

	public void reinitializeFactories(IStructuredModel structuredModel) {

	}

}

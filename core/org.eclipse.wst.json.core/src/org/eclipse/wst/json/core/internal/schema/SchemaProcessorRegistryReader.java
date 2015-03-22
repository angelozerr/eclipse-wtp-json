package org.eclipse.wst.json.core.internal.schema;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.common.uriresolver.internal.util.URIHelper;
import org.eclipse.wst.json.core.JSONCorePlugin;
import org.eclipse.wst.json.core.document.IJSONModel;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.internal.Logger;
import org.eclipse.wst.json.core.schema.IJSONSchemaDocument;
import org.eclipse.wst.json.core.schema.IJSONSchemaProcessor;

public class SchemaProcessorRegistryReader {

	protected static final String EXTENSION_POINT_ID = "schemaProcessors"; //$NON-NLS-1$
	protected static final String TAG_CONTRIBUTION = "schemaProcessor"; //$NON-NLS-1$

	public static SchemaProcessorRegistryReader INSTANCE = null;

	private IJSONSchemaProcessor defaultProcessor;

	public static SchemaProcessorRegistryReader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SchemaProcessorRegistryReader();
			INSTANCE.readRegistry();
		}
		return INSTANCE;
	}

	public IJSONSchemaProcessor getDefaultProcessor() {
		return defaultProcessor;
	}

	public IJSONSchemaDocument getSchemaDocument(IJSONNode node) throws IOException {
		return getSchemaDocument(node.getModel());
	}

	public IJSONSchemaDocument getSchemaDocument(IJSONModel model)
			throws IOException {
		IJSONSchemaProcessor processor = getDefaultProcessor();
		if (processor == null) {
			return null;
		}
		String base = model.getResolver().getFileBaseLocation();
		/**
		 * We shouldn't assert a failure because the catalog does not require a
		 * base location to operate and it will be called from non-file-based
		 * scenarios.
		 * 
		 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=206176
		 */
		// Assert.isNotNull(base, "Base location is expected to be non null."); //$NON-NLS-1$
		if (base != null) {
			base = URIHelper.addImpliedFileProtocol(base);
		}
		String schemaURL = resolve(base, null, null);
		if (schemaURL != null) {
			return processor.getSchema(schemaURL);
		}
		return null;
	}

	private String getFileMatch(String location) {
		if (location == null) {
			return null;
		}
		int index = location.lastIndexOf('/');
		if (index == -1) {
			index = location.lastIndexOf('\\');
		}
		if (index != -1) {
			return location.substring(index, location.length());
		}
		return location;
	}

	private String resolve(String base, String publicId, String systemId) {
		String result = systemId;
		result = URIResolverPlugin.createResolver().resolve(base, publicId,
				systemId);
		return result;
	}

	/**
	 * read from plugin registry and parse it.
	 */
	protected void readRegistry() {
		IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
		IExtensionPoint point = pluginRegistry
				.getExtensionPoint(JSONCorePlugin.getDefault().getBundle()
						.getSymbolicName(), EXTENSION_POINT_ID);
		if (point != null) {
			IConfigurationElement[] elements = point.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				readElement(elements[i]);
			}
		}
	}

	protected void readElement(IConfigurationElement element) {
		if (TAG_CONTRIBUTION.equals(element.getName())) {
			String id = element.getAttribute("id");
			String name = element.getAttribute("name");
			try {
				IJSONSchemaProcessor schemaProcessor = (IJSONSchemaProcessor) element
						.createExecutableExtension("class");
				this.defaultProcessor = schemaProcessor;
			} catch (CoreException e) {
				Logger.logException(e);
			}
		}

	}

}

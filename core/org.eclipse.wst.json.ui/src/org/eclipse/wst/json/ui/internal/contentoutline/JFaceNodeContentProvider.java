/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.ui.internal.contentoutline;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.json.core.document.IJSONModel;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapterFactory;


/**
 * An ITreeContentProvider for a TreeViewers used to display DOM nodes. This
 * content provider takes an adapter factory to create JFace adapters for the
 * nodes in the tree.
 */
public class JFaceNodeContentProvider implements ITreeContentProvider {

	public JFaceNodeContentProvider() {
		super();
	}

	/**
	 * The visual part that is using this content provider is about to be
	 * disposed. Deallocate all allocated SWT resources.
	 */
	public void dispose() {
	}

	/**
	 * Returns the JFace adapter for the specified object.
	 * 
	 * @param adaptable
	 *            java.lang.Object The object to get the adapter for
	 */
	protected IJFaceNodeAdapter getAdapter(Object adaptable) {
		if (adaptable instanceof INodeNotifier) {
			INodeAdapter adapter = ((INodeNotifier) adaptable).getAdapterFor(IJFaceNodeAdapter.class);
			if (adapter instanceof IJFaceNodeAdapter) {
				return (IJFaceNodeAdapter) adapter;
			}
		}
		return null;
	}

	public Object[] getChildren(Object object) {
		IJFaceNodeAdapter adapter = getAdapter(object);

		if (adapter != null) {
			return adapter.getChildren(object);
		}

		return new Object[0];
	}

	public Object[] getElements(Object object) {
		// The root is usually an instance of an JSONStructuredModel in
		// which case we want to extract the document.
		Object topNode = object;
		if (object instanceof IJSONModel) {
			topNode = ((IJSONModel) object).getDocument();
		}

		IJFaceNodeAdapter adapter = getAdapter(topNode);

		if (adapter != null) {
			return adapter.getElements(topNode);
		}

		return new Object[0];
	}

	public Object getParent(Object object) {
		IJFaceNodeAdapter adapter = getAdapter(object);

		if (adapter != null) {
			return adapter.getParent(object);
		}

		return null;
	}

	public boolean hasChildren(Object object) {
		IJFaceNodeAdapter adapter = getAdapter(object);

		if (adapter != null) {
			return adapter.hasChildren(object);
		}

		return false;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if ((oldInput != null) && (oldInput instanceof IStructuredModel)) {
			IJFaceNodeAdapterFactory factory = (IJFaceNodeAdapterFactory) ((IStructuredModel) oldInput).getFactoryRegistry().getFactoryFor(IJFaceNodeAdapter.class);
			if (factory != null) {
				factory.removeListener(viewer);
			}
		}
		if ((newInput != null) && (newInput instanceof IStructuredModel)) {
			IJFaceNodeAdapterFactory factory = (IJFaceNodeAdapterFactory) ((IStructuredModel) newInput).getFactoryRegistry().getFactoryFor(IJFaceNodeAdapter.class);
			if (factory != null) {
				factory.addListener(viewer);
			}
		}
	}
}

package org.eclipse.wst.json.ui.views.contentoutline;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.wst.sse.ui.views.contentoutline.ContentOutlineConfiguration;

public class JSONContentOutlineConfiguration extends
		ContentOutlineConfiguration {

	@Override
	public IContentProvider getContentProvider(TreeViewer viewer) {
		return null;
	}

}

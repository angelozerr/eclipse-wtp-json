package org.eclipse.wst.json.ui.views.contentoutline;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;

class MyDelegatingStyledCellLabelProvider extends DelegatingStyledCellLabelProvider implements ILabelProvider {

	public MyDelegatingStyledCellLabelProvider(
			IStyledLabelProvider labelProvider) {
		super(labelProvider);
	}

	@Override
	public String getText(Object element) {
		return getStyledText(element).getString();
	}

}

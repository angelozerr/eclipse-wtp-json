package org.eclipse.wst.json.ui.internal.contentoutline;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;

public interface IStyledJFaceNodeAdapter extends IJFaceNodeAdapter {

	StyledString getStyledLabelText(Object element);
}

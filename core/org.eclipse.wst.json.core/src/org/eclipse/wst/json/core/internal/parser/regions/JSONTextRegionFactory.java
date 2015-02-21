package org.eclipse.wst.json.core.internal.parser.regions;

import org.eclipse.wst.sse.core.internal.parser.ContextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

public class JSONTextRegionFactory {

	public synchronized static JSONTextRegionFactory getInstance() {
		if (fInstance == null) {
			fInstance = new JSONTextRegionFactory();
		}
		return fInstance;
	}

	public ITextRegion createRegion(String context, int start, int textLength,
			int length) {
		ITextRegion region = null;
		region = new ContextRegion(context, start, textLength, length);
		return region;
	}

	private JSONTextRegionFactory() {
		super();
	}

	private static JSONTextRegionFactory fInstance = null;

}

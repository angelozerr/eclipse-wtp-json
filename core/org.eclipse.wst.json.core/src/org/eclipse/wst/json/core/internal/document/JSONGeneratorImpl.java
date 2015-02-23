package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;

public class JSONGeneratorImpl implements ISourceGenerator {

	private static final ISourceGenerator INSTANCE = new JSONGeneratorImpl();

	public static ISourceGenerator getInstance() {
		return INSTANCE;
	}

	@Override
	public String generateStartTag(IJSONObject element) {
		return "{";
	}
	
	@Override
	public String generateEndTag(IJSONObject element) {
		return "}";
	}
	
	@Override
	public String generateSource(IJSONNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}

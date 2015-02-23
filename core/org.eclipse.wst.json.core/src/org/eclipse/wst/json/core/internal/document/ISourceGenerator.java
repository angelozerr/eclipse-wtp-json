package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;

public interface ISourceGenerator {

	String generateStartTag(IJSONObject element);

	String generateEndTag(IJSONObject element);

	String generateSource(IJSONNode node);

}

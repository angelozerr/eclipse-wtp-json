/**
 *  Copyright (c) 2015-present Angelo ZERR.
 *  
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wst.json.core.internal.format;

import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;

/**
 * 
 */
public class JSONSourceFormatterFactory {
	/**
	 * 
	 */
	private JSONSourceFormatterFactory() {
		super();
	}

	/**
	 * 
	 */
	public IJSONSourceGenerator getSourceFormatter(INodeNotifier target) {
		IJSONNode node = (IJSONNode) target;
		short type = node.getNodeType();
		switch (type) {
		// case IJSONNode.CHARSETRULE_NODE :
		// return CharsetRuleFormatter.getInstance();
		// case IJSONNode.FONTFACERULE_NODE :
		// return FontFaceRuleFormatter.getInstance();
		// case IJSONNode.IMPORTRULE_NODE :
		// return ImportRuleFormatter.getInstance();
		// case IJSONNode.MEDIALIST_NODE :
		// return MediaListFormatter.getInstance();
		// case IJSONNode.MEDIARULE_NODE :
		// return MediaRuleFormatter.getInstance();
		// case IJSONNode.PRIMITIVEVALUE_NODE :
		// IJSONPrimitiveValue value = (IJSONPrimitiveValue) node;
		// if (value.getPrimitiveType() ==
		// org.w3c.dom.json.JSONPrimitiveValue.JSON_COUNTER)
		// return CounterFormatter.getInstance();
		// else if (value.getPrimitiveType() ==
		// org.w3c.dom.json.JSONPrimitiveValue.JSON_RECT)
		// return RectFormatter.getInstance();
		// else if (value.getPrimitiveType() ==
		// org.w3c.dom.json.JSONPrimitiveValue.JSON_RGBCOLOR)
		// return RGBFormatter.getInstance();
		// else
		// return PrimitiveValueFormatter.getInstance();
		// case IJSONNode.PAGERULE_NODE :
		// return PageRuleFormatter.getInstance();
		// case IJSONNode.STYLEDECLARATION_NODE :
		// return StyleDeclarationFormatter.getInstance();
		// case IJSONNode.STYLEDECLITEM_NODE :
		// return StyleDeclItemFormatter.getInstance();
		// case IJSONNode.STYLERULE_NODE :
		// return StyleRuleFormatter.getInstance();
		// case IJSONNode.STYLESHEET_NODE :
		// return StyleSheetFormatter.getInstance();
		// case IJSONNode.ATTR_NODE :
		// return AttrFormatter.getInstance();
		case IJSONNode.DOCUMENT_NODE:
			return JSONDocumentFormatter.getInstance();
		case IJSONNode.OBJECT_NODE:
			return JSONObjectFormatter.getInstance();
		default:
			return UnknownRuleFormatter.getInstance();
		}
	}

	public synchronized static JSONSourceFormatterFactory getInstance() {
		if (fInstance == null) {
			fInstance = new JSONSourceFormatterFactory();
		}
		return fInstance;
	}

	private static JSONSourceFormatterFactory fInstance;
}

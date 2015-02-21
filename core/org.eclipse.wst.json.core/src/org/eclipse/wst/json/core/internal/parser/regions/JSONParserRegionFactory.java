package org.eclipse.wst.json.core.internal.parser.regions;

import org.eclipse.wst.sse.core.internal.parser.ContextRegion;
import org.eclipse.wst.sse.core.internal.parser.ForeignRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionContainer;

public class JSONParserRegionFactory {

	public JSONParserRegionFactory() {
		super();
	}

	public ITextRegion createToken(ITextRegionContainer parent, String context, int start, int textLength, int length) {
		return this.createToken(parent, context, start, textLength, length, null, null);
	}

	public ITextRegion createToken(ITextRegionContainer parent, String context, int start, int textLength, int length, String lang, String surroundingTag) {
		ITextRegion newRegion = createToken(context, start, textLength, length);
		// DW, 4/16/2003 token regions no longer have parents
		// newRegion.setParent(parent);
		return newRegion;
	}

	public ITextRegion createToken(String context, int start, int textLength, int length) {
		return this.createToken(context, start, textLength, length, null, null);
	}

	public ITextRegion createToken(String context, int start, int textLength, int length, String lang, String surroundingTag) {
		ITextRegion newRegion = null;
		/*if (context == DOMRegionContext.JSON_CDATA_TEXT) {
			newRegion = new JSONCDataTextRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.JSON_CONTENT) {
			newRegion = new JSONContentRegion(start, length);
		}
		else if (context == DOMRegionContext.JSON_TAG_NAME) {
			newRegion = new TagNameRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.JSON_TAG_ATTRIBUTE_NAME) {
			newRegion = new AttributeNameRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.JSON_TAG_ATTRIBUTE_EQUALS) {
			newRegion = new AttributeEqualsRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.JSON_TAG_ATTRIBUTE_VALUE) {
			newRegion = new AttributeValueRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.JSON_TAG_OPEN) {
			newRegion = new TagOpenRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.JSON_END_TAG_OPEN) {
			newRegion = new EndTagOpenRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.JSON_TAG_CLOSE) {
			newRegion = new TagCloseRegion(start);
		}
		else if (context == DOMRegionContext.JSON_EMPTY_TAG_CLOSE) {
			newRegion = new EmptyTagCloseRegion(start, textLength, length);
		}
		else if (context == DOMRegionContext.WHITE_SPACE) {
			newRegion = new WhiteSpaceOnlyRegion(start, length);
		}
		else
		// removed this condition during transition, and implemented in
		// subclass
		// if (context == JSONJSPRegionContexts.JSP_CONTENT) {
		// newRegion = new JSPCodeRegion(context, start, textLength, length);
		// } else
		if (context == DOMRegionContext.BLOCK_TEXT) {
			newRegion = new ForeignRegion(context, start, textLength, length);
			((ForeignRegion) newRegion).setSurroundingTag(surroundingTag);
		}
		else {
			newRegion = new ContextRegion(context, start, textLength, length);
		}*/
		newRegion = new ContextRegion(context, start, textLength, length);
		return newRegion;
	}
}

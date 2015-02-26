package org.eclipse.wst.json.ui.internal.projection;

import org.eclipse.jface.text.Position;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.ui.internal.projection.AbstractStructuredFoldingStrategy;


/**
 * A folding strategy for JSON type structured documents.
 * See AbstractStructuredFoldingStrategy for more details.
 */
public class JSONFoldingStrategy extends AbstractStructuredFoldingStrategy {

	/**
	 * Create an instance of the folding strategy.
	 * Be sure to set the viewer and document after creation.
	 */
	public JSONFoldingStrategy() {
		super();
	}

	/**
	 * @see org.eclipse.wst.sse.ui.internal.projection.AbstractFoldingStrategy#calcNewFoldPosition(org.eclipse.wst.sse.core.internal.provisional.IndexedRegion)
	 */
	protected Position calcNewFoldPosition(IndexedRegion indexedRegion) {
		Position retPos = null;
		
		//only want to fold regions of the valid type and with a valid range
		if(indexedRegion.getStartOffset() >= 0 && indexedRegion.getLength() >= 0) {
			IJSONNode node = (IJSONNode)indexedRegion;
			IStructuredDocumentRegion startRegion = node.getStartStructuredDocumentRegion();
			IStructuredDocumentRegion endRegion = node.getEndStructuredDocumentRegion();
			
			//if the node has an endRegion (end tag) then folding region is
			//	between the start and end tag
			//else if the region is a comment
			//else if the region is only an open tag or an open/close tag then don't fold it
			if(startRegion != null && endRegion != null) {
				if (endRegion.getEndOffset() >= startRegion.getStartOffset())
					retPos = new JSONObjectFoldingPosition(startRegion, endRegion);
			} 
//			else if(startRegion != null && indexedRegion instanceof CommentImpl) {
//				retPos = new JSONCommentFoldingPosition(startRegion);
//			}
		}
		
		return retPos;
	}

	@Override
	protected boolean indexedRegionValidType(IndexedRegion indexedRegion) {
		return (indexedRegion instanceof IJSONObject);
	}
}

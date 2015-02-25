package org.eclipse.wst.json.ui.internal.projection;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.ui.internal.projection.AbstractStructuredFoldingPosition;

/**
 * Folds an JSON object
 */
public class JSONObjectFoldingPosition extends
		AbstractStructuredFoldingPosition {

	/**
	 * The region representing the start of the folding region
	 */
	private IStructuredDocumentRegion fStartRegion;

	/**
	 * The region representing the end of the folding region, or
	 * <code>null</code> if the entire folding region is represented by
	 * <code>fStartRegion</code>
	 */
	private IStructuredDocumentRegion fEndRegion;

	/**
	 * <p>
	 * Used to represent a folding position that covers a single
	 * {@link IStructuredDocumentRegion}.
	 * </p>
	 * 
	 * @param region
	 *            the region that covers the entire position of the folding
	 *            region
	 */
	public JSONObjectFoldingPosition(IStructuredDocumentRegion region) {
		super(region.getStartOffset(), region.getEndOffset()
				- region.getStartOffset());
		this.fStartRegion = region;
		this.fEndRegion = null;
	}

	/**
	 * <p>
	 * Used to represent a folding position that covers more then one
	 * {@link IStructuredDocumentRegion}.
	 * </p>
	 * 
	 * @param startRegion
	 *            the first region covered by this position
	 * @param endRegion
	 *            the last region covered by this position
	 */
	public JSONObjectFoldingPosition(IStructuredDocumentRegion startRegion,
			IStructuredDocumentRegion endRegion) {
		super(startRegion.getStartOffset(), endRegion.getEndOffset()
				- startRegion.getStartOffset());
		this.fStartRegion = startRegion;
		this.fEndRegion = endRegion;
	}

	@Override
	protected int getStartOffset() {
		return fStartRegion.getStartOffset();
	}

	@Override
	protected int getEndOffset() {
		int endOffset;
		if (fEndRegion != null) {
			endOffset = fEndRegion.getEndOffset();
		} else {
			endOffset = fStartRegion.getEndOffset();
		}
		return endOffset;
	}
}
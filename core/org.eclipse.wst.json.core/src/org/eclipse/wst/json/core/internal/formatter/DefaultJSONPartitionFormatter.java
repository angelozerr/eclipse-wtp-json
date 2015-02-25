package org.eclipse.wst.json.core.internal.formatter;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.internal.Logger;
import org.eclipse.wst.json.core.regions.JSONRegionContexts;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;

public class DefaultJSONPartitionFormatter {
	/**
	 * Just a small container class that holds a JSONNode & documentRegion that
	 * should represent each other.
	 */
	protected class JSONRegion {
		public IJSONNode jsonNode;
		public IStructuredDocumentRegion documentRegion;
	}

	static private final String PRESERVE = "preserve";//$NON-NLS-1$
	static private final String COLLAPSE = "collapse";//$NON-NLS-1$
	static private final String REPLACE = "replace";//$NON-NLS-1$
	static private final String PRESERVE_QUOTED = "\"preserve\"";//$NON-NLS-1$
	static private final String XML_SPACE = "JSON:space";//$NON-NLS-1$
	static private final String XSL_NAMESPACE = "http://www.w3.org/1999/XSL/Transform"; //$NON-NLS-1$
	static private final String XSL_ATTRIBUTE = "attribute"; //$NON-NLS-1$
	static private final String XSL_TEXT = "text"; //$NON-NLS-1$
	static private final String SPACE = " "; //$NON-NLS-1$
	static private final String EMPTY = ""; //$NON-NLS-1$
	static private final String PROPERTY_WHITESPACE_FACET = "org.eclipse.wst.xsd.cm.properties/whitespace"; //$NON-NLS-1$

	private JSONFormattingPreferences fPreferences = null;
	private IProgressMonitor fProgressMonitor;

	private int replaceSpaces(TextEdit textEdit, int spaceStartOffset,
			int availableLineWidth, String whitespaceRun) {
		StringBuffer buff = new StringBuffer(whitespaceRun);
		for (int i = 0; i < buff.length(); i++) {
			buff.setCharAt(i, ' '); //$NON-NLS-1$
		}
		String replacementString = buff.toString();
		if (!replacementString.equals(whitespaceRun)) {
			ReplaceEdit replaceEdit = new ReplaceEdit(spaceStartOffset,
					whitespaceRun.length(), replacementString);
			textEdit.addChild(replaceEdit);
		}
		return availableLineWidth;
	}

	private int collapseSpaces(TextEdit textEdit, int spaceStartOffset,
			int availableLineWidth, String whitespaceRun) {
		// prefer to use use existing whitespace
		int existingWhitespaceOffset = whitespaceRun.indexOf(' ');
		if (existingWhitespaceOffset > -1) {
			// delete whitespaces before and after existing whitespace
			if (existingWhitespaceOffset > 0) {
				DeleteEdit deleteEdit = new DeleteEdit(spaceStartOffset,
						existingWhitespaceOffset);
				textEdit.addChild(deleteEdit);
			}
			if (existingWhitespaceOffset < whitespaceRun.length() - 1) {
				int nextOffset = existingWhitespaceOffset + 1;
				DeleteEdit deleteEdit = new DeleteEdit(spaceStartOffset
						+ nextOffset, whitespaceRun.length() - nextOffset);
				textEdit.addChild(deleteEdit);
			}
		} else {
			// delete all whitespace and insert new one
			// collapse whitespace by deleting whitespace
			DeleteEdit deleteEdit = new DeleteEdit(spaceStartOffset,
					whitespaceRun.length());
			textEdit.addChild(deleteEdit);
			// then insert one space
			InsertEdit insertEdit = new InsertEdit(spaceStartOffset, SPACE);
			textEdit.addChild(insertEdit);
		}
		// remember to account for space added
		--availableLineWidth;
		return availableLineWidth;
	}

	private int collapseAndIndent(TextEdit textEdit, int spaceStartOffset,
			int availableLineWidth, int indentLevel, String whitespaceRun,
			IStructuredDocumentRegion currentRegion) {
		// Need to keep blank lines, but still collapse the whitespace
		String lineDelimiters = null;
		if (!getFormattingPreferences().getClearAllBlankLines()) {
			lineDelimiters = extractLineDelimiters(whitespaceRun, currentRegion);
			String formattedLine = lineDelimiters
					+ getIndentString(indentLevel);
			if (lineDelimiters.length() > 0
					&& !formattedLine.equals(whitespaceRun)) {
				textEdit.addChild(new ReplaceEdit(spaceStartOffset,
						whitespaceRun.length(), formattedLine));
				availableLineWidth = getFormattingPreferences()
						.getMaxLineWidth() - indentLevel;
			}
		}
		if (lineDelimiters == null || lineDelimiters.length() == 0) {
			availableLineWidth = collapseSpaces(textEdit, spaceStartOffset,
					availableLineWidth, whitespaceRun);
		}
		return availableLineWidth;
	}

	private void deleteTrailingSpaces(TextEdit textEdit,
			ITextRegion currentTextRegion,
			IStructuredDocumentRegion currentDocumentRegion) {
		int textEnd = currentTextRegion.getTextEnd();
		int textEndOffset = currentDocumentRegion.getStartOffset() + textEnd;
		int difference = currentTextRegion.getEnd() - textEnd;
		DeleteEdit deleteEdit = new DeleteEdit(textEndOffset, difference);
		textEdit.addChild(deleteEdit);
	}

	public TextEdit format(IDocument document, int start, int length) {
		return format(document, start, length, new JSONFormattingPreferences());
	}

	public TextEdit format(IDocument document, int start, int length,
			JSONFormattingPreferences preferences) {
		TextEdit edit = null;
		if (document instanceof IStructuredDocument) {
			IStructuredModel model = StructuredModelManager.getModelManager()
					.getModelForEdit((IStructuredDocument) document);
			if (model != null) {
				try {
					edit = format(model, start, length, preferences);
				} finally {
					model.releaseFromEdit();
				}
			}
		}
		return edit;
	}

	public TextEdit format(IStructuredModel model, int start, int length) {
		return format(model, start, length, new JSONFormattingPreferences());
	}

	public TextEdit format(IStructuredModel model, int start, int length,
			JSONFormattingPreferences preferences) {
		setFormattingPreferences(preferences);

		TextEdit edit = new MultiTextEdit();
		IStructuredDocument document = model.getStructuredDocument();
		// get initial document region
		IStructuredDocumentRegion currentRegion = document
				.getRegionAtCharacterOffset(start);
		if (currentRegion != null) {
			int startOffset = currentRegion.getStartOffset();

			// get initial json node
			IndexedRegion currentIndexedRegion = model
					.getIndexedRegion(startOffset);
			if (currentIndexedRegion instanceof IJSONNode) {
				// set up jsonRegion which will contain current region to be
				// formatted
				IJSONNode currentJSONNode = (IJSONNode) currentIndexedRegion;
				JSONRegion jsonRegion = new JSONRegion();
				jsonRegion.documentRegion = currentRegion;
				jsonRegion.jsonNode = currentJSONNode;

				JSONFormattingConstraints parentConstraints = getRegionConstraints(currentJSONNode);

				/*
				 * if the whitespace strategy is declared as default, get it
				 * from the preferences
				 */
				if (JSONFormattingConstraints.DEFAULT.equals(parentConstraints
						.getWhitespaceStrategy()))
					parentConstraints.setWhitespaceStrategy(preferences
							.getElementWhitespaceStrategy());

				// TODO: initialize indentLevel
				// initialize available line width
				int lineWidth = getFormattingPreferences().getMaxLineWidth();
				try {
					IRegion lineInfo = document
							.getLineInformationOfOffset(startOffset);
					lineWidth = lineWidth
							- (startOffset - lineInfo.getOffset());
				} catch (BadLocationException e) {
					Logger.log(Logger.WARNING_DEBUG, e.getMessage(), e);
				}
				parentConstraints.setAvailableLineWidth(lineWidth);

				// format all siblings (and their children) as long they
				// overlap with start/length
				Position formatRange = new Position(start, length);
				formatSiblings(edit, jsonRegion, parentConstraints, formatRange);
			}
		}
		return edit;
	}

	/**
	 * Determines the formatting constraints for a specified node based on its
	 * ancestors' formatting. In particular, if any ancestor node either
	 * explicitly defines whitespace preservation or ignorance, that whitespace
	 * strategy should be used for <code>currentNode</code> and all of its
	 * descendants.
	 * 
	 * @param currentNode
	 *            the node to investigate the ancestry of to determine
	 *            formatting constraints
	 * 
	 * @return formatting constraints defined by an ancestor
	 */
	private JSONFormattingConstraints getRegionConstraints(IJSONNode currentNode) {
		IJSONNode iterator = currentNode;
		JSONFormattingConstraints result = new JSONFormattingConstraints();
		JSONRegion region = new JSONRegion();
		JSONFormattingConstraints parentConstraints = new JSONFormattingConstraints();
		boolean parent = true;

		/*
		 * Iterate through the ancestry to find if any explicit whitespace
		 * strategy has been defined
		 */
		while (iterator != null
				&& iterator.getNodeType() != IJSONNode.DOCUMENT_NODE) {
			iterator = (IJSONNode) iterator.getParentNode();
			region.jsonNode = iterator;
			region.documentRegion = iterator.getFirstStructuredDocumentRegion();

			updateFormattingConstraints(null, null, result, region);

			/*
			 * If this is the parent of the current node, keep the constraints
			 * in case no other constraints are identified
			 */
			if (parent) {
				parentConstraints.copyConstraints(result);
				parent = false;
			}

			/* A parent who has defined a specific whitespace strategy was found */
			if (JSONFormattingConstraints.PRESERVE.equals(result
					.getWhitespaceStrategy())
					|| JSONFormattingConstraints.DEFAULT.equals(result
							.getWhitespaceStrategy()))
				return result;
		}

		return parentConstraints;
	}

	/**
	 * Formats the given JSON content region
	 * 
	 * @param textEdit
	 * @param formatRange
	 * @param parentConstraints
	 * @param currentJSONRegion
	 * @param previousRegion
	 */
	/*
	 * private void formatContent(TextEdit textEdit, Position formatRange,
	 * JSONFormattingConstraints parentConstraints, JSONRegion
	 * currentJSONRegion, IStructuredDocumentRegion previousRegion) {
	 * IStructuredDocumentRegion currentRegion =
	 * currentJSONRegion.documentRegion; String fullText =
	 * currentJSONRegion.jsonNode.getSource();
	 * 
	 * // check if in preserve space mode, if so, don't touch anything but //
	 * make sure to update available line width String whitespaceMode =
	 * parentConstraints.getWhitespaceStrategy(); if
	 * (JSONFormattingConstraints.PRESERVE.equals(whitespaceMode)) { int
	 * availableLineWidth = parentConstraints.getAvailableLineWidth();
	 * availableLineWidth = updateLineWidthWithLastLine(fullText,
	 * availableLineWidth);
	 * 
	 * // update available line width in constraints
	 * parentConstraints.setAvailableLineWidth(availableLineWidth); // A text
	 * node can contain multiple structured document regions - sync the
	 * documentRegion // with the last region of the node since the text from
	 * all regions was formatted currentJSONRegion.documentRegion =
	 * currentJSONRegion.jsonNode.getLastStructuredDocumentRegion(); return; }
	 * 
	 * // if content is just whitespace and there's something after it // just
	 * skip over this region because region will take care of it boolean
	 * isAllWhitespace = ((IJSONText)
	 * currentJSONRegion.jsonNode).isElementContentWhitespace();
	 * IStructuredDocumentRegion nextDocumentRegion = null; if (isAllWhitespace)
	 * {
	 * parentConstraints.setAvailableLineWidth(fPreferences.getMaxLineWidth());
	 * nextDocumentRegion = currentRegion.getNext(); if (nextDocumentRegion !=
	 * null) return; }
	 * 
	 * // special handling if text follows an entity or cdata region if
	 * (!JSONFormattingConstraints.COLLAPSE.equals(whitespaceMode) &&
	 * previousRegion != null) { String previouRegionType =
	 * previousRegion.getType(); if
	 * (JSONRegionContexts.XML_ENTITY_REFERENCE.equals(previouRegionType) ||
	 * JSONRegionContexts.XML_CDATA_TEXT.equals(previouRegionType))
	 * whitespaceMode = JSONFormattingConstraints.COLLAPSE; } // also, special
	 * handling if text is before an entity or cdata region if
	 * (!JSONFormattingConstraints.COLLAPSE.equals(whitespaceMode)) { // get
	 * next document region if dont already have it if (nextDocumentRegion ==
	 * null) nextDocumentRegion = currentRegion.getNext(); if
	 * (nextDocumentRegion != null) { String nextRegionType =
	 * nextDocumentRegion.getType(); if
	 * (JSONRegionContexts.XML_ENTITY_REFERENCE.equals(nextRegionType) ||
	 * JSONRegionContexts.XML_CDATA_TEXT.equals(nextRegionType)) whitespaceMode
	 * = JSONFormattingConstraints.COLLAPSE; } } final IStructuredDocumentRegion
	 * lastRegion =
	 * currentJSONRegion.jsonNode.getLastStructuredDocumentRegion();
	 * formatTextInContent(textEdit, parentConstraints, currentRegion,
	 * lastRegion != null ? lastRegion.getNext(): null, fullText,
	 * whitespaceMode); // A text node can contain multiple structured document
	 * regions - sync the documentRegion // with the last region of the node
	 * since the text from all regions was formatted
	 * currentJSONRegion.documentRegion = lastRegion; }
	 */

	private void formatEmptyStartTagWithNoAttr(TextEdit textEdit,
			JSONFormattingConstraints constraints,
			IStructuredDocumentRegion currentDocumentRegion,
			IStructuredDocumentRegion previousDocumentRegion,
			int availableLineWidth, String indentStrategy,
			String whitespaceStrategy, ITextRegion currentTextRegion) {
		// get preference if there should be a space or not between tag
		// name and empty tag close
		// <tagName />
		boolean oneSpaceInTagName = getFormattingPreferences()
				.getSpaceBeforeEmptyCloseTag();

		// calculate available line width
		int tagNameLineWidth = currentTextRegion.getTextLength() + 3;
		if (oneSpaceInTagName) {
			// add one more to account for space before empty tag close
			++tagNameLineWidth;
		}
		availableLineWidth -= tagNameLineWidth;

		if (JSONFormattingConstraints.INLINE.equals(indentStrategy)) {
			// if was inlining, need to check if out of available line
			// width
			if (availableLineWidth < 0) {
				// need to indent if possible
				int lineWidth = indentIfPossible(textEdit, constraints,
						currentDocumentRegion, previousDocumentRegion,
						whitespaceStrategy, indentStrategy, true);
				// update available line width
				if (lineWidth > 0)
					availableLineWidth = lineWidth - tagNameLineWidth;
				else
					availableLineWidth -= tagNameLineWidth;
			} else {
				// no need to indent
				// just make sure to delete previous whitespace if
				// needed
				// if
				// ((JSONRegionContexts.XML_CONTENT.equals(previousDocumentRegion.getType()))
				// && (previousDocumentRegion.getFullText().trim().length() ==
				// 0)) {
				// availableLineWidth = collapseSpaces(textEdit,
				// previousDocumentRegion.getStartOffset(), availableLineWidth,
				// previousDocumentRegion.getFullText());
				// }
			}
		}

		// delete any trail spaces after tag name
		int textLength = currentTextRegion.getTextLength();
		int regionLength = currentTextRegion.getLength();

		boolean thereAreSpaces = textLength < regionLength;
		if (!oneSpaceInTagName && thereAreSpaces) {
			deleteTrailingSpaces(textEdit, currentTextRegion,
					currentDocumentRegion);
		} else if (oneSpaceInTagName) {
			insertSpaceAndCollapse(textEdit, currentDocumentRegion,
					availableLineWidth, currentTextRegion);
		}
		constraints.setAvailableLineWidth(availableLineWidth);
	}

	/**
	 * Formats an end tag
	 * 
	 * @param textEdit
	 * @param currentRegion
	 * @param textRegions
	 */
	private void formatEndTag(TextEdit textEdit, Position formatRange,
			JSONFormattingConstraints constraints,
			JSONRegion currentJSONRegion,
			IStructuredDocumentRegion previousDocumentRegion) {
		IStructuredDocumentRegion currentDocumentRegion = currentJSONRegion.documentRegion;

		String whitespaceStrategy = constraints.getWhitespaceStrategy();
		String indentStrategy = constraints.getIndentStrategy();

		// do not format space before start tag if preserving spaces
		if (whitespaceStrategy != JSONFormattingConstraints.PRESERVE) {
			// format like indent strategy says
			if (JSONFormattingConstraints.INDENT.equals(indentStrategy)
					|| JSONFormattingConstraints.NEW_LINE
							.equals(indentStrategy)) {
				int availableLineWidth = indentIfPossible(textEdit,
						constraints, currentDocumentRegion,
						previousDocumentRegion, whitespaceStrategy,
						indentStrategy, false);
				constraints.setAvailableLineWidth(availableLineWidth);
			} else if (JSONFormattingConstraints.INLINE.equals(indentStrategy)) {
				IStructuredDocument doc = currentDocumentRegion
						.getParentDocument();
				int currentLine = doc.getLineOfOffset(currentDocumentRegion
						.getStartOffset());
				int prevLine = doc.getLineOfOffset(previousDocumentRegion
						.getStartOffset());
				if (currentLine != prevLine) {
					int availableLineWidth = indentIfPossible(textEdit,
							constraints, currentDocumentRegion,
							previousDocumentRegion, whitespaceStrategy,
							indentStrategy, false);
					constraints.setAvailableLineWidth(availableLineWidth);
				}
			}
		}
		// format the end tag itself
		// formatWithinEndTag(textEdit, constraints, currentDocumentRegion,
		// previousDocumentRegion);
	}

	/**
	 * Formats the given region (and all its children) contained in jsonRegion.
	 * 
	 * @param edit
	 *            edits required to format
	 * @param formatRange
	 *            document range to format (only format content within this
	 *            range)
	 * @param parentConstraints
	 * @param jsonRegion
	 *            assumes json node & region are not null
	 * @param previousRegion
	 *            could be null
	 * @return Returns the last region formatted
	 */
	private JSONRegion formatRegion(TextEdit edit, Position formatRange,
			JSONFormattingConstraints parentConstraints, JSONRegion jsonRegion,
			IStructuredDocumentRegion previousRegion) {
		IStructuredDocumentRegion currentRegion = jsonRegion.documentRegion;
		String regionType = currentRegion.getType();
		// if (JSONRegionContexts.XML_TAG_NAME.equals(regionType) ||
		// "JSP_ROOT_TAG_NAME".equals(regionType) ||
		// "JSP_DIRECTIVE_NAME".equals(regionType)) {
		ITextRegion textRegion = currentRegion.getFirstRegion();
		String textRegionType = textRegion.getType();
		if (JSONRegionContexts.JSON_OBJECT_OPEN.equals(textRegionType)
				|| JSONRegionContexts.JSON_ARRAY_OPEN.equals(textRegionType)) {
			jsonRegion = formatStartTag(edit, formatRange, parentConstraints,
					jsonRegion, previousRegion);
		} else if (JSONRegionContexts.JSON_OBJECT_CLOSE.equals(textRegionType)
				|| JSONRegionContexts.JSON_ARRAY_CLOSE.equals(textRegionType)) {
			formatEndTag(edit, formatRange, parentConstraints, jsonRegion,
					previousRegion);
		}
		// }
		// else if (JSONRegionContexts.XML_CONTENT.equals(regionType) ||
		// jsonRegion.jsonNode.getNodeType() == Node.TEXT_NODE) {
		// formatContent(edit, formatRange, parentConstraints, jsonRegion,
		// previousRegion);
		// }
		// else if (JSONRegionContexts.XML_COMMENT_TEXT.equals(regionType)) {
		// formatComment(edit, formatRange, parentConstraints, jsonRegion,
		// previousRegion);
		// }
		/*
		 * else { // unknown, so just leave alone for now but make sure to
		 * update // available line width String fullText =
		 * currentRegion.getFullText(); int width =
		 * updateLineWidthWithLastLine(fullText,
		 * parentConstraints.getAvailableLineWidth());
		 * parentConstraints.setAvailableLineWidth(width); }
		 */
		return jsonRegion;
	}

	/**
	 * Formats the jsonRegion and all of its children and siblings
	 * 
	 * @param edit
	 * @param jsonRegion
	 * @param parentConstraints
	 * @param formatRange
	 */
	private void formatSiblings(TextEdit edit, JSONRegion jsonRegion,
			JSONFormattingConstraints parentConstraints, Position formatRange) {
		IStructuredDocumentRegion previousRegion = null;
		IStructuredDocumentRegion currentRegion = jsonRegion.documentRegion;
		IJSONNode currentJSONNode = jsonRegion.jsonNode;
		while (currentJSONNode != null
				&& currentRegion != null
				&& formatRange.overlapsWith(currentRegion.getStartOffset(),
						currentRegion.getLength())
				&& (fProgressMonitor == null || !fProgressMonitor.isCanceled())) {
			jsonRegion.documentRegion = currentRegion;
			jsonRegion.jsonNode = currentJSONNode;

			// need to make sure current document region and current
			// json node match up
			if (currentJSONNode.getFirstStructuredDocumentRegion().equals(
					currentRegion)) {
				// format this document region/node, formatRegion will
				// return the last node/region formatted
				jsonRegion = formatRegion(edit, formatRange, parentConstraints,
						jsonRegion, previousRegion);
			} else {
				// TODO: need to figure out what to do if they don't
				// match up
			}
			previousRegion = jsonRegion.documentRegion;
			// get the next sibling information
			if (jsonRegion.jsonNode != null)
				currentJSONNode = (IJSONNode) jsonRegion.jsonNode
						.getNextSibling();
			else
				currentJSONNode = null;
			currentRegion = previousRegion.getNext();
		}
	}

	/**
	 * Formats a start tag
	 * 
	 * @param textEdit
	 * @param currentRegion
	 * @param textRegions
	 */
	private JSONRegion formatStartTag(TextEdit textEdit, Position formatRange,
			JSONFormattingConstraints parentConstraints,
			JSONRegion currentJSONRegion,
			IStructuredDocumentRegion previousDocumentRegion) {
		// determine proper indent by referring to parent constraints,
		// previous node, and current node
		IStructuredDocumentRegion currentDocumentRegion = currentJSONRegion.documentRegion;
		IJSONNode currentJSONNode = currentJSONRegion.jsonNode;

		// create a constraint for this tag
		JSONFormattingConstraints thisConstraints = new JSONFormattingConstraints();
		JSONFormattingConstraints childrenConstraints = new JSONFormattingConstraints();
		updateFormattingConstraints(parentConstraints, thisConstraints,
				childrenConstraints, currentJSONRegion);

		if (JSONFormattingConstraints.DEFAULT.equals(childrenConstraints
				.getWhitespaceStrategy()))
			childrenConstraints
					.setWhitespaceStrategy((new JSONFormattingPreferences())
							.getElementWhitespaceStrategy());

		String whitespaceStrategy = thisConstraints.getWhitespaceStrategy();
		String indentStrategy = thisConstraints.getIndentStrategy();
		int availableLineWidth = thisConstraints.getAvailableLineWidth();

		// format space before start tag
		// do not format space before start tag if preserving spaces
		if (!JSONFormattingConstraints.PRESERVE.equals(whitespaceStrategy)) {
			// format like indent strategy says
			if (JSONFormattingConstraints.INDENT.equals(indentStrategy)
					|| JSONFormattingConstraints.NEW_LINE
							.equals(indentStrategy)) {
				availableLineWidth = indentIfPossible(textEdit,
						thisConstraints, currentDocumentRegion,
						previousDocumentRegion, whitespaceStrategy,
						indentStrategy, true, true);
				if (availableLineWidth > 0)
					thisConstraints.setAvailableLineWidth(availableLineWidth);
			}
		}
		// format the start tag itself
		boolean tagEnded = formatWithinTag(textEdit, thisConstraints,
				currentDocumentRegion, previousDocumentRegion);

		// format children
		if (!tagEnded) {
			// update childConstraints with thisConstraint's indentLevel &
			// availableLineWidth
			childrenConstraints
					.setIndentLevel(thisConstraints.getIndentLevel());
			childrenConstraints.setAvailableLineWidth(thisConstraints
					.getAvailableLineWidth());

			previousDocumentRegion = currentDocumentRegion;
			IJSONNode childJSONNode = (IJSONNode) currentJSONNode
					.getFirstChild();
			IStructuredDocumentRegion nextRegion = currentDocumentRegion
					.getNext();
			boolean passedFormatRange = false;
			// as long as there is one child
			if (childJSONNode != null && nextRegion != null) {
				while (childJSONNode != null
						&& nextRegion != null
						&& !passedFormatRange
						&& (fProgressMonitor == null || !fProgressMonitor
								.isCanceled())) {
					JSONRegion childJSONRegion = new JSONRegion();
					childJSONRegion.documentRegion = nextRegion;
					childJSONRegion.jsonNode = childJSONNode;
					if (nextRegion.equals(childJSONNode
							.getFirstStructuredDocumentRegion())) {
						// format children. pass in child constraints
						childJSONRegion = formatRegion(textEdit, formatRange,
								childrenConstraints, childJSONRegion,
								previousDocumentRegion);
					} else {
						// TODO: what happens if they dont match up?
					}

					// update childJSONRegion with next json/region node
					if (childJSONRegion.jsonNode != null) {
						childJSONNode = (IJSONNode) childJSONRegion.jsonNode
								.getNextSibling();
					} else {
						childJSONNode = null;
					}
					previousDocumentRegion = childJSONRegion.documentRegion;
					nextRegion = previousDocumentRegion.getNext();
					if (nextRegion != null)
						passedFormatRange = !formatRange.overlapsWith(
								nextRegion.getStartOffset(),
								nextRegion.getLength());
				}
			} else {
				// there were no children, so keep end tag inlined
				childrenConstraints
						.setWhitespaceStrategy(JSONFormattingConstraints.COLLAPSE);
				childrenConstraints
						.setIndentStrategy(JSONFormattingConstraints.INLINE);
			}

			if (!passedFormatRange) {
				// update the json region with the last formatted region/json
				// node should be end tag and this tag's JSONNode
				currentJSONRegion.documentRegion = nextRegion;
				currentJSONRegion.jsonNode = currentJSONNode;

				// end tag's indent level should be same as start tag's
				childrenConstraints.setIndentLevel(thisConstraints
						.getIndentLevel());
				// format end tag
				boolean formatEndTag = false;
				if (nextRegion != null && currentJSONNode != null) {
					ITextRegionList rs = nextRegion.getRegions();
					if (rs.size() > 1) {
						ITextRegion r = rs.get(0);
						// if (r != null &&
						// JSONRegionContexts.XML_END_TAG_OPEN.equals(r.getType()))
						// {
						// r = rs.get(1);
						// if (r != null &&
						// JSONRegionContexts.XML_TAG_NAME.equals(r.getType()))
						// {
						// String tagName = nextRegion.getText(r);
						// if (tagName != null &&
						// tagName.equals(currentJSONNode.getNodeName()))
						// formatEndTag = true;
						// }
						// }

					}
				}
				if (formatEndTag)
					formatEndTag(textEdit, formatRange, childrenConstraints,
							currentJSONRegion, previousDocumentRegion);
				else {
					// missing end tag so return last formatted document
					// region
					currentJSONRegion.documentRegion = previousDocumentRegion;
				}
			} else {
				// passed format range before could finish, so update json
				// region to last known formatted region
				currentJSONRegion.documentRegion = nextRegion;
				currentJSONRegion.jsonNode = childJSONNode;
			}

			// update parent constraint since this is what is passed back
			parentConstraints.setAvailableLineWidth(childrenConstraints
					.getAvailableLineWidth());
		} else {
			// update available line width
			parentConstraints.setAvailableLineWidth(thisConstraints
					.getAvailableLineWidth());
		}
		return currentJSONRegion;
	}

	private void formatStartTagWithNoAttr(TextEdit textEdit,
			JSONFormattingConstraints constraints,
			IStructuredDocumentRegion currentDocumentRegion,
			IStructuredDocumentRegion previousDocumentRegion,
			int availableLineWidth, String indentStrategy,
			String whitespaceStrategy, ITextRegion currentTextRegion) {
		// calculate available line width
		int tagNameLineWidth = currentTextRegion.getTextLength() + 2;
		availableLineWidth -= tagNameLineWidth;

		if (JSONFormattingConstraints.INLINE.equals(indentStrategy)) {
			// if was inlining, need to check if out of available line
			// width
			if (availableLineWidth < 0) {
				// need to indent if possible
				int lineWidth = indentIfPossible(textEdit, constraints,
						currentDocumentRegion, previousDocumentRegion,
						whitespaceStrategy, indentStrategy, true);
				// update available line width
				if (lineWidth > 0)
					availableLineWidth = lineWidth - tagNameLineWidth;
				else
					availableLineWidth -= tagNameLineWidth;
			} else {
				// no need to indent
				// just make sure to delete previous whitespace if
				// needed
				if (previousDocumentRegion != null) {
					// if
					// (JSONRegionContexts.XML_CONTENT.equals(previousDocumentRegion.getType()))
					// {
					// String previousDocumentRegionText =
					// previousDocumentRegion.getFullText();
					// if (previousDocumentRegionText.trim().length() == 0) {
					// availableLineWidth = collapseSpaces(textEdit,
					// previousDocumentRegion.getStartOffset(),
					// availableLineWidth, previousDocumentRegionText);
					// }
					// }
				}
			}
		}

		// delete any trail spaces after tag name
		if (currentTextRegion.getTextLength() < currentTextRegion.getLength()) {
			deleteTrailingSpaces(textEdit, currentTextRegion,
					currentDocumentRegion);
		}
		constraints.setAvailableLineWidth(availableLineWidth);
	}

	/**
	 * Format the text in JSON content
	 * 
	 * @param textEdit
	 * @param parentConstraints
	 * @param currentRegion
	 * @param fullText
	 * @param whitespaceMode
	 */
	private void formatTextInContent(TextEdit textEdit,
			JSONFormattingConstraints parentConstraints,
			IStructuredDocumentRegion currentRegion,
			IStructuredDocumentRegion nextRegion, String fullText,
			String whitespaceMode) {
		int availableLineWidth = parentConstraints.getAvailableLineWidth();

		// determine indentation
		boolean forceInitialIndent = false;
		int indentLevel = parentConstraints.getIndentLevel() + 1;
		String indentMode = parentConstraints.getIndentStrategy();
		if (JSONFormattingConstraints.INDENT.equals(indentMode)) {
			forceInitialIndent = true;
		}
		if (JSONFormattingConstraints.NEW_LINE.equals(indentMode)) {
			indentLevel = parentConstraints.getIndentLevel();
			forceInitialIndent = true;
		}

		int fullTextOffset = 0;
		char[] fullTextArray = fullText.toCharArray();
		while (fullTextOffset < fullTextArray.length) {
			// gather all whitespaces
			String whitespaceRun = getCharacterRun(fullTextArray,
					fullTextOffset, true);
			if (whitespaceRun.length() > 0) {
				// offset where whitespace starts
				int whitespaceStart = fullTextOffset;
				// update current offset in fullText
				fullTextOffset += whitespaceRun.length();

				// gather following word
				String characterRun = getCharacterRun(fullTextArray,
						fullTextOffset, false);
				int characterRunLength = characterRun.length();
				if (characterRunLength > 0) {
					// indent if word is too long or forcing initial
					// indent
					availableLineWidth -= characterRunLength;
					// offset where indent/collapse will happen
					int startOffset = currentRegion.getStartOffset()
							+ whitespaceStart;
					if (forceInitialIndent || (availableLineWidth <= 0)) {
						// indent if not already indented
						availableLineWidth = indentIfNotAlreadyIndented(
								textEdit, currentRegion, indentLevel,
								startOffset, whitespaceRun);
						// remember to subtract word length
						availableLineWidth -= characterRunLength;
						forceInitialIndent = false; // initial indent done
					} else {
						// just collapse spaces, but adjust for any indenting
						// that may result from preserving line delimiters
						if (whitespaceStart == 0
								&& JSONFormattingConstraints.IGNOREANDTRIM
										.equals(whitespaceMode)) {
							// if ignore, trim
							DeleteEdit deleteTrailing = new DeleteEdit(
									startOffset, whitespaceRun.length());
							textEdit.addChild(deleteTrailing);
						} else if (JSONFormattingConstraints.REPLACE
								.equals(whitespaceMode))
							availableLineWidth = replaceSpaces(textEdit,
									startOffset, availableLineWidth,
									whitespaceRun);
						else
							availableLineWidth = collapseAndIndent(textEdit,
									startOffset, availableLineWidth,
									indentLevel, whitespaceRun, currentRegion);
					}

					fullTextOffset += characterRunLength;
				} else {
					// handle trailing whitespace
					int whitespaceOffset = currentRegion.getStartOffset()
							+ whitespaceStart;
					if (JSONFormattingConstraints.REPLACE
							.equals(whitespaceMode))
						availableLineWidth = replaceSpaces(textEdit,
								whitespaceOffset, availableLineWidth,
								whitespaceRun);
					else if (JSONFormattingConstraints.IGNOREANDTRIM
							.equals(whitespaceMode)) {
						// always trim
						DeleteEdit deleteTrailing = new DeleteEdit(
								whitespaceOffset, whitespaceRun.length());
						textEdit.addChild(deleteTrailing);
					} else if (getFormattingPreferences()
							.getClearAllBlankLines()) {
						/*
						 * if (!nextRegionHandlesTrailingWhitespace(nextRegion))
						 * { if
						 * (JSONFormattingConstraints.IGNORE.equals(whitespaceMode
						 * )) { // if ignore, trim DeleteEdit deleteTrailing =
						 * new DeleteEdit(whitespaceOffset,
						 * whitespaceRun.length());
						 * textEdit.addChild(deleteTrailing); } else { // if
						 * collapse, leave a space. but what if end up //
						 * wanting to add indent? then need to delete space //
						 * added and add indent instead availableLineWidth =
						 * collapseSpaces(textEdit, whitespaceOffset,
						 * availableLineWidth, whitespaceRun); } }
						 */
					}
				}
			} else {
				// gather word
				String characterRun = getCharacterRun(fullTextArray,
						fullTextOffset, false);
				int characterRunLength = characterRun.length();
				if (characterRunLength > 0) {
					// indent if word is too long or forcing initial
					// indent
					// [243091] - characterRunLength should only be subtracted
					// once or text formatting wraps prematurely
					// availableLineWidth = availableLineWidth -
					// characterRunLength;
					if ((JSONFormattingConstraints.IGNORE
							.equals(whitespaceMode) || JSONFormattingConstraints.IGNOREANDTRIM
							.equals(whitespaceMode))
							&& (forceInitialIndent || (availableLineWidth <= 0))) {
						// indent if not already indented
						availableLineWidth = indentIfNotAlreadyIndented(
								textEdit, currentRegion, indentLevel,
								currentRegion.getStartOffset(), whitespaceRun);
						// remember to subtract word length
						availableLineWidth -= characterRunLength;
						forceInitialIndent = false; // initial indent done
					} else {
						// just collapse spaces
						availableLineWidth -= characterRunLength;
					}

					fullTextOffset += characterRunLength;
				}
			}
		}
		// update available line width
		parentConstraints.setAvailableLineWidth(availableLineWidth);
	}

	/*
	 * private boolean
	 * nextRegionHandlesTrailingWhitespace(IStructuredDocumentRegion region) {
	 * if (region == null) return false; final String type = region.getType();
	 * if (type.equals(JSONRegionContexts.XML_TAG_NAME)) { return
	 * JSONRegionContexts
	 * .XML_TAG_OPEN.equals(region.getFirstRegion().getType()); } return
	 * JSONRegionContexts.XML_COMMENT_TEXT.equals(type); }
	 */

	/*
	 * private void formatWithinEndTag(TextEdit textEdit,
	 * JSONFormattingConstraints constraints, IStructuredDocumentRegion
	 * currentDocumentRegion, IStructuredDocumentRegion previousDocumentRegion)
	 * { String indentStrategy = constraints.getIndentStrategy(); String
	 * whitespaceStrategy = constraints.getWhitespaceStrategy(); int
	 * availableLineWidth = constraints.getAvailableLineWidth(); ITextRegionList
	 * textRegions = currentDocumentRegion.getRegions(); int
	 * currentNumberOfRegions = currentDocumentRegion.getNumberOfRegions(); int
	 * currentTextRegionIndex = 1;
	 * 
	 * ITextRegion currentTextRegion = textRegions.get(currentTextRegionIndex);
	 * String currentType = currentTextRegion.getType(); // tag name should
	 * always be the first text region if
	 * (JSONRegionContexts.XML_TAG_NAME.equals(currentType) &&
	 * currentTextRegionIndex < currentNumberOfRegions - 1) { ITextRegion
	 * nextTextRegion = textRegions.get(currentTextRegionIndex + 1); // Bug
	 * 221279 - Some non well-formed documents will not contribute a next region
	 * if (nextTextRegion != null &&
	 * JSONRegionContexts.XML_TAG_CLOSE.equals(nextTextRegion.getType())) { //
	 * calculate available line width int tagNameLineWidth =
	 * currentTextRegion.getTextLength() + 3; availableLineWidth -=
	 * tagNameLineWidth;
	 * 
	 * if (JSONFormattingConstraints.INLINE.equals(indentStrategy)) { // if was
	 * inlining, need to check if out of available line // width - Whitespace
	 * may have been corrected in the text content if (availableLineWidth < 0 &&
	 * JSONFormattingConstraints.IGNORE.equals(whitespaceStrategy)) { // need to
	 * deindent if possible int lineWidth = indentIfPossible(textEdit,
	 * constraints, currentDocumentRegion, previousDocumentRegion,
	 * whitespaceStrategy, indentStrategy, false); // update available line
	 * width if (lineWidth > 0) availableLineWidth = lineWidth -
	 * tagNameLineWidth; } else { // no need to indent // just make sure to
	 * delete previous whitespace if // needed if (previousDocumentRegion !=
	 * null) { if
	 * (JSONRegionContexts.XML_CONTENT.equals(previousDocumentRegion.getType()))
	 * { String previousDocumentRegionText =
	 * previousDocumentRegion.getFullText(); if
	 * (previousDocumentRegionText.trim().length() == 0) { availableLineWidth =
	 * collapseSpaces(textEdit, previousDocumentRegion.getStartOffset(),
	 * availableLineWidth, previousDocumentRegionText); } } } } } // delete any
	 * trail spaces after tag name if (currentTextRegion.getTextLength() <
	 * currentTextRegion.getLength()) { deleteTrailingSpaces(textEdit,
	 * currentTextRegion, currentDocumentRegion); } } } else { // end tag has
	 * unexpected stuff, so just leave it alone }
	 * constraints.setAvailableLineWidth(availableLineWidth); }
	 */

	/**
	 * Formats the contents within a tag like tag name and attributes
	 * 
	 * @param textEdit
	 * @param currentDocumentRegion
	 * @param textRegions
	 *            contains at least 3 regions
	 * @return true if tag was ended, false otherwise
	 */
	private boolean formatWithinTag(TextEdit textEdit,
			JSONFormattingConstraints constraints,
			IStructuredDocumentRegion currentDocumentRegion,
			IStructuredDocumentRegion previousDocumentRegion) {
		int availableLineWidth = constraints.getAvailableLineWidth();
		String indentStrategy = constraints.getIndentStrategy();
		String whitespaceStrategy = constraints.getWhitespaceStrategy();
		int indentLevel = constraints.getIndentLevel();
		ITextRegionList textRegions = currentDocumentRegion.getRegions();
		int currentTextRegionIndex = 1;

		ITextRegion currentTextRegion = textRegions.get(currentTextRegionIndex);
		String currentType = currentTextRegion.getType();
		// tag name should always be the first text region
		if (JSONRegionContexts.JSON_OBJECT_OPEN.equals(currentType)
				|| JSONRegionContexts.JSON_ARRAY_OPEN.equals(currentType)) {
			ITextRegion nextTextRegion = textRegions
					.get(currentTextRegionIndex + 1);
			String nextType = (nextTextRegion != null) ? nextTextRegion
					.getType() : null;
			if (JSONRegionContexts.JSON_OBJECT_CLOSE.equals(currentType)
					|| JSONRegionContexts.JSON_ARRAY_CLOSE.equals(currentType)) {
				// already at tag close
				formatStartTagWithNoAttr(textEdit, constraints,
						currentDocumentRegion, previousDocumentRegion,
						availableLineWidth, indentStrategy, whitespaceStrategy,
						currentTextRegion);
				return false;
			}
			/*
			 * } else if
			 * (JSONRegionContexts.XML_EMPTY_TAG_CLOSE.equals(nextType)) { //
			 * already at empty tag close
			 * formatEmptyStartTagWithNoAttr(textEdit, constraints,
			 * currentDocumentRegion, previousDocumentRegion,
			 * availableLineWidth, indentStrategy, whitespaceStrategy,
			 * currentTextRegion); return true; }
			 */
			else {
				availableLineWidth -= (currentTextRegion.getTextLength() + 2);
				boolean alignFinalBracket = getFormattingPreferences()
						.getAlignFinalBracket();
				boolean oneSpaceInTagName = getFormattingPreferences()
						.getSpaceBeforeEmptyCloseTag();
				boolean indentMultipleAttribute = getFormattingPreferences()
						.getIndentMultipleAttributes();
				// indicates if tag spanned more than one line
				boolean spanMoreThan1Line = false;
				// indicates if all attributes should be indented
				boolean indentAllAttributes = false;
				if (indentMultipleAttribute) {
					int attributesCount = 0;
					int i = 2;
					final int size = textRegions.size();
					while (i < size && attributesCount < 2) {
						if (JSONRegionContexts.JSON_OBJECT_KEY
								.equals(textRegions.get(i).getType())) {
							++attributesCount;
						}
						i++;
					}
					indentAllAttributes = (attributesCount > 1);
				}

				while ((currentTextRegionIndex + 1) < textRegions.size()) {
					nextTextRegion = textRegions
							.get(currentTextRegionIndex + 1);
					nextType = nextTextRegion.getType();
					if (JSONRegionContexts.JSON_OBJECT_KEY.equals(nextType)) {
						boolean indentAttribute = indentAllAttributes;
						if (!indentAttribute)
							indentAttribute = shouldIndentBeforeAttribute(
									constraints, textRegions,
									availableLineWidth, currentTextRegionIndex,
									currentTextRegion, nextTextRegion);
						if (indentAttribute) {
							availableLineWidth = indentIfNotAlreadyIndented(
									textEdit, indentLevel + 1,
									currentDocumentRegion, currentTextRegion);
							spanMoreThan1Line = true;
						} else {
							// otherwise, insertSpaceAndCollapse
							insertSpaceAndCollapse(textEdit,
									currentDocumentRegion, availableLineWidth,
									currentTextRegion);
							// update available line width
							availableLineWidth -= (currentTextRegion
									.getTextLength() + 1);
						}
					} else if (JSONRegionContexts.JSON_OBJECT_CLOSE
							.equals(nextType)
							|| JSONRegionContexts.JSON_ARRAY_CLOSE
									.equals(nextType)) {
						// if need to align bracket on next line, indent
						if (alignFinalBracket && spanMoreThan1Line) {
							availableLineWidth = indentIfNotAlreadyIndented(
									textEdit, indentLevel,
									currentDocumentRegion, currentTextRegion);
							--availableLineWidth; // for tag close itself
						} else {
							// otherwise, just delete space before tag close
							if (currentTextRegion.getTextLength() < currentTextRegion
									.getLength()) {
								deleteTrailingSpaces(textEdit,
										currentTextRegion,
										currentDocumentRegion);
								availableLineWidth -= (currentTextRegion
										.getTextLength() + 1);
							}
						}
						// update line width
						constraints.setAvailableLineWidth(availableLineWidth);
						return false;
					}
					/*
					 * else if (JSONRegionContexts.XML_EMPTY_TAG_CLOSE
					 * .equals(nextType)) { int textLength =
					 * currentTextRegion.getTextLength(); int regionLength =
					 * currentTextRegion.getLength();
					 * 
					 * boolean thereAreSpaces = textLength < regionLength; if
					 * (!oneSpaceInTagName && thereAreSpaces) { // delete any
					 * trail spaces after tag name
					 * deleteTrailingSpaces(textEdit, currentTextRegion,
					 * currentDocumentRegion); availableLineWidth -=
					 * (currentTextRegion .getTextLength() + 2); } // insert a
					 * space and collapse ONLY IF it's specified else if
					 * (oneSpaceInTagName) { insertSpaceAndCollapse(textEdit,
					 * currentDocumentRegion, availableLineWidth,
					 * currentTextRegion); availableLineWidth -=
					 * (currentTextRegion .getTextLength() + 3); } // update
					 * line width
					 * constraints.setAvailableLineWidth(availableLineWidth);
					 * return true; }
					 */
					else {
						if (JSONRegionContexts.JSON_OBJECT_KEY
								.equals(currentType)
								&& JSONRegionContexts.JSON_COLON
										.equals(nextType)) {
							if (currentTextRegion.getTextLength() < currentTextRegion
									.getLength()) {
								deleteTrailingSpaces(textEdit,
										currentTextRegion,
										currentDocumentRegion);
							}
							// update available width
							availableLineWidth -= currentTextRegion
									.getTextLength();
						} else if (JSONRegionContexts.JSON_COLON
								.equals(currentType)
								&& (JSONRegionContexts.JSON_VALUE_BOOLEAN
										.equals(nextType)
										|| JSONRegionContexts.JSON_VALUE_NULL
												.equals(nextType) || JSONRegionContexts.JSON_VALUE_NUMBER
											.equals(nextType))
								|| JSONRegionContexts.JSON_VALUE_STRING
										.equals(nextType)) {
							if (currentTextRegion.getTextLength() < currentTextRegion
									.getLength()) {
								deleteTrailingSpaces(textEdit,
										currentTextRegion,
										currentDocumentRegion);
							}
							// update available width
							availableLineWidth -= currentTextRegion
									.getTextLength();
						} else {
							// otherwise, insertSpaceAndCollapse
							insertSpaceAndCollapse(textEdit,
									currentDocumentRegion, availableLineWidth,
									currentTextRegion);
							// update available line width
							availableLineWidth -= (currentTextRegion
									.getTextLength() + 1);
						}
					}
					currentTextRegion = nextTextRegion;
					currentType = nextType;
					++currentTextRegionIndex;
				}
			}
		}
		// update line width
		constraints.setAvailableLineWidth(availableLineWidth);
		return false;
	}

	/**
	 * Format an XML comment structured document region.
	 */
	/*
	 * private void formatComment(TextEdit textEdit, Position formatRange,
	 * JSONFormattingConstraints parentConstraints, JSONRegion
	 * currentJSONRegion, IStructuredDocumentRegion previousRegion) {
	 * IStructuredDocumentRegion currentRegion =
	 * currentJSONRegion.documentRegion; int lineWidth =
	 * parentConstraints.getAvailableLineWidth() -
	 * currentRegion.getFullText().length(); // Don't format if we're not
	 * exceeding the available line width, or if the whitespace // strategy is
	 * to preserve whitespace - But update line width. if(currentRegion == null
	 * || JSONFormattingConstraints.PRESERVE.equals(parentConstraints.
	 * getWhitespaceStrategy()) || !fPreferences.getFormatCommentText()) {
	 * parentConstraints.setAvailableLineWidth(lineWidth); return; }
	 * 
	 * Iterator it = currentRegion.getRegions().iterator(); ITextRegion previous
	 * = null; if (previousRegion == null) previousRegion =
	 * currentRegion.getPrevious(); // Iterate over each text region of the
	 * comment Node parent = currentJSONRegion.jsonNode.getParentNode();
	 * while(it.hasNext()) { ITextRegion text = (ITextRegion) it.next(); String
	 * type = text.getType(); if (type == JSONRegionContexts.XML_COMMENT_OPEN) {
	 * int indentLevel = (parent != null && parent.getNodeType() ==
	 * Node.DOCUMENT_NODE) ? 0 : 1; int width = formatCommentStart(textEdit,
	 * parentConstraints, indentLevel, currentRegion, previousRegion, text);
	 * parentConstraints.setAvailableLineWidth(width); } else if (type ==
	 * JSONRegionContexts.XML_COMMENT_TEXT) { int indentLevel = (parent != null
	 * && parent.getNodeType() == Node.DOCUMENT_NODE) ? -1 :
	 * parentConstraints.getIndentLevel(); formatCommentContent(textEdit,
	 * parentConstraints, indentLevel, currentRegion, previous, text); }
	 * previous = text; } }
	 * 
	 * private void formatCommentContent(TextEdit textEdit,
	 * JSONFormattingConstraints parentConstraints, int indentLevel,
	 * IStructuredDocumentRegion currentRegion, ITextRegion previous,
	 * ITextRegion region) { int lineWidth =
	 * parentConstraints.getAvailableLineWidth() -
	 * currentRegion.getFullText(previous).length(); // If there's more text
	 * than line width available, format String text =
	 * currentRegion.getFullText(region); compressContent(textEdit,
	 * currentRegion, currentRegion.getStartOffset(region), indentLevel + 1,
	 * lineWidth, text); }
	 * 
	 * private void compressContent(TextEdit textEdit, IStructuredDocumentRegion
	 * region, int startOffset, int indentLevel, int lineWidth, String text) {
	 * int length = text.length(); int start = 0, end = 0; char c = 0; int
	 * resultLength = 0; boolean joinLines = fPreferences.getJoinCommentLines();
	 * boolean onOwnLine = false;
	 * 
	 * String indent = getIndentString(indentLevel + 1);
	 * 
	 * for (int i = 0; i < length; i++) { c = text.charAt(i); // Compress
	 * whitespace unless its a line delimiter and formatting does not permit
	 * joining lines if (Character.isWhitespace(c)) { if ((c != '\r' && c!=
	 * '\n') || joinLines) { // Just came off of a word if (start == end) {
	 * start = end = i; } end++; resultLength++; } else { // correct the indent
	 * of this line lineWidth = fPreferences.getMaxLineWidth(); resultLength =
	 * 0; onOwnLine = true;
	 * 
	 * // Compress any whitespace before the line delimiter if (start != end) {
	 * int replaceLength = end - start; textEdit.addChild(new ReplaceEdit(start
	 * + startOffset, replaceLength, EMPTY)); start = end = i; } } } else { //
	 * Transitioned to a new word if (start != end) { int replaceLength = end -
	 * start; if (onOwnLine) { // If content is on its own line, replace leading
	 * whitespace with proper indent textEdit.addChild(new ReplaceEdit(start +
	 * startOffset, replaceLength, indent)); resultLength -= (replaceLength -
	 * indent.length()); onOwnLine = false; } else if (!(replaceLength == 1 &&
	 * text.charAt(start) == ' ')) { textEdit.addChild(new ReplaceEdit(start +
	 * startOffset, replaceLength, SPACE)); resultLength -= (replaceLength - 1);
	 * } start = end = i; // Make sure the word starts on a new line if
	 * (resultLength > lineWidth) { lineWidth = fPreferences.getMaxLineWidth();
	 * resultLength = 0; textEdit.addChild(new InsertEdit(start + startOffset,
	 * getLineDelimiter(region) + indent)); } } // Word is immediately after
	 * line delimiters, indent appropriately if (onOwnLine) {
	 * textEdit.addChild(new InsertEdit(i + startOffset, indent)); onOwnLine =
	 * false; } resultLength++; } }
	 * 
	 * // Clean up any dangling whitespace int replaceLength = end - start;
	 * indent = getIndentString(indentLevel); if (replaceLength == 0) { // No
	 * trailing whitespace textEdit.addChild(new InsertEdit(length +
	 * startOffset, (onOwnLine) ? indent : SPACE)); } else { String whitespace =
	 * text.substring(start); String replacement = (onOwnLine) ? indent : SPACE;
	 * if (!whitespace.equals(replacement)) { textEdit.addChild(new
	 * ReplaceEdit(start + startOffset, replaceLength, replacement)); } } }
	 * 
	 * private int formatCommentStart(TextEdit textEdit,
	 * JSONFormattingConstraints parentConstraints, int indentLevel,
	 * IStructuredDocumentRegion currentRegion, IStructuredDocumentRegion
	 * previousRegion, ITextRegion region) { int lineWidth =
	 * parentConstraints.getAvailableLineWidth(); if
	 * (previousRegion!=null&&(JSONRegionContexts
	 * .XML_CONTENT.equals(previousRegion.getType()))) { String previousText =
	 * previousRegion.getFullText(); String trailingWhitespace =
	 * getTrailingWhitespace(previousText); String delimiters =
	 * extractLineDelimiters(trailingWhitespace, previousRegion); if (delimiters
	 * != null && delimiters.length() > 0){// && previousText.length() ==
	 * trailingWhitespace.length()) { // Format the comment if it's on a new
	 * line int offset = previousRegion.getEnd() - trailingWhitespace.length();
	 * lineWidth = indentIfNotAlreadyIndented(textEdit, currentRegion,
	 * parentConstraints.getIndentLevel() + indentLevel, offset,
	 * trailingWhitespace); } } return lineWidth; }
	 */

	/**
	 * Returns either a String of whitespace or characters depending on
	 * forWhitespace
	 * 
	 * @param fullTextArray
	 *            the text array to look in
	 * @param textOffset
	 *            the start offset to start searching
	 * @param forWhitespace
	 *            true if should return whitespaces, false otherwise
	 * @return a String of either all whitespace or all characters. Never
	 *         returns null
	 */
	private String getCharacterRun(char[] fullTextArray, int textOffset,
			boolean forWhitespace) {
		StringBuffer characterRun = new StringBuffer();
		boolean nonCharacterFound = false;
		while (textOffset < fullTextArray.length && !nonCharacterFound) {
			char c = fullTextArray[textOffset];
			boolean isWhitespace = Character.isWhitespace(c);
			if ((forWhitespace && isWhitespace)
					|| (!forWhitespace && !isWhitespace))
				characterRun.append(c);
			else
				nonCharacterFound = true;
			++textOffset;
		}
		return characterRun.toString();
	}

	private String getTrailingWhitespace(String text) {
		StringBuffer whitespaceRun = new StringBuffer();
		int index = text.length() - 1;
		while (index >= 0) {
			char c = text.charAt(index--);
			if (Character.isWhitespace(c))
				whitespaceRun.insert(0, c);
			else
				break;
		}
		return whitespaceRun.toString();
	}

	private String getIndentString(int indentLevel) {
		StringBuffer indentString = new StringBuffer();
		String indent = getFormattingPreferences().getOneIndent();
		for (int i = 0; i < indentLevel; ++i) {
			indentString.append(indent);
		}
		return indentString.toString();
	}

	protected JSONFormattingPreferences getFormattingPreferences() {
		if (fPreferences == null)
			fPreferences = new JSONFormattingPreferences();
		return fPreferences;
	}

	protected void setFormattingPreferences(
			JSONFormattingPreferences preferences) {
		fPreferences = preferences;
	}

	/**
	 * Indent if whitespaceRun does not already contain an indent
	 * 
	 * @param textEdit
	 * @param indentLevel
	 * @param indentStartOffset
	 * @param maxAvailableLineWidth
	 * @param whitespaceRun
	 * @return new available line width up to where indented
	 */
	private int indentIfNotAlreadyIndented(TextEdit textEdit,
			IStructuredDocumentRegion currentRegion, int indentLevel,
			int indentStartOffset, String whitespaceRun) {
		int maxAvailableLineWidth = getFormattingPreferences()
				.getMaxLineWidth();

		int availableLineWidth;
		String indentString = getIndentString(indentLevel);
		String lineDelimiter = getLineDelimiter(currentRegion);
		String newLineAndIndent = lineDelimiter + indentString;

		TextEdit indentation = null;

		// if not already correctly indented
		if (!newLineAndIndent.equals(whitespaceRun)) {
			if (getFormattingPreferences().getClearAllBlankLines()) {
				if (whitespaceRun != null) {
					// replace existing whitespace run
					indentation = new ReplaceEdit(indentStartOffset,
							whitespaceRun.length(), newLineAndIndent);
				} else {
					// just insert correct indent
					indentation = new InsertEdit(indentStartOffset,
							newLineAndIndent);
				}
			}
			// Keep the empty lines
			else {
				// just insert correct indent
				if (whitespaceRun == null)
					indentation = new InsertEdit(indentStartOffset,
							newLineAndIndent);
				// Need to preserve the number of empty lines, but still indent
				// on the current line properly
				else {
					String existingDelimiters = extractLineDelimiters(
							whitespaceRun, currentRegion);
					if (existingDelimiters != null
							&& existingDelimiters.length() > 0) {
						String formatted = existingDelimiters + indentString;
						// Don't perform a replace if the formatted string is
						// the same as the existing whitespaceRun
						if (!formatted.equals(whitespaceRun))
							indentation = new ReplaceEdit(indentStartOffset,
									whitespaceRun.length(), formatted);
					}
					// No blank lines to preserve - correct the indent
					else
						indentation = new ReplaceEdit(indentStartOffset,
								whitespaceRun.length(), newLineAndIndent);
				}
			}
		}

		if (indentation != null)
			textEdit.addChild(indentation);
		// update line width
		availableLineWidth = maxAvailableLineWidth - indentString.length();
		return availableLineWidth;
	}

	private int indentIfNotAlreadyIndented(TextEdit textEdit, int indentLevel,
			IStructuredDocumentRegion currentDocumentRegion,
			ITextRegion currentTextRegion) {
		// indent if not already indented
		int textLength = currentTextRegion.getTextLength();
		int regionLength = currentTextRegion.getLength();
		int indentStartOffset = currentDocumentRegion
				.getTextEndOffset(currentTextRegion);
		String fullText = currentDocumentRegion.getFullText(currentTextRegion);
		String whitespaceRun = fullText.substring(textLength, regionLength);

		// update line width
		int availableLineWidth = indentIfNotAlreadyIndented(textEdit,
				currentDocumentRegion, indentLevel, indentStartOffset,
				whitespaceRun);
		return availableLineWidth;
	}

	private int indentIfPossible(TextEdit textEdit,
			JSONFormattingConstraints thisConstraints,
			IStructuredDocumentRegion currentDocumentRegion,
			IStructuredDocumentRegion previousDocumentRegion,
			String whitespaceStrategy, String indentStrategy, boolean addIndent) {
		return indentIfPossible(textEdit, thisConstraints,
				currentDocumentRegion, previousDocumentRegion,
				whitespaceStrategy, indentStrategy, addIndent,
				!getFormattingPreferences().getClearAllBlankLines());
	}

	private int indentIfPossible(TextEdit textEdit,
			JSONFormattingConstraints thisConstraints,
			IStructuredDocumentRegion currentDocumentRegion,
			IStructuredDocumentRegion previousDocumentRegion,
			String whitespaceStrategy, String indentStrategy,
			boolean addIndent, boolean handlePreviousWhitespace) {
		int availableLineWidth = -1;
		// if there is no previous document region, there is no need to indent
		// because we're at beginning of document
		if (previousDocumentRegion == null)
			return availableLineWidth;

		// only indent if ignoring whitespace or if collapsing and
		// there was a whitespace character before this region
		boolean canIndent = false;
		String previousRegionFullText = null;
		String previousRegionType = null;

		if ((JSONFormattingConstraints.IGNORE.equals(whitespaceStrategy))
				|| JSONFormattingConstraints.IGNOREANDTRIM
						.equals(whitespaceStrategy)) {
			// if ignoring, need to check if previous region was cdata
			previousRegionType = previousDocumentRegion.getType();
			// if (JSONRegionContexts.XML_CDATA_TEXT.equals(previousRegionType))
			// canIndent = false;
			// else
			canIndent = true;
		} else if (JSONFormattingConstraints.COLLAPSE
				.equals(whitespaceStrategy)) {
			// if collapsing, need to check if previous region ended in a
			// whitespace
			previousRegionType = previousDocumentRegion.getType();
			// if (JSONRegionContexts.XML_CONTENT.equals(previousRegionType)) {
			// previousRegionFullText = previousDocumentRegion.getFullText();
			// int length = previousRegionFullText.length();
			// if (length > 1)
			// canIndent =
			// Character.isWhitespace(previousRegionFullText.charAt(length -
			// 1));
			// }
		}
		if (canIndent) {
			int indentStartOffset = currentDocumentRegion.getStartOffset();
			String whitespaceRun = null;

			// get previous region type if it was not previously retrieved
			if (previousRegionType == null)
				previousRegionType = previousDocumentRegion.getType();

			// get previous region's text if it was not previously retrieved
			// if (previousRegionFullText == null &&
			// JSONRegionContexts.XML_CONTENT.equals(previousRegionType))
			// previousRegionFullText = previousDocumentRegion.getFullText();

			// if previous region was only whitespace, this may
			// already be indented, so need to make sure
			if ((previousRegionFullText != null)
					&& (previousRegionFullText.trim().length() == 0)) {
				indentStartOffset = previousDocumentRegion.getStartOffset();
				whitespaceRun = previousRegionFullText;
			}
			if ((previousRegionFullText != null) && (whitespaceRun == null)
					&& handlePreviousWhitespace) {
				whitespaceRun = getTrailingWhitespace(previousRegionFullText);
				indentStartOffset = previousDocumentRegion.getEndOffset()
						- whitespaceRun.length();
			}

			int indentLevel = thisConstraints.getIndentLevel();
			if (addIndent
					&& JSONFormattingConstraints.INDENT.equals(indentStrategy)) {
				++indentLevel;
				thisConstraints.setIndentLevel(indentLevel);
			}

			// indent if not already indented
			availableLineWidth = indentIfNotAlreadyIndented(textEdit,
					currentDocumentRegion, indentLevel, indentStartOffset,
					whitespaceRun);
		}
		return availableLineWidth;
	}

	/**
	 * Allow exactly one whitespace in currentTextRegion. If there are more,
	 * collapse to one. If there are none, insert one.
	 * 
	 * @param textEdit
	 * @param currentDocumentRegion
	 * @param availableLineWidth
	 * @param currentTextRegion
	 */
	private void insertSpaceAndCollapse(TextEdit textEdit,
			IStructuredDocumentRegion currentDocumentRegion,
			int availableLineWidth, ITextRegion currentTextRegion) {
		int textLength = currentTextRegion.getTextLength();
		int regionLength = currentTextRegion.getLength();
		boolean thereAreSpaces = textLength < regionLength;
		int spacesStartOffset = currentDocumentRegion
				.getStartOffset(currentTextRegion) + textLength;

		if (thereAreSpaces) {
			String fullTagName = currentDocumentRegion
					.getFullText(currentTextRegion);
			String whitespaceRun = fullTagName.substring(textLength,
					regionLength);
			collapseSpaces(textEdit, spacesStartOffset, availableLineWidth,
					whitespaceRun);
		} else {
			// insert a space
			InsertEdit insertEdit = new InsertEdit(spacesStartOffset, SPACE);
			textEdit.addChild(insertEdit);
		}
	}

	private boolean shouldIndentBeforeAttribute(
			JSONFormattingConstraints constraints, ITextRegionList textRegions,
			int availableLineWidth, int currentTextRegionIndex,
			ITextRegion currentTextRegion, ITextRegion nextTextRegion) {
		boolean indentAttribute = false;

		// look ahead to see if going to hit max line width
		// something attrName
		int currentWidth = currentTextRegion.getTextLength()
				+ nextTextRegion.getTextLength() + 1;
		if (currentWidth > availableLineWidth)
			indentAttribute = true;
		else {
			if ((currentTextRegionIndex + 2) < textRegions.size()) {
				// still okay, so try next region
				// something attrName=
				ITextRegion textRegion = textRegions
						.get(currentTextRegionIndex + 2);
				if (JSONRegionContexts.JSON_COLON.equals(textRegion.getType())) {
					++currentWidth;
					if (currentWidth > availableLineWidth)
						indentAttribute = true;
					else {
						if ((currentTextRegionIndex + 3) < textRegions.size()) {
							// still okay, so try next region
							// something attrName=attrValue
							textRegion = textRegions
									.get(currentTextRegionIndex + 3);
							if (JSONRegionContexts.JSON_VALUE_BOOLEAN
									.equals(textRegion.getType())
									|| JSONRegionContexts.JSON_VALUE_NULL
											.equals(textRegion.getType())
									|| JSONRegionContexts.JSON_VALUE_NUMBER
											.equals(textRegion.getType())) {
								currentWidth = +textRegion.getTextLength();
								if (currentWidth > availableLineWidth)
									indentAttribute = true;
							}
						}
					}
				}
			}
		}
		return indentAttribute;
	}

	/**
	 * Given the provided information (parentConstraints & currentJSONRegion),
	 * update the formatting constraints (for this & child)
	 * 
	 * @param parentConstraints
	 *            can be null
	 * @param thisConstraints
	 *            can be null
	 * @param childConstraints
	 *            can be null
	 * @param currentJSONRegion
	 *            cannot be null
	 */
	protected void updateFormattingConstraints(
			JSONFormattingConstraints parentConstraints,
			JSONFormattingConstraints thisConstraints,
			JSONFormattingConstraints childConstraints,
			JSONRegion currentJSONRegion) {
		IStructuredDocumentRegion currentRegion = currentJSONRegion.documentRegion;
		IJSONNode currentNode = currentJSONRegion.jsonNode;

		// default to whatever parent's constraint said to do
		if (parentConstraints != null) {
			if (thisConstraints != null) {
				thisConstraints.copyConstraints(parentConstraints);
			}
			if (childConstraints != null) {
				childConstraints.copyConstraints(parentConstraints);
				// if whitespace strategy was only a hint, null it out so
				// defaults are taken instead
				if (parentConstraints.isWhitespaceStrategyAHint())
					childConstraints.setWhitespaceStrategy(null);
			}
		}

		// set up constraints for direct children of document root
		IJSONNode parentNode = currentNode.getParentNode();
		if (parentNode != null
				&& parentNode.getNodeType() == IJSONNode.DOCUMENT_NODE) {
			if (thisConstraints != null) {
				thisConstraints
						.setWhitespaceStrategy(JSONFormattingConstraints.IGNORE);
				thisConstraints
						.setIndentStrategy(JSONFormattingConstraints.NEW_LINE);
				thisConstraints.setIndentLevel(0);
			}
			if (childConstraints != null) {
				childConstraints.setWhitespaceStrategy(null);
				childConstraints.setIndentStrategy(null);
				childConstraints.setIndentLevel(0);
			}
		}

		// other conditions to check when setting up child constraints
		if (childConstraints != null) {
			JSONFormattingPreferences preferences = getFormattingPreferences();

			// if we're at document root, child tags should always just start
			// on a new line and have an indent level of 0
			if (currentNode.getNodeType() == IJSONNode.DOCUMENT_NODE) {
				childConstraints
						.setWhitespaceStrategy(JSONFormattingConstraints.IGNORE);
				childConstraints
						.setIndentStrategy(JSONFormattingConstraints.NEW_LINE);
				childConstraints.setIndentLevel(0);
			} else {
				// BUG108074 & BUG84688 - preserve whitespace in xsl:text &
				// xsl:attribute
				// String nodeNamespaceURI = currentNode.getNamespaceURI();
				// if (XSL_NAMESPACE.equals(nodeNamespaceURI)) {
				// String nodeName = ((Element) currentNode).getLocalName();
				// if (XSL_ATTRIBUTE.equals(nodeName) ||
				// XSL_TEXT.equals(nodeName)) {
				// childConstraints.setWhitespaceStrategy(JSONFormattingConstraints.PRESERVE);
				// }
				// }
				// else {
				// search within current tag for JSON:space attribute
				ITextRegionList textRegions = currentRegion.getRegions();
				int i = 0;
				boolean JSONSpaceFound = false;
				boolean preserveFound = false;
				while (i < textRegions.size() && !JSONSpaceFound) {
					ITextRegion textRegion = textRegions.get(i);
					if (JSONRegionContexts.JSON_OBJECT_KEY.equals(textRegion
							.getType())) {
						String regionText = currentRegion.getText(textRegion);
						if (XML_SPACE.equals(regionText)) {
							if ((i + 1) < textRegions.size()) {
								++i;
								textRegion = textRegions.get(i);
								if (JSONRegionContexts.JSON_COLON
										.equals(textRegion.getType())
										&& ((i + 1) < textRegions.size())) {
									++i;
									textRegion = textRegions.get(i);
									regionText = currentRegion
											.getText(textRegion);
									if (PRESERVE.equals(regionText)
											|| PRESERVE_QUOTED
													.equals(regionText)) {
										preserveFound = true;
									}
								}
							}
							JSONSpaceFound = true;
						}
					}
					++i;
				}
				if (JSONSpaceFound) {
					if (preserveFound) {
						// preserve was found so set the strategy
						childConstraints
								.setWhitespaceStrategy(JSONFormattingConstraints.PRESERVE);
					} else {
						// JSON:space was found but it was not collapse, so
						// use default whitespace strategy
						childConstraints
								.setWhitespaceStrategy(JSONFormattingConstraints.DEFAULT);
					}
				} else {
					// how to hande nodes that have nonwhitespace text
					// content
					// NodeList nodeList = currentNode.getChildNodes();
					// int length = nodeList.getLength();
					// int index = 0;
					// boolean textNodeFound = false;
					// // BUG214516 - If the parent constraint is to preserve
					// // whitespace, child constraints should
					// // still reflect the parent constraints
					// while (index < length
					// && !textNodeFound
					// && parentConstraints != null
					// && !JSONFormattingConstraints.PRESERVE
					// .equals(parentConstraints
					// .getWhitespaceStrategy())) {
					// IJSONNode childNode = nodeList.item(index);
					// // if (childNode.getNodeType() == Node.TEXT_NODE) {
					// // textNodeFound = !((IJSONText)
					// // childNode).isElementContentWhitespace();
					// // }
					// ++index;
					// }
					// if (textNodeFound) {
					// if (length > 1) {
					// // more in here than just text, so consider
					// // this mixed content
					// childConstraints.setWhitespaceStrategy(preferences
					// .getMixedWhitespaceStrategy());
					// childConstraints.setIndentStrategy(preferences
					// .getMixedIndentStrategy());
					// } else {
					// // there's only text
					// childConstraints.setWhitespaceStrategy(preferences
					// .getTextWhitespaceStrategy());
					// childConstraints.setIndentStrategy(preferences
					// .getTextIndentStrategy());
					// }
					// childConstraints.setIsWhitespaceStrategyAHint(true);
					// childConstraints.setIsIndentStrategyAHint(true);
					// }

					// try referring to content model for information on
					// whitespace & indent strategy
					// ModelQueryAdapter adapter = (ModelQueryAdapter)
					// ((IJSONDocument)
					// currentNode.getOwnerDocument()).getAdapterFor(ModelQueryAdapter.class);
					// CMElementDeclaration elementDeclaration =
					// (CMElementDeclaration)
					// adapter.getModelQuery().getCMNode(currentNode);
					// if (elementDeclaration != null) {
					// // follow whitespace strategy preference for
					// // pcdata content
					// int contentType = elementDeclaration.getContentType();
					//
					// String facetValue = null;
					// if(elementDeclaration.getDataType() != null)
					// facetValue = (String)
					// elementDeclaration.getDataType().getProperty(PROPERTY_WHITESPACE_FACET);
					// if(facetValue != null) {
					// if(PRESERVE.equals(facetValue))
					// childConstraints.setWhitespaceStrategy(JSONFormattingConstraints.PRESERVE);
					// // For XSD types, "collapse" corresponds to the
					// IGNOREANDTRIM strategy
					// else if(COLLAPSE.equals(facetValue))
					// childConstraints.setWhitespaceStrategy(JSONFormattingConstraints.IGNOREANDTRIM);
					// else if(REPLACE.equals(facetValue))
					// childConstraints.setWhitespaceStrategy(JSONFormattingConstraints.REPLACE);
					// }
					// else if (contentType == CMElementDeclaration.PCDATA &&
					// parentConstraints != null &&
					// !JSONFormattingConstraints.PRESERVE.equals(parentConstraints.getWhitespaceStrategy()))
					// {
					// childConstraints.setWhitespaceStrategy(preferences.getPCDataWhitespaceStrategy());
					// }
					// else if (contentType == CMElementDeclaration.ELEMENT &&
					// parentConstraints != null &&
					// !JSONFormattingConstraints.PRESERVE.equals(parentConstraints.getWhitespaceStrategy()))
					// {
					// childConstraints.setWhitespaceStrategy(JSONFormattingConstraints.IGNORE);
					// childConstraints.setIndentStrategy(JSONFormattingConstraints.INDENT);
					// childConstraints.setIsWhitespaceStrategyAHint(true);
					// childConstraints.setIsIndentStrategyAHint(true);
					// }
					// else {
					// // look for JSON:space in content model
					// CMNamedNodeMap cmAttributes =
					// elementDeclaration.getAttributes();
					//
					// // Not needed - we're looking for JSON:space
					// //CMNamedNodeMapImpl allAttributes = new
					// CMNamedNodeMapImpl(cmAttributes);
					// //List nodes =
					// ModelQueryUtil.getModelQuery(currentNode.getOwnerDocument()).getAvailableContent((Element)
					// currentNode, elementDeclaration,
					// ModelQuery.INCLUDE_ATTRIBUTES);
					// //for (int k = 0; k < nodes.size(); k++) {
					// // CMNode cmnode = (CMNode) nodes.get(k);
					// // if (cmnode.getNodeType() ==
					// CMNode.ATTRIBUTE_DECLARATION) {
					// // allAttributes.put(cmnode);
					// // }
					// //}
					// //cmAttributes = allAttributes;
					//
					// // Check implied values from the DTD way.
					// CMAttributeDeclaration attributeDeclaration =
					// (CMAttributeDeclaration)
					// cmAttributes.getNamedItem(XML_SPACE);
					// if (attributeDeclaration != null) {
					// // CMAttributeDeclaration found, check
					// // it
					// // out.
					//
					// //BUG214516/196544 - Fixed NPE that was caused by an attr
					// having
					// // a null attr type
					// String defaultValue = null;
					// CMDataType attrType = attributeDeclaration.getAttrType();
					// if (attrType != null) {
					// if ((attrType.getImpliedValueKind() !=
					// CMDataType.IMPLIED_VALUE_NONE) &&
					// attrType.getImpliedValue() != null)
					// defaultValue = attrType.getImpliedValue();
					// else if ((attrType.getEnumeratedValues() != null) &&
					// (attrType.getEnumeratedValues().length > 0)) {
					// defaultValue = attrType.getEnumeratedValues()[0];
					// }
					// }
					//
					// // JSON:space="preserve" means preserve
					// // space,
					// // everything else means back to
					// // default.
					// if (PRESERVE.equals(defaultValue))
					// childConstraints.setWhitespaceStrategy(JSONFormattingConstraints.PRESERVE);
					// else
					// childConstraints.setWhitespaceStrategy(JSONFormattingConstraints.DEFAULT);
					// }
					// // If the node has no attributes, inherit the parents
					// whitespace strategy
					// else {
					// if (parentConstraints != null)
					// childConstraints.setWhitespaceStrategy(parentConstraints.getWhitespaceStrategy());
					// else
					// childConstraints.setWhitespaceStrategy(null);
					// }
					// }
					// }
				}
			}
			// }
			// set default values according to preferences
			if (childConstraints.getWhitespaceStrategy() == null) {
				childConstraints.setWhitespaceStrategy(preferences
						.getElementWhitespaceStrategy());
			}
			if (childConstraints.getIndentStrategy() == null) {
				childConstraints.setIndentStrategy(preferences
						.getElementIndentStrategy());
			}
		}
	}

	/**
	 * Calculates the current available line width given fullText.
	 * 
	 * @param fullText
	 * @param availableLineWidth
	 * @param maxAvailableLineWidth
	 * @return
	 */
	private int updateLineWidthWithLastLine(String fullText,
			int availableLineWidth) {
		int maxAvailableLineWidth = getFormattingPreferences()
				.getMaxLineWidth();
		int lineWidth = availableLineWidth;
		if (fullText != null) {
			int textLength = fullText.length();
			// update available line width
			// find last newline
			int lastLFOffset = fullText.lastIndexOf('\n');
			int lastCROffset = fullText.lastIndexOf('\r');
			// all text was on 1 line
			if (lastLFOffset == -1 && lastCROffset == -1) {
				// just subtract text length from current
				// available line width
				lineWidth -= fullText.length();
			} else {
				// calculate available line width of last line
				int lastNewLine = Math.max(lastLFOffset, lastCROffset);
				lineWidth = maxAvailableLineWidth - (textLength - lastNewLine);
			}
		}
		return lineWidth;
	}

	private String getLineDelimiter(IStructuredDocumentRegion currentRegion) {
		IStructuredDocument doc = currentRegion.getParentDocument();
		int line = doc.getLineOfOffset(currentRegion.getStartOffset());
		String lineDelimiter = doc.getLineDelimiter();
		try {
			if (line > 0) {
				lineDelimiter = doc.getLineDelimiter(line - 1);
			}
		} catch (BadLocationException e) {
			// log for now, unless we find reason not to
			Logger.log(Logger.INFO, e.getMessage());
		}
		// BUG115716: if cannot get line delimiter from current line, just
		// use default line delimiter
		if (lineDelimiter == null)
			lineDelimiter = doc.getLineDelimiter();
		return lineDelimiter;
	}

	private String extractLineDelimiters(String base,
			IStructuredDocumentRegion currentRegion) {
		String lineDelimiter = getLineDelimiter(currentRegion);
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < base.length();) {
			index = base.indexOf(lineDelimiter, index);
			if (index++ >= 0)
				sb.append(lineDelimiter);
			else
				break;
		}
		return sb.toString();
	}

	void setProgressMonitor(IProgressMonitor monitor) {
		fProgressMonitor = monitor;
	}
}
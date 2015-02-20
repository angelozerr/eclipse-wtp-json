package org.eclipse.wst.json.core.internal.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.sse.core.internal.ltk.parser.RegionParser;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.core.internal.util.Debug;

public class JSONSourceParser implements RegionParser {

	private long fStartTime;
	private long fStopTime;
	private JSONTokenizer fTokenizer;

	@Override
	public IStructuredDocumentRegion getDocumentRegions() {
		IStructuredDocumentRegion headnode = null;
		if (headnode == null) {
			if (Debug.perfTest) {
				fStartTime = System.currentTimeMillis();
			}
			headnode = parseNodes();
			if (Debug.perfTest) {
				fStopTime = System.currentTimeMillis();
				System.out
						.println(" -- creating nodes of IStructuredDocument -- "); //$NON-NLS-1$
				System.out
						.println(" Time parse and init all regions: " + (fStopTime - fStartTime) + " (msecs)"); //$NON-NLS-2$//$NON-NLS-1$
				// System.out.println(" for " + fRegions.size() + "
				// Regions");//$NON-NLS-2$//$NON-NLS-1$
				System.out
						.println("      and " + _countNodes(headnode) + " Nodes"); //$NON-NLS-2$//$NON-NLS-1$
			}
		}
		return headnode;
	}

	private IStructuredDocumentRegion parseNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Return the full list of known regions. Typically getNodes should be used
	 * instead of this method.
	 */
	@Override
	public List getRegions() {
		IStructuredDocumentRegion headNode = null;
		if (!getTokenizer().isEOF()) {
			headNode = getDocumentRegions();
			// throw new IllegalStateException("parsing has not finished");
		}
		// for memory recovery, we assume if someone
		// requests all regions, we can reset our big
		// memory consuming objects
		// but the new "getRegions" method is then more expensive.
		// I don't think its used much, though.
		List localRegionsList = getRegions(headNode);
		primReset();
		return localRegionsList;
	}

	/**
	 * Method getRegions.
	 * 
	 * @param headNode
	 * @return List
	 */
	protected List getRegions(IStructuredDocumentRegion headNode) {
		List allRegions = new ArrayList();
		IStructuredDocumentRegion currentNode = headNode;
		while (currentNode != null) {
			ITextRegionList nodeRegions = currentNode.getRegions();
			for (int i = 0; i < nodeRegions.size(); i++) {
				allRegions.add(nodeRegions.get(i));
			}
			currentNode = currentNode.getNext();
		}
		return allRegions;
	}

	@Override
	public RegionParser newInstance() {
		return new JSONSourceParser();
	}

	@Override
	public void reset(Reader reader) {
		primReset();
		getTokenizer().reset(reader, 0);
	}

	@Override
	public void reset(Reader reader, int offset) {
		reset(reader);
	}

	@Override
	public void reset(String input) {
		reset(new StringReader(input));
	}

	@Override
	public void reset(String input, int offset) {
		reset(input);
	}

	private void primReset() {
		getTokenizer().reset(new char[0]);
	}

	/**
	 * currently public but may be made default access protected in future.
	 */
	private JSONTokenizer getTokenizer() {
		if (fTokenizer == null) {
			fTokenizer = new JSONTokenizer();
		}
		return fTokenizer;
	}

	/**
	 * This is a simple utility to count nodes. Used only for debug statements.
	 */
	private int _countNodes(IStructuredDocumentRegion nodes) {
		int result = 0;
		IStructuredDocumentRegion countNode = nodes;
		while (countNode != null) {
			result++;
			countNode = countNode.getNext();
		}
		return result;
	}
}

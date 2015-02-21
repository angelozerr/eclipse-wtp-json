package org.eclipse.wst.json.core.internal.document;

import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONModel;
import org.eclipse.wst.sse.core.internal.model.AbstractStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.core.internal.provisional.events.IStructuredDocumentListener;
import org.eclipse.wst.sse.core.internal.provisional.events.NewDocumentEvent;
import org.eclipse.wst.sse.core.internal.provisional.events.NoChangeEvent;
import org.eclipse.wst.sse.core.internal.provisional.events.RegionChangedEvent;
import org.eclipse.wst.sse.core.internal.provisional.events.RegionsReplacedEvent;
import org.eclipse.wst.sse.core.internal.provisional.events.StructuredDocumentRegionsReplacedEvent;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegionList;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.core.internal.util.Assert;

public class JSONModelImpl extends AbstractStructuredModel implements
		IJSONModel, IStructuredDocumentListener {

	private JSONDocumentImpl document = null;
	private JSONModelParser fParser = null;
	private boolean fStructuredDocumentUpdate = false;

	public JSONModelImpl() {
	}

	@Override
	public IJSONDocument getDocument() {
		if (document == null) {
			this.document = createDocument();
			this.document.setModel(this);
		}
		return this.document;
	}

	@Override
	public IndexedRegion getIndexedRegion(int offset) {
		if (getDocument() == null)
			return null;
		return ((JSONStructuredDocumentRegionContainer) getDocument())
				.getContainerNode(offset);
	}

	@Override
	public IStructuredDocument getStructuredDocument() {
		IStructuredDocument structuredDocument = null;
		structuredDocument = super.getStructuredDocument();
		if (structuredDocument != null)
			return structuredDocument;

		// the first time
		Assert.isNotNull(getModelHandler());
		structuredDocument = (IStructuredDocument) getModelHandler()
				.getDocumentLoader().createNewStructuredDocument();

		setStructuredDocument(structuredDocument);
		return structuredDocument;
	}

	private JSONDocumentImpl createDocument() {
		return new JSONDocumentImpl();
	}

	@Override
	public void setStructuredDocument(IStructuredDocument newStructuredDocument) {
		IStructuredDocument oldStructuredDocument = super
				.getStructuredDocument();
		if (newStructuredDocument == oldStructuredDocument)
			return; // noting to do

		if (oldStructuredDocument != null)
			oldStructuredDocument.removeDocumentChangingListener(this);
		super.setStructuredDocument(newStructuredDocument);

		if (newStructuredDocument != null) {
			if (newStructuredDocument.getLength() > 0) {
				newModel(new NewDocumentEvent(newStructuredDocument, this));
			}
			newStructuredDocument.addDocumentChangingListener(this);
		}
	}

	@Override
	public void newModel(NewDocumentEvent structuredDocumentEvent) {
		if (structuredDocumentEvent == null)
			return;
		IStructuredDocument structuredDocument = structuredDocumentEvent
				.getStructuredDocument();
		if (structuredDocument == null)
			return;
		// this should not happen, but for the case
		if (structuredDocument != getStructuredDocument())
			setStructuredDocument(structuredDocument);
		IStructuredDocumentRegionList flatNodes = structuredDocument
				.getRegionList();
		if (flatNodes == null)
			return;
		if (getDocument() == null)
			return;

		fStructuredDocumentUpdate = true;

		((JSONStructuredDocumentRegionContainer) getDocument())
				.removeChildNodes();

		JSONModelParser parser = getParser();
		parser.setStructuredDocumentEvent(structuredDocumentEvent);
		parser.replaceStructuredDocumentRegions(flatNodes, null);

		fStructuredDocumentUpdate = false;
	}

	protected JSONModelParser getParser() {
		if (fParser == null) {
			if (getDocument() != null) {
				fParser = new JSONModelParser(document);
			}
		}
		return fParser;
	}

	@Override
	public void noChange(NoChangeEvent structuredDocumentEvent) {
		// nop
	}

	@Override
	public void nodesReplaced(
			StructuredDocumentRegionsReplacedEvent structuredDocumentEvent) {
		if (structuredDocumentEvent == null) {
			return;
		}
		IStructuredDocumentRegionList oldStructuredDocumentRegions = structuredDocumentEvent.getOldStructuredDocumentRegions();
		IStructuredDocumentRegionList newStructuredDocumentRegions = structuredDocumentEvent.getNewStructuredDocumentRegions();
		if (oldStructuredDocumentRegions == null && newStructuredDocumentRegions == null) {
			return;
		}

		fStructuredDocumentUpdate = true;

		JSONModelParser parser = getParser();
		parser.setStructuredDocumentEvent(structuredDocumentEvent);
		if (structuredDocumentEvent.isEntireDocumentReplaced())
			parser.replaceDocument(newStructuredDocumentRegions);
		else
			parser.replaceStructuredDocumentRegions(newStructuredDocumentRegions, oldStructuredDocumentRegions);

		fStructuredDocumentUpdate = false;
	}

	@Override
	public void regionChanged(RegionChangedEvent structuredDocumentEvent) {
		if (structuredDocumentEvent == null) {
			return;
		}
		IStructuredDocumentRegion flatNode = structuredDocumentEvent
				.getStructuredDocumentRegion();
		if (flatNode == null) {
			return;
		}
		ITextRegion region = structuredDocumentEvent.getRegion();
		if (region == null) {
			return;
		}

		fStructuredDocumentUpdate = true;

		JSONModelParser parser = getParser();
		parser.setStructuredDocumentEvent(structuredDocumentEvent);
		parser.changeRegion(flatNode, region);

		fStructuredDocumentUpdate = false;
	}

	@Override
	public void regionsReplaced(RegionsReplacedEvent structuredDocumentEvent) {
		if (structuredDocumentEvent == null)
			return;
		IStructuredDocumentRegion flatNode = structuredDocumentEvent
				.getStructuredDocumentRegion();
		if (flatNode == null)
			return;
		ITextRegionList oldRegions = structuredDocumentEvent.getOldRegions();
		ITextRegionList newRegions = structuredDocumentEvent.getNewRegions();
		if (oldRegions == null && newRegions == null)
			return;

		fStructuredDocumentUpdate = true;

		JSONModelParser parser = getParser();
		parser.setStructuredDocumentEvent(structuredDocumentEvent);
		parser.replaceRegions(flatNode, newRegions, oldRegions);

		fStructuredDocumentUpdate = false;
	}

	public void childReplaced(JSONNodeImpl jsonNodeImpl, JSONNodeImpl newChild,
			JSONNodeImpl oldChild) {

	}

}

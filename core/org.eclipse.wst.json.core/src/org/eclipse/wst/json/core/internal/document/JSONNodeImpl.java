/*******************************************************************************
 * Copyright (c) 2004, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.json.core.internal.document;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONModel;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.sse.core.internal.model.FactoryRegistry;
import org.eclipse.wst.sse.core.internal.provisional.AbstractNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.w3c.dom.DOMException;

/**
 * 
 */
public abstract class JSONNodeImpl extends AbstractNotifier implements
		IJSONNode, IndexedRegion, IAdaptable {

	private JSONDocumentImpl fOwnerDocument = null;
	private JSONNodeImpl fParentNode = null;
	private JSONNodeImpl fNextSibling = null;
	private JSONNodeImpl fPreviousSibling = null;
	private JSONNodeImpl fFirstChild = null;
	private JSONNodeImpl fLastChild = null;
	//protected JSONNamedNodeMapImpl fAttrs = null;

	/**
	 * JSONNodeImpl constructor comment.
	 */
	JSONNodeImpl() {
		super();
	}

	JSONNodeImpl(JSONNodeImpl that) {
		if (that != null) {
			this.fOwnerDocument = that.fOwnerDocument;
//			if (that.fAttrs != null) {
//				int nAttrs = that.fAttrs.getLength();
//				for (int i = 0; i < nAttrs; i++) {
//					JSONAttrImpl attr = (JSONAttrImpl) that.fAttrs.item(i);
//					setAttribute(attr.getName(), attr.getValue());
//				}
//			}
		}
	}

	public Object getAdapter(Class adapter) {
		final IStructuredModel model = fOwnerDocument != null ? fOwnerDocument
				.getModel() : null;
		return model != null ? Platform.getAdapterManager().getAdapter(model,
				adapter) : null;
	}

	/**
	 * currently public but may be made default access protected in future.
	 */
	public JSONNodeImpl appendChild(JSONNodeImpl newChild)
			throws org.w3c.dom.DOMException {
		return insertBefore(newChild, null);
	}

	/**
	 * currently public but may be made default access protected in future.
	 */
	protected void cloneChildNodes(IJSONNode newParent, boolean deep) {
		if (newParent == null || newParent == this)
			return;

		JSONNodeImpl container = (JSONNodeImpl) newParent;
		container.removeChildNodes();

		for (IJSONNode child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			JSONNodeImpl cloned = (JSONNodeImpl) child.cloneNode(deep);
			if (cloned != null)
				container.appendChild(cloned);
		}
	}

	/**
	 * @return boolean
	 * @param offset
	 *            int
	 */
	public boolean contains(int offset) {
		return (getStartOffset() <= offset && offset < getEndOffset());
	}

	/**
	 *
	 * currently public but may be made default access protected in future.
	 *
	 * @return java.lang.String
	 */
	protected String generateSource() {
		/*JSONSourceGenerator formatter = JSONSourceFormatterFactory
				.getInstance().getSourceFormatter(this);
		return formatter.format(this).toString();*/
		return "TODO";
	}

	/**
	 *
	 * currently public but may be made default access protected in future.
	 *
	 * @return java.lang.String
	 * @param name
	 *            java.lang.String
	 */
//	protected String getAttribute(String name) {
//		JSONAttrImpl attr = getAttributeNode(name);
//		if (attr != null)
//			return attr.getValue();
//		return null;
//	}
//
//	protected JSONAttrImpl getAttributeNode(String name) {
//		if (fAttrs == null)
//			return null;
//
//		int nAttrs = fAttrs.getLength();
//		for (int i = 0; i < nAttrs; i++) {
//			JSONAttrImpl attr = (JSONAttrImpl) fAttrs.item(i);
//			if (attr.matchName(name))
//				return attr;
//		}
//		return null;
//	}

	/**
	 * @return org.eclipse.wst.json.core.model.interfaces.IJSONNamedNodeMap
	 */
//	public IJSONNamedNodeMap getAttributes() {
//		if (fAttrs == null)
//			fAttrs = new JSONNamedNodeMapImpl();
//		return fAttrs;
//	}

	/*public IJSONNodeList getChildNodes() {
		JSONNodeListImpl list = new JSONNodeListImpl();
		for (IJSONNode node = getFirstChild(); node != null; node = node
				.getNextSibling()) {
			list.appendNode(node);
		}
		return list;
	}*/

	IJSONNode getCommonAncestor(IJSONNode node) {
		if (node == null)
			return null;

		for (IJSONNode na = node; na != null; na = na.getParentNode()) {
			for (IJSONNode ta = this; ta != null; ta = ta.getParentNode()) {
				if (ta == na)
					return ta;
			}
		}

		return null; // not found
	}

	JSONDocumentImpl getContainerDocument() {
		for (IJSONNode node = this; node != null; node = node.getParentNode()) {
			if (node instanceof JSONDocumentImpl) {
				JSONDocumentImpl doc = (JSONDocumentImpl) node;
				if (doc.isDocument())
					return doc;
			}
		}
		return null;
	}

	JSONNodeImpl getContainerNode(int offset) {
		if (!contains(offset))
			return null;

		for (IJSONNode child = getFirstChild(); child != null; child = child
				.getNextSibling()) {
			IJSONNode found = ((JSONNodeImpl) child).getContainerNode(offset);
			if (found != null)
				return (JSONNodeImpl) found;
		}

		return this;
	}

	/**
	 */
	public FactoryRegistry getFactoryRegistry() {
		IJSONModel model = getOwnerDocument().getModel();
		if (model != null) {
			FactoryRegistry reg = model.getFactoryRegistry();
			if (reg != null)
				return reg;
		}
		return null;
	}

	public IJSONNode getFirstChild() {
		return this.fFirstChild;
	}

	public IJSONNode getLastChild() {
		return this.fLastChild;
	}

	public IJSONNode getNextSibling() {
		return this.fNextSibling;
	}

	IJSONNode getNodeAt(int offset) {
		// the same as getContainerNode()
		return getContainerNode(offset);
	}

	public IJSONDocument getOwnerDocument() {
		return this.fOwnerDocument;
	}

	public IJSONNode getParentNode() {
		return this.fParentNode;
	}

	public IJSONNode getPreviousSibling() {
		return this.fPreviousSibling;
	}

	IJSONNode getRootNode() {
		JSONNodeImpl parent = (JSONNodeImpl) getParentNode();
		if (parent == null)
			return this;
		return parent.getRootNode();
	}

	/**
	 * @return boolean
	 */
	public boolean hasChildNodes() {
		return (this.fFirstChild != null);
	}

	/**
	 * @return boolean
	 */
	public boolean hasProperties() {
		return false;
	}

	/**
	 * currently public but may be made default access protected in future.
	 */
	public JSONNodeImpl insertBefore(JSONNodeImpl newChild,
			JSONNodeImpl refChild) throws org.w3c.dom.DOMException {
		if (newChild == null)
			return null;

		JSONNodeImpl child = newChild;
		JSONNodeImpl next = refChild;
		JSONNodeImpl prev = null;
		if (next == null) {
			prev = this.fLastChild;
			this.fLastChild = child;
		} else {
			prev = (JSONNodeImpl) next.getPreviousSibling();
			next.setPreviousSibling(child);
		}

		if (prev == null)
			this.fFirstChild = child;
		else
			prev.setNextSibling(child);
		child.setPreviousSibling(prev);
		child.setNextSibling(next);
		child.setParentNode(this);

		notifyChildReplaced(child, null);

		return newChild;
	}

//	protected void notifyAttrReplaced(JSONNodeImpl newAttr, JSONNodeImpl oldAttr) {
//		// for model
//		IJSONDocument doc = getContainerDocument();
//		if (doc == null)
//			return;
//		JSONModelImpl model = (JSONModelImpl) doc.getModel();
//		if (model == null)
//			return;
//		model.attrReplaced(this, newAttr, oldAttr);
//
//		// for adapters
//		int type = CHANGE;
//		if (newAttr == null)
//			type = REMOVE;
//		else if (oldAttr == null)
//			type = ADD;
//		notify(type, oldAttr, oldAttr, newAttr, getStartOffset());
//	}

	protected void notifyChildReplaced(JSONNodeImpl newChild,
			JSONNodeImpl oldChild) {
		// for model
		IJSONDocument doc = getContainerDocument();
		if (doc == null)
			return;
		JSONModelImpl model = (JSONModelImpl) doc.getModel();
		if (model == null)
			return;
		model.childReplaced(this, newChild, oldChild);

		// for adapters
		int type = CHANGE;
		if (newChild == null)
			type = REMOVE;
		else if (oldChild == null)
			type = ADD;
		notify(type, oldChild, oldChild, newChild, getStartOffset());
	}

//	void removeAttributeNode(JSONNodeImpl attr) {
//		// find
//		int nAttrs = fAttrs.getLength();
//		for (int i = 0; i < nAttrs; i++) {
//			if (fAttrs.item(i) == attr) {
//				fAttrs.removeNode(i);
//				notifyAttrReplaced(null, attr);
//				return;
//			}
//		}
//	}

	protected JSONNodeImpl removeChild(JSONNodeImpl oldChild)
			throws org.w3c.dom.DOMException {
		if (oldChild == null)
			return null;
		if (oldChild.getParentNode() != this)
			return null;

		// close import rules
//		ImportRuleCollector trav = new ImportRuleCollector();
//		trav.apply(oldChild);
//		Iterator it = trav.getRules().iterator();
//		while (it.hasNext()) {
//			((JSONImportRuleImpl) it.next()).closeStyleSheet();
//		}

		JSONNodeImpl child = oldChild;
		JSONNodeImpl prev = (JSONNodeImpl) child.getPreviousSibling();
		JSONNodeImpl next = (JSONNodeImpl) child.getNextSibling();

		if (prev == null)
			this.fFirstChild = next;
		else
			prev.setNextSibling(next);

		if (next == null)
			this.fLastChild = prev;
		else
			next.setPreviousSibling(prev);

		child.setPreviousSibling(null);
		child.setNextSibling(null);
		child.setParentNode(null);

		notifyChildReplaced(null, child);

		return child;
	}

	/**
	 * 
	 */
	void removeChildNodes() {
		IJSONNode nextChild = null;
		for (IJSONNode child = getFirstChild(); child != null; child = nextChild) {
			nextChild = child.getNextSibling();
			removeChild((JSONNodeImpl) child);
		}
	}

	protected JSONNodeImpl replaceChild(JSONNodeImpl newChild,
			JSONNodeImpl oldChild) throws org.w3c.dom.DOMException {
		if (oldChild == null)
			return newChild;
		if (newChild != null)
			insertBefore(newChild, oldChild);
		return removeChild(oldChild);
	}

	/**
	 *
	 * currently public but may be made default access protected in future.
	 *
	 * @param name
	 *            java.lang.String
	 * @param value
	 *            java.lang.String
	 */
//	public void setAttribute(String name, String value) {
//		if (name == null)
//			return;
//
//		JSONAttrImpl attr = getAttributeNode(name);
//		if (attr != null) {
//			String oldValue = attr.getValue();
//			if (value != null && value.equals(oldValue))
//				return;
//			if (value == null) {
//				if (oldValue != null) {
//					removeAttributeNode(attr);
//				}
//				return;
//			}
//		} else {
//			if (value == null)
//				return;
//			if (fAttrs == null)
//				fAttrs = new JSONNamedNodeMapImpl();
//			JSONDocumentImpl doc = (JSONDocumentImpl) getOwnerDocument();
//			if (doc == null)
//				return;
//			attr = (JSONAttrImpl) doc.createJSONAttr(name);
//			attr.setOwnerJSONNode(this);
//			fAttrs.appendNode(attr);
//			notifyAttrReplaced(attr, null);
//		}
//		attr.setValue(value);
//	}

	/**
	 * @param jsonText
	 *            java.lang.String
	 */
//	public void setCssText(String jsonText) {
//		// TODO : call flat model parser and replace myself with new three!!
//		throw new DOMException(DOMException.INVALID_MODIFICATION_ERR, "");//$NON-NLS-1$
//	}

	private void setNextSibling(IJSONNode nextSibling) {
		this.fNextSibling = (JSONNodeImpl) nextSibling;
	}

	/**
	 * currently public but may be made default access protected in future.
	 */
	public void setOwnerDocument(IJSONDocument ownerDocument) {
		this.fOwnerDocument = (JSONDocumentImpl) ownerDocument;
	}

	private void setParentNode(IJSONNode parentNode) {
		this.fParentNode = (JSONNodeImpl) parentNode;
	}

	private void setPreviousSibling(IJSONNode previousSibling) {
		this.fPreviousSibling = (JSONNodeImpl) previousSibling;
	}

	public int getLength() {
		int result = -1;
		int start = getStartOffset();
		if (start >= 0) {
			int end = getEndOffset();
			if (end >= 0) {
				result = end - start;
				if (result < -1) {
					result = -1;
				}
			}
		}
		return result;
	}
}

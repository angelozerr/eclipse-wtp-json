package org.eclipse.wst.json.core.internal.document;

public abstract class JSONValueImpl extends JSONNodeImpl {

	protected JSONValueImpl() {
		super();
	}

	/**
	 * NodeImpl constructor
	 * 
	 * @param that
	 *            NodeImpl
	 */
	protected JSONValueImpl(JSONValueImpl that) {
		super(that);
	}
}

package org.eclipse.wst.json.core.contenttype;

/**
 * This class, with its one field, is a convience to provide compile-time safety
 * when refering to a contentType ID. The value of the contenttype id field must
 * match what is specified in plugin.xml file.
 */

public class ContentTypeIdForJSON {
	/**
	 * The value of the contenttype id field must match what is specified in
	 * plugin.xml file. Note: this value is intentially set with default
	 * protected method so it will not be inlined.
	 */
	public final static String ContentTypeID_JSON = getConstantString();

	/**
	 * Don't allow instantiation.
	 */
	private ContentTypeIdForJSON() {
		super();
	}

	static String getConstantString() {
		return "org.eclipse.wst.json.core.jsonsource"; //$NON-NLS-1$
	}

}

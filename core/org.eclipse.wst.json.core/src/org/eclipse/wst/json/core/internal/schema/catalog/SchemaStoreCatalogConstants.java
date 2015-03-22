package org.eclipse.wst.json.core.internal.schema.catalog;

/**
 * SchemaStore constants
 * 
 * @see See http://schemastore.org/api.html
 * @see https 
 *      ://github.com/SchemaStore/schemastore/blob/master/src/api/json/catalog
 *      .json
 */
public interface SchemaStoreCatalogConstants {

	/** Types of the schema entries */
	/** The SCHEMA element name. */
	String TAG_SCHEMA = "schema"; //$NON-NLS-1$
	String ATTR_SCHEMA_NAME = "name"; //$NON-NLS-1$
	String ATTR_SCHEMA_DESCRIPTION = "description"; //$NON-NLS-1$
	String ATTR_SCHEMA_FILEMATCH = "fileMatch"; //$NON-NLS-1$
	String ATTR_SCHEMA_URL = "url"; //$NON-NLS-1$
	String ATTR_SCHEMA_URI = "uri"; //$NON-NLS-1$
}

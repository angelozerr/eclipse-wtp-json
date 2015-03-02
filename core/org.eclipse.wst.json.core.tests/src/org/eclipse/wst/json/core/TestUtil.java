package org.eclipse.wst.json.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.wst.json.core.contenttype.ContentTypeIdForJSON;
import org.eclipse.wst.json.core.document.IJSONDocument;
import org.eclipse.wst.json.core.document.IJSONNode;
import org.eclipse.wst.json.core.document.IJSONObject;
import org.eclipse.wst.json.core.document.IJSONPair;
import org.eclipse.wst.json.core.document.IJSONStructure;
import org.eclipse.wst.json.core.document.IJSONValue;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;

public class TestUtil {

	public static String toString(IJSONDocument document) {
		StringBuilder json = new StringBuilder();
		serialize(document, json);
		return json.toString();
	}

	private static void serialize(IJSONNode node, StringBuilder json) {
		if (node == null) {
			return;
		}
		// for (IJSONNode child = node.getFirstChild(); child != null; child =
		// child
		// .getNextSibling()) {
		switch (node.getNodeType()) {
		case IJSONNode.DOCUMENT_NODE:
			serialize(node.getFirstChild(), json);
			break;
		case IJSONNode.OBJECT_NODE:
		case IJSONNode.ARRAY_NODE:
			IJSONStructure object = (IJSONStructure) node;
			json.append(object.getFirstStructuredDocumentRegion().getText());
			serialize(node.getFirstChild(), json);
			if (object.isClosed()) {
				json.append(object.getEndStructuredDocumentRegion().getText());
			}
			if (node.getNextSibling() != null) {
				json.append(",");
				serialize(node.getNextSibling(), json);
			}
			break;
		case IJSONNode.PAIR_NODE:
			IJSONPair pair = (IJSONPair) node;
			json.append("\"");
			json.append(pair.getName());
			json.append("\"");
			if (pair.getValue() != null) {
				json.append(":");
				serialize(pair.getValue(), json);
			}
			if (node.getNextSibling() != null) {
				json.append(",");
				serialize(node.getNextSibling(), json);
			}
			break;
		case IJSONNode.VALUE_BOOLEAN_NODE:
		case IJSONNode.VALUE_NULL_NODE:
		case IJSONNode.VALUE_NUMBER_NODE:
		case IJSONNode.VALUE_STRING_NODE:
			IJSONValue value = (IJSONValue) node;
			json.append(value.getSimpleValue());
			if (node.getNextSibling() != null) {
				json.append(",");
				serialize(node.getNextSibling(), json);
			}
			break;
		}

		// }
	}

	public static String toString(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public static IStructuredModel createModel() {
		IModelManager manager = StructuredModelManager.getModelManager();
		return manager
				.createUnManagedStructuredModelFor(ContentTypeIdForJSON.ContentTypeID_JSON);

	}
}

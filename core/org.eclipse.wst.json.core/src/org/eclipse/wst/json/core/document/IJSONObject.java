package org.eclipse.wst.json.core.document;

public interface IJSONObject extends IJSONStructure {

	IJSONObject add(IJSONPair pair);

	IJSONObject remove(IJSONPair pair);

}

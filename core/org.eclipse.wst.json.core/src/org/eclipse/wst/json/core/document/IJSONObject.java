package org.eclipse.wst.json.core.document;

import java.util.List;

public interface IJSONObject extends IJSONStructure {

	IJSONObject add(IJSONPair pair);

	IJSONObject remove(IJSONPair pair);

	List<IJSONPair> getPairs();
}

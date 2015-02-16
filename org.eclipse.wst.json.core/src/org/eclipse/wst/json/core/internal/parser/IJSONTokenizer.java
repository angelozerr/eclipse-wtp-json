package org.eclipse.wst.json.core.internal.parser;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

public interface IJSONTokenizer {

	void setInitialState(int initialState);

	void setInitialBufferSize(int bufsize);

	boolean isEOF();

	void reset(Reader reader, int i);

	ITextRegion getNextToken() throws IOException;

	void reset(char[] cs);

	int getOffset();
}

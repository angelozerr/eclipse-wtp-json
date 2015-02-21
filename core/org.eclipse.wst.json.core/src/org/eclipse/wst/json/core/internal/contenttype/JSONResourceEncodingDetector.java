package org.eclipse.wst.json.core.internal.contenttype;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.internal.encoding.CodedIO;
import org.eclipse.wst.sse.core.internal.encoding.EncodingMemento;
import org.eclipse.wst.sse.core.internal.encoding.IResourceCharsetDetector;
import org.eclipse.wst.sse.core.internal.encoding.NonContentBasedEncodingRules;

public class JSONResourceEncodingDetector implements IResourceCharsetDetector {

	private EncodingMemento fEncodingMemento;
	private boolean fHeaderParsed;
	private Reader fReader;

	class NullMemento extends EncodingMemento {
		/**
		 * 
		 */
		public NullMemento() {
			super();
			String defaultCharset = NonContentBasedEncodingRules
					.useDefaultNameRules(null);
			setJavaCharsetName(defaultCharset);
			setAppropriateDefault(defaultCharset);
			setDetectedCharsetName(null);
		}
	}

	@Override
	public String getEncoding() throws IOException {
		return getEncodingMemento().getDetectedCharsetName();
	}

	public EncodingMemento getEncodingMemento() throws IOException {
		if (fEncodingMemento == null) {
			// safty net
			fEncodingMemento = new NullMemento();
		}
		return fEncodingMemento;
	}

	@Override
	public String getSpecDefaultEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	private void resetAll() {
		fReader = null;
		fHeaderParsed = false;
		fEncodingMemento = null;
	}

	@Override
	public void set(InputStream inputStream) {
		resetAll();
		fReader = new ByteReader(inputStream);
		try {
			fReader.mark(CodedIO.MAX_MARK_SIZE);
		} catch (IOException e) {
			// impossible, since we know ByteReader
			// supports marking
			throw new Error(e);
		}
	}

	@Override
	public void set(Reader reader) {
		resetAll();
		fReader = reader;
		if (!fReader.markSupported()) {
			fReader = new BufferedReader(fReader);
		}
		try {
			fReader.mark(CodedIO.MAX_MARK_SIZE);
		} catch (IOException e) {
			// impossble, since we just checked if markable
			throw new Error(e);
		}
	}

	@Override
	public void set(IStorage iStorage) throws CoreException {
		resetAll();
		InputStream inputStream = iStorage.getContents();
		InputStream resettableStream = new BufferedInputStream(inputStream,
				CodedIO.MAX_BUF_SIZE);
		resettableStream.mark(CodedIO.MAX_MARK_SIZE);
		set(resettableStream);
		// TODO we'll need to "remember" IFile, or
		// get its (or its project's) settings, in case
		// those are needed to handle cases when the
		// encoding is not in the file stream.
	}

}

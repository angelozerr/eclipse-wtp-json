package org.eclipse.wst.json.ui.internal.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.link.ILinkedModeListener;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI.ExitFlags;
import org.eclipse.jface.text.link.LinkedModeUI.IExitPolicy;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.wst.sse.ui.typing.AbstractCharacterPairInserter;

public class JSONCharacterPairInserter extends AbstractCharacterPairInserter {

	public boolean hasPair(char c) {
		switch (c) {
		case '"':
		case '\'':
		case '[':
		case '{':
			return true;
		default:
			return false;
		}
	}

	protected char getPair(char c) {
		switch (c) {
		case '\'':
		case '"':
			return c;
		case '[':
			return ']';
		case '{':
			return '}';
		default:
			throw new IllegalArgumentException();
		}
	}

	class ExitPolicy implements IExitPolicy {

		private char fExit;
		private char fEscape;
		private IDocument fDocument;

		public ExitPolicy(char exit, char escape, IDocument document) {
			fExit = exit;
			fEscape = escape;
			fDocument = document;
		}

		public ExitFlags doExit(LinkedModeModel model, VerifyEvent event,
				int offset, int length) {
			if (!isMasked(offset)) {
				if (event.character == fExit)
					return new ExitFlags(ILinkedModeListener.UPDATE_CARET,
							false);
				if (event.character == '\r' || event.character == '\n'
						&& offset > 0) {
					try {
						if (fDocument.getChar(offset - 1) == '{') {
							return new ExitFlags(ILinkedModeListener.EXIT_ALL,
									true);
						}
					} catch (BadLocationException e) {
					}
				}
			}
			return null;
		}

		private boolean isMasked(int offset) {
			try {
				return fEscape == fDocument.getChar(offset - 1);
			} catch (BadLocationException e) {
			}
			return false;
		}
	}

	protected IExitPolicy getExitPolicy(char exit, char escape,
			IDocument document) {
		return new ExitPolicy(exit, escape, document);
	}
}

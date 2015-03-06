package org.eclipse.wst.json.core.validation;

import java.io.StringReader;
import java.util.List;

import org.eclipse.wst.json.core.internal.parser.JSONLineTokenizer;
import org.eclipse.wst.validation.internal.operations.LocalizedMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.junit.Assert;
import org.junit.Test;

public class JSONSyntaxValidatorHelperTest {

	private static final ISeverityProvider PROVIDER = new ISeverityProvider() {

		@Override
		public int getSeverity(String preferenceName) {
			return 1;
		}
	};

	@Test
	public void noErrors() throws Exception {
		IReporter reporter = validate("{}");
		List messages = reporter.getMessages();
		Assert.assertEquals(0, messages.size());
	}

	@Test
	public void missingEndObject() throws Exception {
		IReporter reporter = validate("{");
		List messages = reporter.getMessages();
		Assert.assertEquals(1, messages.size());
		LocalizedMessage msg = (LocalizedMessage) messages.get(0);
		assertMessage(msg, "Missing end object", 1, 1);
	}


	@Test
	public void badObjectKey() throws Exception {
		IReporter reporter = validate("{aa}");
		List messages = reporter.getMessages();
		Assert.assertEquals(1, messages.size());
		LocalizedMessage msg = (LocalizedMessage) messages.get(0);
		assertMessage(msg, "Expected object key but found undefined", 1, 1);
	}

	@Test
	public void missingEndObjectAndBadObjectKey() throws Exception {
		IReporter reporter = validate("{aa");
		List messages = reporter.getMessages();
		Assert.assertEquals(2, messages.size());
		LocalizedMessage msg = (LocalizedMessage) messages.get(0);
		assertMessage(msg, "Expected object key but found undefined", 1, 1);
		msg = (LocalizedMessage) messages.get(1);
		assertMessage(msg, "Missing end object", 1, 1);
	}
	
	private void assertMessage(LocalizedMessage msg, String message,
			int lineNumber, int length) {
		Assert.assertEquals(msg.getLocalizedMessage(), message);
		Assert.assertEquals(msg.getLineNumber(), lineNumber);
		Assert.assertEquals(msg.getLength(), length);
	}

	public IReporter validate(String json) {
		IReporter reporter = new MockReporter();
		JSONLineTokenizer tokenizer = new JSONLineTokenizer(new StringReader(
				json));
		JSONSyntaxValidatorHelper.validate(tokenizer, reporter, null, PROVIDER);
		return reporter;
	}
}

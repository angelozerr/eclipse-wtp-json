package org.eclipse.wst.json.core.internal.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.junit.Assert;
import org.junit.Test;

public class JSONTokenizerTest {

	@Test
	public void array() {
		assertRegions("[]", "[ContextRegion--> JSON_ARRAY_OPEN: 0-1, "
				+ "ContextRegion--> JSON_ARRAY_CLOSE: 1-2]");
	}

	@Test
	public void arrayString() {
		assertRegions("[\"a\"]", "[ContextRegion--> JSON_ARRAY_OPEN: 0-1, "
				+ "ContextRegion--> JSON_VALUE_STRING: 1-4, "
				+ "ContextRegion--> JSON_ARRAY_CLOSE: 4-5]");
	}

	@Test
	public void array2String() {
		assertRegions("[\"a\", \"b\"]",
				"[ContextRegion--> JSON_ARRAY_OPEN: 0-1, "
						+ "ContextRegion--> JSON_VALUE_STRING: 1-4, "
						+ "ContextRegion--> JSON_COMMA: 4-5, "
						+ "ContextRegion--> WHITE_SPACE: 5-6, "
						+ "ContextRegion--> JSON_VALUE_STRING: 6-9, "
						+ "ContextRegion--> JSON_ARRAY_CLOSE: 9-10]");
	}

	@Test
	public void objectWithArray() {
		assertRegions("{\"array\":[]}", "[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
				+ "ContextRegion--> JSON_OBJECT_KEY: 1-8, "
				+ "ContextRegion--> JSON_COLON: 8-9, "
				+ "ContextRegion--> JSON_ARRAY_OPEN: 9-10, "
				+ "ContextRegion--> JSON_ARRAY_CLOSE: 10-11, "
				+ "ContextRegion--> JSON_OBJECT_CLOSE: 11-12]");
	}
	
	@Test
	public void notValidStartObject() {
		assertRegions("{", "[ContextRegion--> JSON_OBJECT_OPEN: 0-1]");
	}

	@Test
	public void notValidEndObject() {
		assertRegions("}", "[ContextRegion--> JSON_UNKNOWN: 0-1]");
	}

	@Test
	public void emptyJSON() {
		assertRegions("{}", "[ContextRegion--> JSON_OBJECT_OPEN: 0-1,"
				+ " ContextRegion--> JSON_OBJECT_CLOSE: 1-2]");
	}

	@Test
	public void emptyJSONWithSpace() {
		assertRegions("{}  \n\r", "[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
				+ "ContextRegion--> JSON_OBJECT_CLOSE: 1-2, "
				+ "ContextRegion--> WHITE_SPACE: 2-6]");
	}

	@Test
	public void notValidKeyStartQuote() {
		assertRegions("{\"}", "[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
				+ "ContextRegion--> JSON_UNKNOWN: 1-2, "
				+ "ContextRegion--> JSON_OBJECT_CLOSE: 2-3]");
	}

	@Test
	public void notValidKeyDontEndWithQuote() {
		assertRegions("{\"config}", "[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
				+ "ContextRegion--> JSON_UNKNOWN: 1-8, "
				+ "ContextRegion--> JSON_OBJECT_CLOSE: 8-9]");
	}

	@Test
	public void notValidMissColon() {
		assertRegions("{\"config\"}",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-9, "
						+ "ContextRegion--> JSON_UNKNOWN: 9-10]");
	}

	@Test
	public void notValidMissValue() {
		assertRegions("{\"config\": }",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-9, "
						+ "ContextRegion--> JSON_COLON: 9-10, "
						+ "ContextRegion--> WHITE_SPACE: 10-11, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 11-12]");
	}

	@Test
	public void trueValue() {
		assertRegions("{\"config\": true}",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-9, "
						+ "ContextRegion--> JSON_COLON: 9-10, "
						+ "ContextRegion--> WHITE_SPACE: 10-11, "
						+ "ContextRegion--> JSON_VALUE_BOOLEAN: 11-15, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 15-16]");
	}

	@Test
	public void stringValue() {
		assertRegions("{\"config\": \"true\"}",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-9, "
						+ "ContextRegion--> JSON_COLON: 9-10, "
						+ "ContextRegion--> WHITE_SPACE: 10-11, "
						+ "ContextRegion--> JSON_VALUE_STRING: 11-17, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 17-18]");
	}

	@Test
	public void nullValue() {
		assertRegions("{\"config\": null}",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-9, "
						+ "ContextRegion--> JSON_COLON: 9-10, "
						+ "ContextRegion--> WHITE_SPACE: 10-11, "
						+ "ContextRegion--> JSON_VALUE_NULL: 11-15, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 15-16]");
	}

	@Test
	public void numberValue() {
		assertRegions("{\"config\": 5   }",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-9, "
						+ "ContextRegion--> JSON_COLON: 9-10, "
						+ "ContextRegion--> WHITE_SPACE: 10-11, "
						+ "ContextRegion--> JSON_VALUE_NUMBER: 11-12, "
						+ "ContextRegion--> WHITE_SPACE: 12-15, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 15-16]");
	}

	@Test
	public void embed() {
		assertRegions("{\"config\": {}}",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-9, "
						+ "ContextRegion--> JSON_COLON: 9-10, "
						+ "ContextRegion--> WHITE_SPACE: 10-11, "
						+ "ContextRegion--> JSON_OBJECT_OPEN: 11-12, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 12-13, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 13-14]");
	}

	@Test
	public void twoFields() {
		assertRegions("{\"a\": 1, \"b\": 2}",
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 1-4, "
						+ "ContextRegion--> JSON_COLON: 4-5, "
						+ "ContextRegion--> WHITE_SPACE: 5-6, "
						+ "ContextRegion--> JSON_VALUE_NUMBER: 6-7, "
						+ "ContextRegion--> JSON_COMMA: 7-8, "
						+ "ContextRegion--> WHITE_SPACE: 8-9, "
						+ "ContextRegion--> JSON_OBJECT_KEY: 9-12, "
						+ "ContextRegion--> JSON_COLON: 12-13, "
						+ "ContextRegion--> WHITE_SPACE: 13-14, "
						+ "ContextRegion--> JSON_VALUE_NUMBER: 14-15, "
						+ "ContextRegion--> JSON_OBJECT_CLOSE: 15-16]");
	}

	@Test
	public void sample() {
		InputStream json = JSONTokenizerTest.class
				.getResourceAsStream("sample.json");
		assertRegions(
				json,
				"[ContextRegion--> JSON_OBJECT_OPEN: 0-1, ContextRegion--> WHITE_SPACE: 1-7, ContextRegion--> JSON_OBJECT_KEY: 7-17, ContextRegion--> JSON_COLON: 17-18, ContextRegion--> WHITE_SPACE: 18-19, ContextRegion--> JSON_OBJECT_OPEN: 19-20, ContextRegion--> WHITE_SPACE: 20-30, ContextRegion--> JSON_OBJECT_KEY: 30-37, ContextRegion--> JSON_COLON: 37-38, ContextRegion--> WHITE_SPACE: 38-39, ContextRegion--> JSON_VALUE_STRING: 39-57, ContextRegion--> JSON_COMMA: 57-58, ContextRegion--> WHITE_SPACE: 58-62, ContextRegion--> JSON_OBJECT_KEY: 62-72, ContextRegion--> JSON_COLON: 72-73, ContextRegion--> WHITE_SPACE: 73-74, ContextRegion--> JSON_OBJECT_OPEN: 74-75, ContextRegion--> WHITE_SPACE: 75-89, ContextRegion--> JSON_OBJECT_KEY: 89-96, ContextRegion--> JSON_COLON: 96-97, ContextRegion--> WHITE_SPACE: 97-98, ContextRegion--> JSON_VALUE_STRING: 98-101, ContextRegion--> JSON_COMMA: 101-102, ContextRegion--> WHITE_SPACE: 102-107, ContextRegion--> JSON_OBJECT_KEY: 107-118, ContextRegion--> JSON_COLON: 118-119, ContextRegion--> WHITE_SPACE: 119-120, ContextRegion--> JSON_OBJECT_OPEN: 120-121, ContextRegion--> WHITE_SPACE: 121-139, ContextRegion--> JSON_OBJECT_KEY: 139-151, ContextRegion--> JSON_COLON: 151-152, ContextRegion--> WHITE_SPACE: 152-153, ContextRegion--> JSON_OBJECT_OPEN: 153-154, ContextRegion--> WHITE_SPACE: 154-176, ContextRegion--> JSON_OBJECT_KEY: 176-180, ContextRegion--> JSON_COLON: 180-181, ContextRegion--> WHITE_SPACE: 181-182, ContextRegion--> JSON_VALUE_STRING: 182-188, ContextRegion--> JSON_COMMA: 188-189, ContextRegion--> WHITE_SPACE: 189-196, ContextRegion--> JSON_OBJECT_KEY: 196-204, ContextRegion--> JSON_COLON: 204-205, ContextRegion--> WHITE_SPACE: 205-206, ContextRegion--> JSON_VALUE_STRING: 206-212, ContextRegion--> JSON_COMMA: 212-213, ContextRegion--> WHITE_SPACE: 213-220, ContextRegion--> JSON_OBJECT_KEY: 220-231, ContextRegion--> JSON_COLON: 231-232, ContextRegion--> WHITE_SPACE: 232-233, ContextRegion--> JSON_VALUE_STRING: 233-271, ContextRegion--> JSON_COMMA: 271-272, ContextRegion--> WHITE_SPACE: 272-279, ContextRegion--> JSON_OBJECT_KEY: 279-288, ContextRegion--> JSON_COLON: 288-289, ContextRegion--> WHITE_SPACE: 289-290, ContextRegion--> JSON_VALUE_STRING: 290-296, ContextRegion--> JSON_COMMA: 296-297, ContextRegion--> WHITE_SPACE: 297-304, ContextRegion--> JSON_OBJECT_KEY: 304-312, ContextRegion--> JSON_COLON: 312-313, ContextRegion--> WHITE_SPACE: 313-314, ContextRegion--> JSON_VALUE_STRING: 314-329, ContextRegion--> JSON_COMMA: 329-330, ContextRegion--> WHITE_SPACE: 330-337, ContextRegion--> JSON_OBJECT_KEY: 337-347, ContextRegion--> JSON_COLON: 347-348, ContextRegion--> WHITE_SPACE: 348-349, ContextRegion--> JSON_OBJECT_OPEN: 349-350, ContextRegion--> WHITE_SPACE: 350-376, ContextRegion--> JSON_OBJECT_KEY: 376-382, ContextRegion--> JSON_COLON: 382-383, ContextRegion--> WHITE_SPACE: 383-384, ContextRegion--> JSON_VALUE_STRING: 384-458, ContextRegion--> JSON_COMMA: 458-459, ContextRegion--> WHITE_SPACE: 459-467, ContextRegion--> JSON_OBJECT_KEY: 467-481, ContextRegion--> JSON_COLON: 481-482, ContextRegion--> WHITE_SPACE: 482-483, ContextRegion--> JSON_ARRAY_OPEN: 483-484, ContextRegion--> JSON_VALUE_STRING: 484-489, ContextRegion--> JSON_COMMA: 489-490, ContextRegion--> WHITE_SPACE: 490-491, ContextRegion--> JSON_VALUE_STRING: 491-496, ContextRegion--> JSON_ARRAY_CLOSE: 496-497, ContextRegion--> WHITE_SPACE: 497-519, ContextRegion--> JSON_OBJECT_CLOSE: 519-520, ContextRegion--> JSON_COMMA: 520-521, ContextRegion--> WHITE_SPACE: 521-528, ContextRegion--> JSON_OBJECT_KEY: 528-538, ContextRegion--> JSON_COLON: 538-539, ContextRegion--> WHITE_SPACE: 539-540, ContextRegion--> JSON_VALUE_STRING: 540-548, ContextRegion--> WHITE_SPACE: 548-566, ContextRegion--> JSON_OBJECT_CLOSE: 566-567, ContextRegion--> WHITE_SPACE: 567-581, ContextRegion--> JSON_OBJECT_CLOSE: 581-582, ContextRegion--> WHITE_SPACE: 582-592, ContextRegion--> JSON_OBJECT_CLOSE: 592-593, ContextRegion--> WHITE_SPACE: 593-599, ContextRegion--> JSON_OBJECT_CLOSE: 599-600, ContextRegion--> WHITE_SPACE: 600-602, ContextRegion--> JSON_OBJECT_CLOSE: 602-603]");
	}

	@Test
	public void comment() {
		assertRegions("/*{}*/", "[ContextRegion--> JSON_COMMENT: 0-6]");
	}
	
	@Test
	public void simpleComment() {
		assertRegions("// bla bla bla\n{}", "[ContextRegion--> JSON_COMMENT: 0-14, "
				+ "ContextRegion--> WHITE_SPACE: 14-15, "
				+ "ContextRegion--> JSON_OBJECT_OPEN: 15-16, "
				+ "ContextRegion--> JSON_OBJECT_CLOSE: 16-17]");
	}
	
	private void assertRegions(String json, String regions) {
		assertRegions(new java.io.StringReader(json), regions);
	}

	private void assertRegions(InputStream json, String regions) {
		assertRegions(new InputStreamReader(json), regions);
	}

	private void assertRegions(Reader reader, String regions) {
		JSONTokenizer tokenizer = new JSONTokenizer();
		tokenizer.reset(reader, 0);
		List<ITextRegion> r = getRegions(tokenizer);
		System.err.println(reader.toString() + "==>" + r);
		Assert.assertEquals(regions, r.toString());
	}

	private static final List<ITextRegion> getRegions(JSONTokenizer tokenizer) {
		List<ITextRegion> tokens = new ArrayList<ITextRegion>();
		ITextRegion region = null;
		try {
			region = tokenizer.getNextToken();
			while (region != null) {
				if (region != null) {
					tokens.add(region);
				}
				region = tokenizer.getNextToken();
			}
		} catch (StackOverflowError e) {
			//Logger.logException(getClass().getName()+": input could not be tokenized correctly at position " + getOffset(), e);//$NON-NLS-1$
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();
			// Since this is convenience method and NOT the recommended
			// way of getting tokens, many errors are simply hidden
			//Logger.logException("Exception not handled retrieving regions: " + e.getLocalizedMessage(), e);//$NON-NLS-1$
		}
		return tokens;
	}

}

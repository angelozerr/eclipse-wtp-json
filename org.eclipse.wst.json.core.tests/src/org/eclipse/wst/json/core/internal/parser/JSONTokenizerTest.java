package org.eclipse.wst.json.core.internal.parser;

//import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.junit.Test;

public class JSONTokenizerTest {

	@Test
	public void notValidStartObject() {
		List regions = null;
		assertRegions("{", regions);
	}

	@Test
	public void notValidEndObject() {
		List regions = null;
		assertRegions("}", regions);
	}

	@Test
	public void emptyJSON() {
		List regions = null;
		assertRegions("{}", regions);
	}

	@Test
	public void notValidKeyStartQuote() {
		List regions = null;
		assertRegions("{\"}", regions);
	}

	@Test
	public void notValidKeyDontEndWithQuote() {
		List regions = null;
		assertRegions("{\"config}", regions);
	}

	@Test
	public void notValidMissColon() {
		List regions = null;
		assertRegions("{\"config\"}", regions);
	}

	@Test
	public void notValidMissValue() {
		List regions = null;
		assertRegions("{\"config\": }", regions);
	}

	@Test
	public void trueValue() {
		List regions = null;
		assertRegions("{\"config\": true}", regions);
	}

	@Test
	public void stringValue() {
		List regions = null;
		assertRegions("{\"config\": \"true\"}", regions);
	}

	@Test
	public void nullValue() {
		List regions = null;
		assertRegions("{\"config\": null}", regions);
	}

	@Test
	public void numberValue() {
		List regions = null;
		assertRegions("{\"config\": 5   }", regions);
	}

	@Test
	public void embed() {
		List regions = null;
		assertRegions("{\"config\": {}}", regions);
	}

	@Test
	public void sample() {
		List regions = null;
		Reader reader = new InputStreamReader(
				JSONTokenizerTest.class.getResourceAsStream("sample.json"));
		JSONTokenizer tokenizer = new JSONTokenizer();
		tokenizer.reset(reader, 0);
		System.err.println(getRegions(tokenizer));
	}

	private void assertRegions(String json, List regions) {
		JSONTokenizer tokenizer = new JSONTokenizer();
		tokenizer.reset(new java.io.StringReader(json), 0);
		System.err.println(json + "==>" + getRegions(tokenizer));
	}

	private static final List getRegions(JSONTokenizer tokenizer) {
		List tokens = new ArrayList();
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
		} catch (Exception e) {
			// Since this is convenience method and NOT the recommended
			// way of getting tokens, many errors are simply hidden
			//Logger.logException("Exception not handled retrieving regions: " + e.getLocalizedMessage(), e);//$NON-NLS-1$
		}
		return tokens;
	}

}



//import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.wst.json.core.internal.parser.JSONTokenizer;
//import org.eclipse.wst.xml.core.internal.parser.XMLTokenizer;
//import org.junit.Test;

public class JSONTokenizerTest {

	
	//@Test
	//public void testName() throws Exception {
	public static void main(String[] args) throws IOException {
			JSONTokenizer toker = new JSONTokenizer();
		//XMLTokenizer toker = new XMLTokenizer();
		//toker.setCaseSensitiveBlocking(false);
		toker.reset(new java.io.StringReader(
				"@\"a\": true}"), 0);
		// toker.beginBlockMarkerScan("script", DOMRegionContext.BLOCK_TEXT);
		System.err.println(toker.getNextToken());
	}
	
//	//@Test
//	public void fmComment() throws Exception {
//		JSONTokenizer toker = new JSONTokenizer();
//		//toker.setCaseSensitiveBlocking(false);
//		toker.reset(new java.io.StringReader(
//				"<#--<#assign >  <a></a></#assign>-->"));
//		System.err.println(toker.getRegions());		
//	}

	/*@Test
	public void fmCommentBracket() throws Exception {
		JSPTokenizer toker = new JSPTokenizer();
		toker.setCaseSensitiveBlocking(false);
		toker.reset(new java.io.StringReader(
				"[#--<#assign >  <a></a></#assign>--]"));
		System.err.println(toker.getRegions());		
	}*/
//	
//	@Test
//	public void fmList() throws Exception {
//		JSONTokenizer toker = new JSONTokenizer();
//		toker.setCaseSensitiveBlocking(false);
//		toker.reset(JSPTokenizerTest.class.getResourceAsStream("list.ftl"));
//		System.err.println(toker.getRegions());		
//	}
}

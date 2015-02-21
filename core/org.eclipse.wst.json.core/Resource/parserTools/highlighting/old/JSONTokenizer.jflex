/*nlsXXX*/
package org.eclipse.wst.json.core.internal.parser;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.json.core.internal.parser.regions.JSONTextRegionFactory;
import org.eclipse.wst.json.core.internal.regions.JSONRegionContexts;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;

%%

%public
%class JSONTokenizer
%implements JSONRegionContexts, IJSONTokenizer
%function primGetNextToken
%type String
%char
%unicode
%caseless
//%debug
%pack

%{
	private final static String UNDEFINED = "undefined";
	private String fBufferedContext = null;
	private int fBufferedStart;
//	private int fBufferedTextLength;
	private int fBufferedLength;
//	private StringBuffer fBufferedText = null;
	private JSONTextRegionFactory fRegionFactory = JSONTextRegionFactory.getInstance();
	private int fInitialState = YYINITIAL;
	public final static int BUFFER_SIZE_NORMAL = 16384;
	public final static int BUFFER_SIZE_SMALL = 256;
	private int fInitialBufferSize = BUFFER_SIZE_NORMAL;

	public void setInitialState(int state) {
		fInitialState = state;
	}
	
	public void setInitialBufferSize(int size) {
		fInitialBufferSize = size;
	}

	/* user method */
  	public final ITextRegion getNextToken() throws IOException {
		String context;
		String nextTokenType;
		boolean spaceFollows;
//		StringBuffer text;
		int start;
		int textLength;
		int length;
		if (fBufferedContext != null) {
			context = fBufferedContext;
//			text = fBufferedText;
			start = fBufferedStart;
			textLength = length = fBufferedLength;

			fBufferedContext = null;
		} else {
			context = primGetNextToken();
//			text = new StringBuffer(yytext());
			start = yychar;
			textLength = length = yylength();
		}

		if (context != null) {
			if (context == UNDEFINED) {
				// undef -> concatenate undef's
				nextTokenType = primGetNextToken();
				while (nextTokenType == UNDEFINED) {
//					text.append(yytext());
					textLength += yylength();
					length = textLength;
					nextTokenType = primGetNextToken();
				}
				fBufferedContext = nextTokenType;
//				fBufferedText = new StringBuffer(yytext());
				fBufferedStart = yychar;
				fBufferedLength = yylength();
			} else {
				nextTokenType = null;
				spaceFollows = false;
				if (JSONRegionUtil.isDeclarationValueType(context)) { // declaration value can contain VALUE_S
					nextTokenType = primGetNextToken();
					spaceFollows = (nextTokenType == JSON_DECLARATION_VALUE_S);
				} else if (canContainSpace(context)) {
					nextTokenType = primGetNextToken();
					spaceFollows = (nextTokenType == JSON_S);
				}
				if (nextTokenType != null) { // nextToken is retrieved
					if (spaceFollows && (context != JSON_COMMENT)) {
						// next is space -> append
//						text.append(yytext());
						length += yylength();
					} else {
						// next is NOT space -> push this for next time, return itself
						fBufferedContext = nextTokenType;
//						fBufferedText = new StringBuffer(yytext());
						fBufferedStart = yychar;
						fBufferedLength = yylength();
					}
				}
			}
		}

		if (context != null) {
			if (context == UNDEFINED) {
				context = JSON_UNKNOWN;
			}
			return fRegionFactory.createRegion(context, start, textLength, length);
		} else {
			return null;
		}
  	}

	/* user method */
	/* for standalone use */
  	public final List parseText() throws IOException {
  		List tokens = new ArrayList();

  		JSONTextToken token;
		for (String kind = primGetNextToken(); kind != null; kind = primGetNextToken()) {
			token = new JSONTextToken();
			token.kind = kind;  				
			token.start = yychar;
			token.length = yylength();
			token.image = yytext();
			tokens.add(token);
		}

  		return tokens;
  	}
  	
  	/* user method */
  	private boolean canContainSpace(String type) {
  		if (type == JSON_DELIMITER || type == JSON_RBRACE || type == JSON_DECLARATION_DELIMITER) {
  			return false;
  		} else {
  			return true;
  		}
  	}

	/* user method */
	public final int getOffset() {
		return yychar;
	}
	
	/* user method */
	public final boolean isEOF() {
		return zzAtEOF;
	}

	/* user method */
	public void reset(char[] charArray) {
		reset(new CharArrayReader(charArray), 0);
	}

	/* user method */
	public final void reset(java.io.Reader in, int newOffset) {
		/** the input device */
		zzReader = in;

		/** the current state of the DFA */
		zzState = 0;

		/** the current lexical state */
		zzLexicalState = fInitialState; //YYINITIAL;

		/** this buffer contains the current text to be matched and is
			the source of the yytext() string */
		if (zzBuffer.length != fInitialBufferSize) {
			zzBuffer = new char[fInitialBufferSize];
		}
		java.util.Arrays.fill(zzBuffer, (char)0);

		/** the textposition at the last accepting state */
		zzMarkedPos = 0;

		/** the textposition at the last state to be included in yytext */
//		yy_pushbackPos = 0;

		/** the current text position in the buffer */
		zzCurrentPos = 0;

		/** startRead marks the beginning of the yytext() string in the buffer */
		zzStartRead = 0;

		/** endRead marks the last character in the buffer, that has been read
			from input */
		zzEndRead = 0;

		/** number of newlines encountered up to the start of the matched text */
		yyline = 0;

		/** the number of characters up to the start of the matched text */
		yychar = 0;

		/**
		 * the number of characters from the last newline up to the start of the 
		 * matched text
		 */
//		yycolumn = 0; 

		/** 
		 * yy_atBOL == true <=> the scanner is currently at the beginning of a line
		 */
//		yy_atBOL = false;
		
		/** zzAtEOF == true <=> the scanner has returned a value for EOF */
		zzAtEOF = false;

		/* user variables */
		//		fUndefined.delete(0, fUndefined.length());
	}

	/* user method */
	public JSONTokenizer() {
		super();
	}

	/**
	 * Added to workaround stricter compilation options without creating
	 * an alternate skeleton file
	 */
	void _usePrivates() {
		System.out.print(yycolumn);
		System.out.print(yyline);
		System.out.print(Boolean.toString(zzAtBOL));
	}
%}

%state STRING
WHITE_SPACE_CHAR=[\n\r\ \t\b\012]
NUMBER_TEXT=-?(0|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)?
%%
<YYINITIAL> {
  "," { return (new JsonToken(JsonToken.COMMA, yytext(), yyline, yychar, yychar+1)); }
  ":" { return (new JsonToken(JsonToken.COLON, yytext(), yyline, yychar, yychar+1)); }
  "[" { return (new JsonToken(JsonToken.START_ARRAY, yytext(), yyline, yychar, yychar+1)); }
  "]" { return (new JsonToken(JsonToken.END_ARRAY, yytext(), yyline, yychar, yychar+1)); }
  "{" { return (new JsonToken(JsonToken.START_OBJECT, yytext(), yyline, yychar, yychar+1)); }
  "}" { return (new JsonToken(JsonToken.END_OBJECT, yytext(), yyline, yychar, yychar+1)); }
  "true" { return (new JsonToken(JsonToken.TRUE, yytext(), yyline, yychar, yychar+yylength()));}
  "false" { return (new JsonToken(JsonToken.FALSE, yytext(), yyline, yychar, yychar+yylength()));}
  "null" { return (new JsonToken(JsonToken.NULL, yytext(), yyline, yychar, yychar+yylength()));}
  \"  { string.setLength(0); yybegin(STRING); }
  {NUMBER_TEXT} { return (new JsonToken(JsonToken.NUMBER, yytext(), yyline, yychar, yychar+yylength()));} 
  {WHITE_SPACE_CHAR} {}
}

<STRING> {
 \"  { yybegin(YYINITIAL); 
       return (new JsonToken(JsonToken.STRING, string.toString(), yyline, yychar, yychar+string.length()));}
 [^\n\r\"\\]+   { string.append(yytext()); }
  \\\"          { string.append('\"'); }
  \\\\          { string.append('\\'); }
  \\\/           { string.append('/'); }
  \\b          { string.append('\b'); }
  \\f          { string.append('\f'); }
  \\n           { string.append('\n'); }
  \\r           { string.append('\r'); }
  \\t          { string.append('\t'); }
  \\u[0-9A-Fa-f]{4}    { string.append(Character.toChars(Integer.parseInt(yytext().substring(2),16))); }
}
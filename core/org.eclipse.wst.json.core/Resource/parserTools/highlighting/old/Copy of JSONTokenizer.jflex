/*nlsXXX*/
package org.eclipse.wst.json.core.internal.parser;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.json.core.internal.parser.regions.JSONTextRegionFactory;
import org.eclipse.wst.json.core.internal.parserz.JSONRegionContexts;
import org.eclipse.wst.json.core.internal.parserz.JSONTextToken;
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

%state ST_CHARSET_NAME
%state ST_CHARSET_DELIMITER
%state ST_IMPORT_URI
%state ST_IMPORT_MEDIUM
%state ST_IMPORT_DELIMITER
%state ST_MEDIA_MEDIUM
%state ST_MEDIA_DELIMITER
%state ST_PAGE_PSEUDO_PAGE
%state ST_PAGE_DELIMITER
%state ST_FONT_FACE_DELIMITER
%state ST_SELECTOR
%state ST_SELECTOR_MODIFIER
%state ST_SELECTOR_ATTRIBUTE_NAME
%state ST_SELECTOR_ATTRIBUTE_OPERATOR
%state ST_SELECTOR_ATTRIBUTE_VALUE
%state ST_SELECTOR_ATTRIBUTE_END
%state ST_DECLARATION
%state ST_DECLARATION_SEPARATOR
%state ST_DECLARATION_PRE_VALUE
%state ST_DECLARATION_VALUE

h = [0-9a-f]
nonascii = [\u0080-\uffff]
unicode = \\{h}{1,6}[ \t\r\n\f]?
escape = {unicode}|\\[ -~\u0080-\uffff]



nmstart = [_a-zA-Z]|{nonascii}|{escape}
nmchar = [_a-zA-Z0-9-]|{nonascii}|{escape}
string1 = \"([\t !#$%&(-~]|\\{nl}|\'|{nonascii}|{escape})*\"
string2 = \'([\t !#$%&(-~]|\\{nl}|\"|{nonascii}|{escape})*\'

ident = -?{nmstart}{nmchar}*
value_ident = -?{nmstart}"."?({nmchar}+"."?)*

name = {nmchar}+
num = [+-]?([0-9]+|[0-9]*"."[0-9]*)
string = {string1}|{string2}
url = ([ !#$%&*-~]|{nonascii}|{escape})*
s = [ \t\r\n\f]
w = {s}*
nl = \n|\r\n|\r|\f

//range = \?{1,6}|{h}(\?{0,5}|{h}(\?{0,4}|{h}(\?{0,3}|{h}(\?{0,2}|{h}(\??|{h})))))

hash = "#"{name}
uri = ("url("{w}{string}{w}")"|"url("{w}{url}{w}")")
function = {ident}"("
unicode_range = "U"\+[0-9a-fA-F?]{1,6}("-"[0-9a-fA-F?]{1,6})?

%%

/*
 * *** global ***
 */

{s}+ { return JSON_S; }
"<!--" { return JSON_CDO; }
"-->" { return JSON_CDC; }
"}" { yybegin(YYINITIAL); return JSON_RBRACE; }
\/\*[^*]*\*+([^/*][^*]*\*+)*\/ { return JSON_COMMENT; }

//<YYINITIAL> {
//	"@import" {	yybegin(ST_IMPORT_URI); return JSON_IMPORT; }
//}

/*
 * *** charset rule ***
 * CHARSET_SYM S* STRING S* ';'
 */

"@charset" { yybegin(ST_CHARSET_NAME); return JSON_CHARSET; }

<ST_CHARSET_NAME> {
	{string} { yybegin(ST_CHARSET_DELIMITER); return JSON_STRING; }
}

<ST_CHARSET_DELIMITER> {
	";" { yybegin(YYINITIAL); return JSON_DELIMITER; }
}

/*
 * *** import rule ***
 * IMPORT_SYM S* [STRING|URI] S* [ medium [ COMMA S* medium]* ]? ';' S*
 */

"@import" { yybegin(ST_IMPORT_URI); return JSON_IMPORT; }

<ST_IMPORT_URI> {
	{string} { yybegin(ST_IMPORT_MEDIUM); return JSON_STRING; }
	//	"url("{w}{string}{w}")" { yybegin(ST_IMPORT_MEDIUM); return JSON_URI; }
	//	"url("{w}{url}{w}")" { yybegin(ST_IMPORT_MEDIUM); return JSON_URI; }
	{uri} { yybegin(ST_IMPORT_MEDIUM); return JSON_URI; }
	";" { yybegin(YYINITIAL); return JSON_DELIMITER; }
}

<ST_IMPORT_MEDIUM> {
	{ident} { yybegin(ST_IMPORT_DELIMITER); return JSON_MEDIUM; }
	";" { yybegin(YYINITIAL); return JSON_DELIMITER; }
}

<ST_IMPORT_DELIMITER> {
	";" { yybegin(YYINITIAL); return JSON_DELIMITER; }
	"," { yybegin(ST_IMPORT_MEDIUM); return JSON_MEDIA_SEPARATOR; }
}

/*
 * *** media rule ***
 * MEDIA_SYM S* medium [ COMMA S* medium ]* LBRACE S* ruleset* '}' S*
 */

"@media" { yybegin(ST_MEDIA_MEDIUM); return JSON_MEDIA; }

/* 
 * medium
 * IDENT S*
 */
<ST_MEDIA_MEDIUM> {
	{ident} { yybegin(ST_MEDIA_DELIMITER); return JSON_MEDIUM; }
	"{" { yybegin(YYINITIAL); return JSON_LBRACE; }
}

<ST_MEDIA_DELIMITER> {
	"{" { yybegin(YYINITIAL); return JSON_LBRACE; }
	"," { yybegin(ST_MEDIA_MEDIUM); return JSON_MEDIA_SEPARATOR; }
}

/*
 * *** page rule **
 * PAGE_SYM S* pseudo_page? S* LBRACE S* declaration [ ';' S* declaration ]* '}' S*
 */

"@page" { yybegin(ST_PAGE_PSEUDO_PAGE); return JSON_PAGE; }
 
/*
 * pseudo_page
 * ':' IDENT
 */

<ST_PAGE_PSEUDO_PAGE> {
	":"?{ident} { yybegin(ST_PAGE_DELIMITER); return JSON_PAGE_SELECTOR; }
	"{" { yybegin(ST_DECLARATION); return JSON_LBRACE; }
}

<ST_PAGE_DELIMITER> {
	"{" { yybegin(ST_DECLARATION); return JSON_LBRACE; }
}

/*
 * font-face
 * FONT_FACE_SYM S* '{' S* declaration [ ';' S* declaration '* '}' S*
 */

"@font-face" { yybegin(ST_FONT_FACE_DELIMITER); return JSON_FONT_FACE; }

<ST_FONT_FACE_DELIMITER> {
	"{" { yybegin(ST_DECLARATION); return JSON_LBRACE; }
}

/*
 * selector
 * simple_selector [ combinator simple_selector ]*
 */

/*
 * simple_selector
 * element_name [ HASH | class | attrib | pseudo ]* | [ HASH | class | attrib | pseudo ]+
 */

<YYINITIAL, ST_SELECTOR_MODIFIER, ST_SELECTOR> {
	"*" { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_UNIVERSAL; }
	{hash} { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_ID; }
//	":"{ident}("("{s}*{ident}{s}*")")? { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_PSEUDO; }
	":"({ident}("("{s}*([a-zA-Z0-9]|[-+]|{s})*{s}*")")?)? { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_PSEUDO; }
	"."{name} { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_CLASS; }
	"[" { yybegin(ST_SELECTOR_ATTRIBUTE_NAME); return JSON_SELECTOR_ATTRIBUTE_START; }
}

<YYINITIAL, ST_SELECTOR> {
	{ident} { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_ELEMENT_NAME; }
}

<ST_SELECTOR_MODIFIER> {
	"," { yybegin(ST_SELECTOR); return JSON_SELECTOR_SEPARATOR; }
	// using LOOKAHEAD
	{s}+/[^+>\{,/] { yybegin(ST_SELECTOR); return JSON_SELECTOR_COMBINATOR; }
	"+"|">" { yybegin(ST_SELECTOR); return JSON_SELECTOR_COMBINATOR; }
	"{" { yybegin(ST_DECLARATION); return JSON_LBRACE; }
}

/*
 * attrib
 * '[' S* IDENT S* [ [ '=' | INCLUDES | DASHMATCH ] S* [ IDENT | STRING ] S* ]? ']'
 */

<ST_SELECTOR_ATTRIBUTE_NAME> {
	{ident} { yybegin(ST_SELECTOR_ATTRIBUTE_OPERATOR); return JSON_SELECTOR_ATTRIBUTE_NAME; }
	"]" { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_ATTRIBUTE_END; }
}

<ST_SELECTOR_ATTRIBUTE_OPERATOR> {
	"="|"~="|"|="|"*="|"$="|"^=" { yybegin(ST_SELECTOR_ATTRIBUTE_VALUE); return JSON_SELECTOR_ATTRIBUTE_OPERATOR; }
	"]" { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_ATTRIBUTE_END; }
}

<ST_SELECTOR_ATTRIBUTE_VALUE> {
	{ident}|{string} { yybegin(ST_SELECTOR_ATTRIBUTE_END); return JSON_SELECTOR_ATTRIBUTE_VALUE; }
}

<ST_SELECTOR_ATTRIBUTE_END> {
	"]" { yybegin(ST_SELECTOR_MODIFIER); return JSON_SELECTOR_ATTRIBUTE_END; }
}

/*
 * declaration
 * property ':' S* expr prio? | // empty //
 */

<ST_DECLARATION> {
	\x2A?{ident} { yybegin(ST_DECLARATION_SEPARATOR); return JSON_DECLARATION_PROPERTY; }
}

<ST_DECLARATION_SEPARATOR> {
	":" { yybegin(ST_DECLARATION_PRE_VALUE); return JSON_DECLARATION_SEPARATOR; }
}

<ST_DECLARATION_PRE_VALUE, ST_DECLARATION_VALUE> {
	"!"{s}*"important" { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_IMPORTANT; }
	
	
	")" { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_PARENTHESIS_CLOSE; }
	
	// ordered following two rules deliberately, see 
	//  https://bugs.eclipse.org/bugs/show_bug.cgi?id=129902
	{num}{ident} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_DIMENSION; }
	{value_ident} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_IDENT; }


	{num}"%" { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_PERCENTAGE; }
		
	{num} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_NUMBER; }
	
	
	
	{function} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_FUNCTION; }
	{string} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_STRING; }
	{uri} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_URI; }
	"#"{name} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_HASH; }
	{unicode_range} { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_UNICODE_RANGE; }
	[,/] { yybegin(ST_DECLARATION_VALUE); return JSON_DECLARATION_VALUE_OPERATOR; }
}

<ST_DECLARATION_VALUE> {
	{s}+/[^;}] { return JSON_DECLARATION_VALUE_S; }
}

<ST_DECLARATION, ST_DECLARATION_SEPARATOR, ST_DECLARATION_PRE_VALUE, ST_DECLARATION_VALUE> {
	";" { yybegin(ST_DECLARATION); return JSON_DECLARATION_DELIMITER; }
	//	"}" { yybegin(YYINITIAL); return JSON_RBRACE; }
}


//<YYINITIAL, ST_IMPORT_URI, ST_IMPORT_MEDIUM, ST_IMPORT_DELIMITER> {
//	\/\*[^*]*\*+([^/*][^*]*\*+)*\/ { return JSON_COMMENT; }
//	{s}+ { return JSON_S; }
//	. { return UNDEFINED; }
//}

//<YYINITIAL, ST_IMPORT_URI, ST_IMPORT_MEDIUM, ST_IMPORT_DELIMITER> {
//	[^ \t\r\n\f]+ { return JSON_UNKNOWN; }
//}

. {
	return UNDEFINED;
}

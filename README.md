# Eclipse WTP/JSON

[![Build Status](https://secure.travis-ci.org/angelozerr/eclipse-wtp-json.png)](http://travis-ci.org/angelozerr/eclipse-wtp-json)

Goal of Eclipse WTP JSON is to create an advanced JSON Editor based on WTP, which is extensible and contribute if WTP Team are interested. WTP JSON Editor will provide extension point to customize the editor with custom, compeltion, validatioon, hyperlink and text hover to provide same features than [JetBrains & PHP](http://blog.jetbrains.com/phpstorm/2015/01/working-with-composer-json-in-phpstorm/) for instance.

Eclipse WTP JSON provides a JSON Editor based on Eclipse WTP (SSE):

![JSON Editor](https://github.com/angelozerr/eclipse-wtp-json/wiki/images/JSONEditorOverview.png)  

# Features

Today this project is a POC, but her goal is to provide a WTP JSON Editor with the following commons features :

- Color text highlighting : **DONE**
- An Outline Tree view : **TODO!**
- JSON validation based on WTP Validator : **TODO!**
- Text formatting : **TODO!**
- Text folding on JSON Objects and Arrays : **TODO!**

And provides advanced features like : 
 
 - Extends the editor with custom completion: **TODO!** (completion which follows a JSON Schema, whith files for bower.json, etc)
 - Extends the editor with custom hyperlink (hyperlink to open a file from a JSON string value).
 - Extends the editor with custom validation (validate the existing of declared file from a JSON string value).
 - Extends the editor with custom hover (text hover displays information about the JSON string value which decalred a file (file path, name, etc)).

# Why another JSON Editor?

See [tern.java issue 253](https://github.com/angelozerr/tern.java/issues/253).

It exists several JSON Editor :

 - http://marketplace.eclipse.org/content/json-editor-plugin
 - http://marketplace.eclipse.org/content/json-tools (see sources at https://bitbucket.org/denmiroch/jsontools/src/d332043d57dbd0bb18ae26098fe576369740d521?at=default)
 - https://github.com/skoptelov/jsonclipse
 - https://github.com/xcoulon/jbosstools-jsoneditor

But there are some limitations : 

 - not possible to update the JSON Editor with an JSON model kind like WTP IDOMModel and ICSSModel.
 - not extensible to extends completion and validation

# Architecture 

WTP JSON Editor It follows the same architecture than XML, CSS, DTD, WTP Editors (org.eclipse.wst.sse.ui.StructuredTextEditor) 

 * JSON Tokenizer uses JFlex lexer based on [JSONTokenizer.jflex](https://github.com/angelozerr/eclipse-wtp-json/blob/master/core/org.eclipse.wst.json.core/Resource/parserTools/highlighting/JSONTokenizer.jflex). You can find test at [JSONTokenizerTest.java]( https://github.com/angelozerr/eclipse-wtp-json/blob/master/core/org.eclipse.wst.json.core.tests/src/org/eclipse/wst/json/core/internal/parser/JSONTokenizerTest.java)
 
 

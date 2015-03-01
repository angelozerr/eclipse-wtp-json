# Eclipse WTP/JSON

[![Build Status](https://secure.travis-ci.org/angelozerr/eclipse-wtp-json.png)](http://travis-ci.org/angelozerr/eclipse-wtp-json)

Goal of Eclipse WTP JSON is to create an advanced JSON Editor based on WTP, which is extensible and contribute if WTP Team are interested. WTP JSON Editor will provide extension points to customize the editor with custom, completion, validation, hyperlink and text hover to provide same features than [Working with composer.json in PhpStorm](http://blog.jetbrains.com/phpstorm/2015/01/working-with-composer-json-in-phpstorm/) for instance.

Those extension can be usefull to provide custom editor for `bower.json`, `.jshintrc`, etc.

Eclipse WTP JSON provides a JSON Editor based on Eclipse WTP (SSE):

![JSON Editor](https://github.com/angelozerr/eclipse-wtp-json/wiki/images/JSONEditorOverview.png)  

# Features

Today this project is a POC, but her goal is to provide a WTP JSON Editor with the following commons features :

- [Syntax Coloring](https://github.com/angelozerr/eclipse-wtp-json/wiki/SyntaxColoring) which can be customized with preferences.
- An Outline Tree view : **TODO!**
- JSON validation based on WTP Validator : **TODO!**
- Text formatting : **TODO!**
- [Text folding](https://github.com/angelozerr/eclipse-wtp-json/wiki/TextFolding) on JSON Objects and Arrays.

And provides advanced features like : 
 
 - Extends the editor with custom completion: **TODO!** (completion which follows a JSON Schema, whith files for bower.json, etc)
 - Extends the editor with custom hyperlink (hyperlink to open a file from a JSON string value).
 - Extends the editor with custom validation (validate the existing of declared file from a JSON string value).
 - Extends the editor with custom hover (text hover displays information about the JSON string value which decalred a file (file path, name, etc)).

# Why another JSON Editor?

See [tern.java issue 253](https://github.com/angelozerr/tern.java/issues/253).

There exist several JSON Editors :

 - http://marketplace.eclipse.org/content/json-editor-plugin
 - http://marketplace.eclipse.org/content/json-tools (see sources at https://bitbucket.org/denmiroch/jsontools/src/)
 - https://github.com/skoptelov/jsonclipse
 - https://github.com/xcoulon/jbosstools-jsoneditor

But there are some limitations : 

 - not possible to update the JSON Editor with an JSON model kind like WTP IDOMModel and ICSSModel.
 - not extensible to extends completion and validation

# Installation

Eclipse WTP Web JSON is developed/tested with Eclipse 4.4 Luna. It is advised to use Eclipse 4.4 Luna (even if it could work with older version of Eclipse).

To install Eclipse WTP JSON, please read [Installation - Update Site](https://github.com/angelozerr/eclipse-wtp-json/wiki/InstallationUpdateSite) section.
 
# Build

Eclipse WTP Web Resourcese is build with this [cloudbees job](https://opensagres.ci.cloudbees.com/job/eclipse-wtp-json/).

# Architecture 

WTP JSON Editor It follows the same architecture than XML, CSS, DTD, WTP Editors (org.eclipse.wst.sse.ui.StructuredTextEditor) 

 * JSON Tokenizer uses JFlex lexer based on [JSONTokenizer.jflex](https://github.com/angelozerr/eclipse-wtp-json/blob/master/core/org.eclipse.wst.json.core/Resource/parserTools/highlighting/JSONTokenizer.jflex). You can find test at [JSONTokenizerTest.java]( https://github.com/angelozerr/eclipse-wtp-json/blob/master/core/org.eclipse.wst.json.core.tests/src/org/eclipse/wst/json/core/internal/parser/JSONTokenizerTest.java)
 
 

# L language compiler
L language compiler for Compilers course at Saint-Petersburg Academic University.

L is a simple object-oriented language with Scala-like syntax.

Code examples could be found in the `resources` folder.
## Building from sources
1. Clone the repository:
```
$ git clone https://github.com/adkozlov/compilers-2015
```

2. Open the project in [IntelliJ IDEA](https://www.jetbrains.com/idea/).

2. Build the ANLTR grammar:
  * if you have the [ANTLR4 plugin](https://plugins.jetbrains.com/plugin/7358?pr=) for IntelliJ IDEA already installed just open the grammar file (`LLang.g4`) and press `Ctrl+Shift+G / Shift+Command+G`;
  * if you have not yet installed the plugin:
```
$ cd build
$ chmod +x build_grammar.sh
$ ./build_grammar.sh
```
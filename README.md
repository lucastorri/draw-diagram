# Draw Diagram

Generate [Syntax Diagrams](https://en.wikipedia.org/wiki/Syntax_diagram) from simplified [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form) rules.

**Disclaimer**: this project is full of bugs!


## Rule format

Rules are composed of 3 parts, each separated by tabs (`\t`). They look something like this: `$LABEL\t$NAME\t$RULE`.

The first part and second are just used to identify the rule. Labels and names can be repeated, and they'll be merged together. Rules are the final part, and they also have their own format. Expected tokens are just regular words, but they can be made optional or part of a choice group. Optional tokens are enclosed in bracket (`[]`), and choices are inside parentheses (`()`) and with pipes (`|`) in between.

Words enclosed by less-than and greater-than signs (`<>`) will be drawn inside square boxes.

For example, a valid rule would be:

```
BLAH	BLEH	[please] (what is|what's) your <name>
```

This rule will generate the following diagram:

![Example's Syntax Diagram](docs/example.png)

Combinations of modifiers can be used inside each other. For instance, a choice could be made optional with `[(a|b|c)]`.


## Installation

You need to generate the last version of the script used to interpret the rules. Once inside the project directory, you can do it by executing:

```bash
./sbt fastOptJS
```


## Run

After generating the required script (see previous section), just open `index.html`. There you can paste your rules and the appropriate diagrams will be generated and displayed on the page.


## How it Works

A given input is broken down by lines and split by tabs. The last part of the split, the one actually containing the rule, is tokenized and then parsed into an intermediate tree representation, using immutable Scala case classes. All these classes extend the *Part* trait. This tree is built with the help of a stack on the *Parser* object.

Later on, this intermediate representation can be serialized to the format used by *railroad-diagrams* through the `toDiagram` method.

The Scala code is converted to Javascript with *Scala.js*, and therefore can be used on the `index.html` page, which takes of care of taking the user input, passing it to the parser, and finally pipe the output to *railroad-diagrams*. The exposed Javascript API for the Scala code is available on the *Main* object.

When debugging, the *Debug* object offers a function that prints the generated intermediate tree.


## Acknowledgments

This project wouldn't be possible without:

  * [railroad-diagrams](https://github.com/tabatkins/railroad-diagrams)
  * [Scala.js](https://www.scala-js.org/)

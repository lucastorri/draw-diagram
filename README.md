# Draw Diagram

Generate [Syntax Diagrams](https://en.wikipedia.org/wiki/Syntax_diagram) from simplified [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form) rules.


## Installation

You need to generate the last version of the script used to interpret the rules. Once inside the project directory, you can do it by executing:

```bash
./sbt fastOptJS
```


## Run

After generating the required script (see previous section), just open `index.html`. There you can paste your rules and the appropriate diagrams will be generated and displayed on the page.


## Acknowledgments

This project wouldn't be possible without [railroad-diagrams](https://github.com/tabatkins/railroad-diagrams) by [`tabaktins`](https://github.com/tabatkins).

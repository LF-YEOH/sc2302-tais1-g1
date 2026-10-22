# Design Decisions

## D1: Plain javac with build scripts instead of Maven/Gradle
Context:  The project needs a build-and-run process that markers can
          reproduce exactly from the submitted files, with no live demo.
Options:  (a) Maven or Gradle project
          (b) Plain javac, driven by small build scripts
              (build.bat for Windows, build.sh for Mac/Linux)
Chose:    (b)
Because:  - Markers only need a JDK installed; no extra tool to install,
            no network access needed to download plugins (the brief
            requires all features to work offline).
          - The game has no external libraries, so dependency
            management, Maven's main benefit, gives us nothing.
          - One command compiles everything and runs the game, which
            keeps the README's run instructions short and reliable.
Cost:     - No built-in test runner, so we can't easily use JUnit;
            we use replay files of full games as tests instead.
          - Two scripts to maintain (Windows and Mac) instead of one
            portable build file.
          - A larger project would outgrow this; for ~20 classes it
            is enough.

## D2: MenuItem as a single concrete class in Stage 1
Context:  Stage 1 has three menu items that differ only in their data
          (name, price, preparation units).
Options:  (a) One concrete MenuItem class; Salad/Burger/Pasta are objects
          (b) Abstract MenuItem with a subclass per dish
          (c) A MenuItem interface
Chose:    (a)
Because:  - The items differ only in data, not behaviour, so subclasses
            or an interface would add classes with nothing to override.
          - The course advises abstractions only where substitution is
            meaningful, and Stage 1 must not anticipate later stages.
          - Menu creates and owns its items (composition), so the whole
            menu is defined in one place.
Cost:     If a later requirement needs items that behave differently,
          MenuItem's design will have to be revisited at that point.

## D3: Money stored as integer cents
Context:  The brief requires exact money amounts displayed with two
          decimals.
Options:  (a) double dollars   (b) int cents
Chose:    (b)
Because:  double cannot represent many decimal values exactly, so sums
          can be off by a cent; int cents is exact. Money.format is the
          one place that converts cents to "$x.xx".
Cost:     Every display must go through Money.format.
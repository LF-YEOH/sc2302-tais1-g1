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
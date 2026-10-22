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

## D2: MenuItem as an interface, not an abstract class
Context:  Dish (S1) and ComboMeal (S3) must both be orderable items
          with a name, price and preparation units.
Options:  (a) Abstract class MenuItem holding name/price/units fields
          (b) Interface MenuItem declaring the three getters
Chose:    (b)
Because:  - Dish stores its values; ComboMeal derives them from its
            components. Shared fields would duplicate derived data
            in ComboMeal and could drift out of sync.
          - ComboMeal can hold two MenuItems and itself be a MenuItem
            (Composite pattern), so Order, Chef and Waiter treat
            combos and dishes identically (polymorphism).
          - Stage 3 adds ComboMeal without modifying MenuItem or
            Dish (OCP).
Cost:     No place for shared implementation; if items later share
          behaviour it must be repeated or put in a default method.

## D3: Money as integer cents
Context:  Prices are discounted (VIP 10%, Happy Hour 20%, combos 10%)
          and must be displayed exactly.
Options:  (a) double dollars
          (b) BigDecimal
          (c) int cents with one rounding helper
Chose:    (c)
Because:  - double cannot represent many decimal values exactly, so
            stacked discounts drift by a cent.
          - BigDecimal is exact but verbose for amounts this small.
          - int cents is exact for storage; Money.applyPercent rounds
            to the nearest cent in exactly one place.
Cost:     Every display must go through Money.format; forgetting
          shows "1830" instead of "$18.30".
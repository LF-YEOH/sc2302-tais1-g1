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

## D4: Boundary-Control-Entity structure; the controller drives the turn
Context:  The brief requires game rules to stay out of the UI. Someone
          must run the six turn steps in the right order.
Options:  (a) RestaurantUI runs the turn loop and calls the controller
              step by step
          (b) GameController runs the turn loop and asks RestaurantUI
              only for the manager's decisions and to display state
Chose:    (b)
Because:  - The order of the six steps is itself a game rule (Waiter
            before Chef, payment before eating). In (a) that order would
            live in the UI.
          - RestaurantUI is left with one reason to change: how things
            are shown and typed (SRP).
          - Follows the course's BCE model: boundary for input/output,
            control to coordinate and own the collections, entities for
            data and their own rules.
Cost:     GameController depends on RestaurantUI, so it cannot run
          without a UI object (a replay file can stand in for a person).

## D5: Invalid actions refused with a checked InvalidActionException
Context:  An invalid choice must explain why it failed and let the
          manager choose again without using up the staff task.
Options:  (a) Staff methods return true/false or an error string
          (b) Staff methods throw a custom checked exception carrying
              the reason, before changing any state
Chose:    (b)
Because:  - The check and the reason live in the staff class that owns
            the rule; the controller cannot forget to check, because
            the compiler forces it to catch the exception.
          - Nothing changes when an action is refused, so retrying is
            safe and the task is not used up.
Cost:     Callers need try/catch blocks; one extra class.

## D6: Customers own their state changes and their food choice
Context:  A customer moves WAITING -> SEATED -> ORDERED -> EATING ->
          READY_TO_PAY -> PAID, or LEFT. The brief says customers
          choose their own food.
Options:  (a) Staff and controller set the customer's fields directly
          (b) Customer exposes methods (sitAt, placeOrder, receiveFood,
              finishEating, pay, leave, chooseItem) that update its own
              state; staff check the rules and call them
Chose:    (b)
Because:  - Encapsulation: a customer's state can only change through
            its own methods, so it can never be left inconsistent
            (e.g. leaving always cancels the order and frees the table).
          - The food-choice rule sits with the customer, as in the brief.
Cost:     Customer is the largest entity class.

## D7: Separate status enums for Customer and Order
Context:  The brief describes both customer states (READY_TO_PAY, PAID)
          and order states (READY, SERVED, PAID).
Options:  (a) One status shared by both   (b) CustomerStatus and
          OrderStatus
Chose:    (b)
Because:  They are different lifecycles: an order is READY while its
          customer is still waiting; a customer can be SEATED with no
          order at all.
Cost:     Two enums to keep in step (e.g. paying marks both PAID).

## D8: Scoreboard entity for results and the victory check
Context:  The game must show revenue, average satisfaction, served count
          and departures every turn, and decide victory after turn 18.
Options:  (a) Counters as fields of GameController
          (b) A Scoreboard entity
Chose:    (b)
Because:  The victory targets and the average calculation change for
          different reasons than the turn flow (SRP), and the UI can be
          handed one object to display.
Cost:     One more class.

## D9: No common Staff superclass
Context:  Waiter, Chef and Cashier are all staff.
Options:  (a) Abstract Staff class or Staff interface   (b) Three
          independent classes
Chose:    (b)
Because:  They share no operations or data: seat/takeOrder/serve, cook
          and collectPayment are unrelated. A shared type would force
          methods some staff cannot perform (ISP) and nothing in the
          game needs to treat staff interchangeably.
Cost:     If a shared staff behaviour appears later, a common type would
          have to be introduced then.
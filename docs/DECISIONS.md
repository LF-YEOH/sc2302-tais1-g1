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

## D10: Victory target checks live only in Scoreboard
Context:  The result screen shows whether each of the three targets was
          met. The first UI version kept its own copies of the targets and
          re-checked them, so a game rule lived in the UI (brief 2.2:
          "Keep rules out of the UI").
Options:  (a) UI keeps copies of the targets and checks them itself
          (b) Scoreboard checks each target; the UI only asks and displays
Chose:    (b)
Because:  - Each target rule now has one owner (SRP); isVictory() combines
            isPaidTargetMet(), isRevenueTargetMet() and
            isSatisfactionTargetMet().
          - A changed target is updated in one place, not two that could
            drift apart.
          - The targets and MAX_TURNS are public constants, so the UI can
            display them without duplicating them.
Cost:     The UI depends on Scoreboard's and GameController's constants.
          Found in code review of the UI pull request and refactored;
          both replays print identical output before and after.

## D11: How ambiguous parts of the brief were read
Context:  The brief leaves some details open.
Chose:    - Arrivals happen before the start-of-turn display, so the
            manager sees the new customer when choosing (3.1 asks to show
            the queue and the tasks available).
          - The Waiter may seat any queued customer, not only the first:
            4.1 says the player sets seating priority.
          - The Waiter uses the lowest-numbered free table: tables are
            identical, so choosing one would add input but no decision.
          - Food choice: customer n orders menu item (n - 1) % 3, i.e.
            C1 Salad, C2 Burger, C3 Pasta, ... (2.2: documented
            deterministic rule; keeps runs reproducible).
          - Input is checked in two layers: the UI rejects non-numbers and
            out-of-range values (format); staff reject choices that break
            game rules (with a reason).
Because:  Each choice gives the player a decision only where it can change
          the outcome, and keeps every run reproducible.
Cost:     Other groups may read the brief differently; our readings are
          documented here and in design-stage1.md.

## D12: Replays through a plain Scanner instead of an input interface
Context:  The brief asks for reproducible victory and defeat runs (4.1)
          and allows an accelerated replay in the video (6.2).
Options:  (a) An InputSource interface with keyboard and file versions
          (b) Main builds a Scanner from System.in or from a file and
              passes it to RestaurantUI
Chose:    (b)
Because:  Scanner already reads from both sources, so an extra interface
          would add a class without real substitution. Only Main knows the
          source; the UI just echoes lines when reading a file, and no
          game logic changed to support replays.
Cost:     Replay files hold bare numbers with no comments, so they are
          hard to read on their own; their turns are described in the
          test notes and video script.

## D13: The waiting queue is an ArrayList, not a Queue
Context:  Customers wait in arrival order before being seated.
Options:  (a) java.util.Queue (first in, first out)
          (b) ArrayList kept in arrival order
Chose:    (b)
Because:  Customers leave the line from anywhere: the manager may seat any
          of them (D11), and impatient customers leave from any position.
          A Queue is meant for taking from the front only. ArrayList is
          also within the course material; Queue/LinkedList are not.
Cost:     Nothing enforces arrival order except adding to the end.

## D14: Table.seat() sets both sides of the table-customer link
Context:  A table knows its customer and a customer knows its table; the
          two must always agree.
Options:  (a) Callers set table.occupant and customer.table separately
          (b) Table.seat(customer) sets its occupant and calls
              customer.sitAt(this); Customer.pay() and leave() clear the
              table and forget it
Chose:    (b)
Because:  The two-way association is updated in one method, so no caller
          can set one side and forget the other (encapsulation).
Cost:     Table and Customer each know about the other (a bidirectional
          association), which slightly increases coupling.

## D15: VIP and Critic extend Customer, which stays the Regular customer
Context:  Stage 2 adds VIP and Critic customers (brief 4.2). They follow
          the same order and service flow as Regular customers but behave
          differently in places.
Options:  (a) VIPCustomer and CriticCustomer extend the existing Customer,
              which remains the Regular customer
          (b) Make Customer abstract and add RegularCustomer alongside
              VIPCustomer and CriticCustomer
Chose:    (a)
Because:  - Customer is already a complete, working Regular customer; an
            abstract parent with an empty RegularCustomer would add a class
            with nothing in it.
          - Keeps the change small: existing code that creates or uses
            Customer is unaffected, and Regular behaviour is preserved
            (the revision slides' "minimal modification").
          - VIP and Critic are genuinely "a kind of customer" with different
            behaviour, so inheritance fits (unlike Stage 1's menu items,
            which differed only in values).
Cost:     Customer means both "any customer" and "a Regular customer"; a
          change meant only for Regular customers would also reach VIPs and
          Critics unless they override it.

## D16: Customer differences are overridden methods, never type checks
Context:  Brief 4.2: "Different behaviour must be dispatched through the
          customer objects, not through a chain of type checks in the turn
          controller."
Options:  (a) Check the type (instanceof) wherever behaviour differs
          (b) Customer defines overridable methods; each subclass overrides
              only what differs
Chose:    (b)
Because:  - getSatisfactionLoss() (8 / VIP 5 / Critic 12),
            getDiscountedPrice() (VIP 90%), reactToService() (Critic) and
            toString() (e.g. "C2 [Critic]") are chosen at run time by dynamic
            binding (polymorphism). GameController, staff and UI never check
            a customer's type, so a new customer type needs no change to them
            (OCP).
          - Values become methods because fields cannot be overridden: a
            subclass cannot replace Customer's SATISFACTION_LOSS constant.
          - Satisfaction stays private; subclasses change it only through
            protected adjustSatisfaction(), which enforces the 0-100 limits
            in one place (encapsulation).
Cost:     Customer gains four small methods that Regular customers barely use.

## D17: The Critic's reaction is a hook in the parent's pay() contract
Context:  Our Critic behaviour (brief 4.2 lets us define it): when paying,
          satisfaction of 50 or more gains 20 (good review, max 100); below
          50 loses 20 (bad review, min 0). Combined with losing 12 per
          waiting turn, it rewards fast service and punishes slow service.
Options:  (a) CriticCustomer overrides pay() and changes satisfaction there
          (b) Customer.pay() calls reactToService() first (does nothing for
              Regular and VIP); CriticCustomer overrides reactToService()
Chose:    (b)
Because:  Customer.pay()'s Stage 1 contract promised "satisfaction is not
          changed". Overriding pay() to change it would break the parent's
          promise to its callers, violating LSP (a subtype must keep the
          parent's contract). Instead the parent's contract was updated for
          every customer: pay() may adjust satisfaction through
          reactToService() before it is recorded. Every subtype then keeps
          the same promise; only the reaction differs.
Cost:     A Stage 1 method's contract had to change; documented in the
          Stage 1 -> 2 change analysis.

## D18: The Waiter locks the customer's price into the Order
Context:  A VIP pays 10% less. Brief 4.3 later requires "Apply the VIP
          discount first, then Happy Hour. The price is locked when the
          order is taken."
Options:  (a) Order reads the price from the item and a setter changes it
              later
          (b) The Waiter checks the customer's type and computes a discount
          (c) The Waiter asks customer.getDiscountedPrice(menuPrice) and
              passes the result to new Order(item, price)
Chose:    (c)
Because:  - Like a real waiter writing the price on the ticket, the Waiter
            sets the price, but only when the order is created; the Order's
            price can never change afterwards (no setter), so it stays
            locked.
          - The customer knows its own discount, so the Waiter needs no type
            check (rejects b) and a new discount type changes only one
            customer class.
          - All percentage rounding goes through Money.applyPercent(), so
            discounts round consistently (D3).
Cost:     Order's constructor signature changed (Stage 1 -> 2 change).

## D19: Arrival order, and where customer types are created
Context:  Brief 4.2: a reproducible run must include at least one VIP and
          one Critic; two Regular arrivals may be replaced; six arrivals.
Chose:    C2 (turn 4) is the Critic and C3 (turn 7) is the VIP; the others
          stay Regular.
Because:  - The Critic arrives while the restaurant is still quiet, so the
            manager has time to respond to its fast decline: a fair test of
            the mechanic rather than a guaranteed loss. C2 orders a Burger
            (1 unit), so fast service is possible.
          - Customer 3 always orders the slow Pasta (2 units); the VIP's
            slower decline (5 per turn) gives the kitchen time to cook it.
          - ArrivalSchedule is the only class that names the concrete
            customer types when creating them. Creation must name a class
            somewhere; after creation nothing checks the type (the SOLID
            slides: OCP "does NOT mean never modify any code").
Cost:     ArrivalSchedule changes whenever the arrival mix changes.

## D20: Host and Waiter share an abstract SeatingStaff parent (revisits D9)
Context:  Stage 3 adds a Host who seats customers with exactly the same
          rules as the Waiter (brief 4.3). In Stage 1, D9 rejected a shared
          Staff type because the staff shared no operations. Seating is now a
          genuinely shared behaviour.
Options:  (a) Duplicate the seating checks in Host
          (b) Host extends Waiter
          (c) Waiter extends Host
          (d) Abstract SeatingStaff holding seat(); Host and Waiter extend it
Chose:    (d)
Because:  - One copy of the seating rules, used by both roles.
          - (b) breaks LSP: a Host would inherit takeOrder() and serve(),
            which it cannot do (the Bird/Penguin case in the SOLID slides).
          - (c) keeps LSP but claims "a Waiter is a kind of Host", which does
            not match how people think about these roles; the course models
            the real world the way people think about it.
          - Named SeatingStaff, not Staff: the Chef and Cashier cannot seat,
            so they stay outside it (ISP, as in D9).
          - Host adds no methods, but it is a distinct role the controller
            holds and gives its own task each turn.
Cost:     Host is an empty subclass; D9's "no shared staff type" is
          partly reversed, which is documented in the Stage 2 -> 3 analysis.

## D21: The Host acts before the Waiter
Context:  The brief does not say when the Host acts within a turn.
Chose:    Host first, then Waiter, then Chef.
Because:  A customer the Host seats can have their order taken by the
          Waiter in the same turn, so the Host genuinely speeds up service.
          With two free tables, Host and Waiter can each seat someone in
          one turn ("in addition to the Waiter's task").
Cost:     One more choice for the manager each turn (the Host may wait).

## D22: ComboMeal is a MenuItem made of two MenuItems (revisits D2)
Context:  "A combo is made of two existing menu items"; units are the sum,
          price is 90% of the combined price (brief 4.3).
Options:  (a) Change MenuItem into an interface or abstract class
          (b) ComboMeal extends the existing MenuItem and holds its two parts
Chose:    (b)
Because:  - A combo "is a" menu item (orders, the Chef and the Waiter handle
            it like any dish: polymorphism) and "has" two menu items.
          - ComboMeal computes its name, price and units from its parts and
            passes them to MenuItem's constructor, so MenuItem itself did not
            need to change: D2's simple Stage 1 class held up.
          - The parts are the menu's existing dish objects, which exist on
            their own and can be shared by several combos, so this is an
            aggregation (Menu -> MenuItem remains the composition).
          - Price uses Money.applyPercent, so rounding is consistent (D3).
          - All three pairs are offered, after the three dishes. The food-
            choice rule (n - 1) % menu size is unchanged: C1-C3 order exactly
            what they did in Stage 2, and C4/C5 order combos.
Cost:     Adding combos changes what later customers order compared with
          Stage 2 (documented in the change analysis).

## D23: Happy Hour is an entity; turn start is arrival, display, offer, recovery
Context:  Happy Hour can be activated once, at the start of a turn, lasts
          that turn and the next, and its remaining duration must be shown.
          During each active turn, customers waiting for a table or food
          recover 15 satisfaction at turn start (max 100).
Chose:    - HappyHour (entity) holds its state and rules: canActivate(),
            activate() (refuses a second use), isActive(),
            getTurnsRemaining(), endTurn() for the countdown.
          - Turn start: arrival -> display (including Happy Hour status) ->
            offer Happy Hour if unused -> recovery if active.
          - Customer.recoverSatisfaction(amount) applies the +15 only to
            customers still waiting for service, using adjustSatisfaction()
            so the 0-100 limits hold (D16).
Because:  The manager sees the newcomer and the restaurant before deciding,
          and the activation turn itself gets the recovery. The UI only
          displays Happy Hour's status; the rules stay in the entity (2.2).
Cost:     One extra prompt per turn until Happy Hour is used.

## D24: Pricing policies behind an interface (DIP, OCP)
Context:  Orders taken during Happy Hour get 20% off, applied after the VIP
          discount, and the price is locked when the order is taken.
Options:  (a) The Waiter checks if Happy Hour is active
          (b) HappyHour.applyTo(price) decides
          (c) A PricingPolicy interface with StandardPricing and
              HappyHourPricing; the Waiter is given the current policy
Chose:    (c)
Because:  - There are now two interchangeable ways of pricing, which is when
            the course says an interface is meaningful.
          - The Waiter depends on the PricingPolicy abstraction, not on Happy
            Hour (DIP, like OrderService -> Notifier in the slides): it
            applies the policy to the customer's own price, so the VIP
            discount comes first ($15 x 0.9 x 0.8 = $10.80).
          - A new promotion is a new PricingPolicy class with no change to
            the Waiter (OCP). HappyHour supplies the policy for the current
            turn; the price is still locked into the Order (D18).
Cost:     Three small classes for what (b) does in one method; (b) was the
          simpler alternative considered.

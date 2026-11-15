# Restaurant Rush

SC2002 group assignment: a turn-based restaurant management game in Java,
built in three stages (tags `stage-1`, `stage-2`, `stage-3`).

**Repository:** https://github.com/LF-YEOH/sc2302-tais1-g1

## Stages

| Stage | Tag | Features added |
|---|---|---|
| 1 | [`stage-1`](https://github.com/LF-YEOH/sc2302-tais1-g1/tree/stage-1) | Core game: arrivals, Waiter, Chef, Cashier, satisfaction, scoreboard |
| 2 | [`stage-2`](https://github.com/LF-YEOH/sc2302-tais1-g1/tree/stage-2) | VIP and Critic customers |
| 3 | [`stage-3`](https://github.com/LF-YEOH/sc2302-tais1-g1/tree/stage-3) | Host, combo meals, Happy Hour |

## Video

Total length 15:53.

| Time | Segment | Presenter |
|---|---|---|
| 0:00 | Introduction | Yeoh Lin Feng |
| 1:00 | 1. The game launching from the submitted project | Belgutei |
| 2:42 | 2. Meaningful manager choices, including a decision where staff are constrained | Batsukh |
| 4:46 | 3. A dish going READY → SERVED → payment | Tengis |
| 6:47 | 4. A VIP or Critic behaviour | Tenuun |
| 9:12 | 5. A Stage 3 feature (Happy Hour, combo meals, Host) | Belgutei |
| 11:31 | 6. One victory and one defeat | Yeoh Lin Feng |
| 13:36 | 7. The stage tag comparisons and the diagrams | Yeoh Lin Feng |

## Build and run

Requires a JDK (Java 17 or newer). From the repository root:

| Windows (PowerShell) | Mac / Linux |
|---|---|
| `.\build.bat` | `./build.sh` |
| `.\build.bat --replay runs\stage1-victory.txt` | `./build.sh --replay runs/stage1-victory.txt` |

The first line plays with the keyboard; the second plays back a saved run.

## Tests

Tests are described in `docs/test-notes-stage1.md`,
`docs/test-notes-stage2.md` and `docs/test-notes-stage3.md`. Each one is a
replay file in `runs/`, e.g. `.\build.bat --replay runs\stage3-victory.txt`.
Replays only run on the stage they were written for (each stage adds
questions to a turn).

## Stage changes

### Stage 1 → Stage 2: VIP and Critic customers

Compare: https://github.com/LF-YEOH/sc2302-tais1-g1/compare/stage-1...stage-2
Design: `docs/design-stage2.md`; decisions D15–D19 in `docs/DECISIONS.md`.

**New files**

| File | Purpose |
|---|---|
| `entity/VIPCustomer.java` | Loses 5 satisfaction per waiting turn; pays 90% of the menu price |
| `entity/CriticCustomer.java` | Loses 12 per waiting turn; when paying, gains 20 if satisfaction is 50 or more (max 100), otherwise loses 20 (min 0) |
| `runs/stage2-*.txt`, `docs/test-notes-stage2.md` | Stage 2 replays and test notes |

**Changed files, and why**

| File | Change | Why it was necessary |
|---|---|---|
| `entity/Customer.java` | Added overridable `getSatisfactionLoss()`, `getDiscountedPrice()` and `reactToService()`, plus `adjustSatisfaction()` (keeps satisfaction in 0–100). `loseSatisfaction()` uses each customer's own loss rate; `pay()` calls `reactToService()` first | A subclass cannot override a field, so values that differ had to become methods (D16). `pay()`'s contract was changed in the parent so the Critic's reaction does not break it (LSP, D17) |
| `entity/Order.java` | Constructor is now `Order(item, priceCents)` | The price must come from the customer (VIP discount) and be locked when the order is taken (D18) |
| `entity/Waiter.java` | `takeOrder()` asks the customer for its price | So the Waiter never checks a customer's type (D18) |
| `entity/Money.java` | Added `applyPercent()` | One place for rounding discounts to the nearest cent (D3, D18) |
| `control/ArrivalSchedule.java` | Customer 2 is created as a Critic, customer 3 as a VIP | The brief requires at least one of each; creation is the only place that names the types (D19) |

**Unchanged:** `GameController`, `Chef`, `Cashier`, `Table`, `Menu`,
`MenuItem`, `Scoreboard`, `RestaurantUI`. The turn controller needed no
change: customer differences are handled by the customer objects through
polymorphism, with no type checks anywhere.

**Earlier behaviour still works:** with only Regular customers (Customer's
default methods), the Stage 1 replays give exactly the Stage 1 results. The
same victory inputs now give $64.20 and average 83.4 instead of $66.00 and
79.2, because of the VIP discount and the Critic's review.

### Stage 2 → Stage 3: Host, ComboMeal, Happy Hour

Compare: https://github.com/LF-YEOH/sc2302-tais1-g1/compare/stage-2...stage-3
Design: `docs/design-stage3.md`; decisions D20–D24 in `docs/DECISIONS.md`.

**New files**

| File | Purpose |
|---|---|
| `entity/SeatingStaff.java` | Abstract parent holding the seating rules, shared by the Host and the Waiter |
| `entity/Host.java` | Seats one waiting customer per turn, before the Waiter acts |
| `entity/ComboMeal.java` | A menu item made of two menu items: 90% of their total price, the sum of their preparation units |
| `entity/HappyHour.java` | Once per game, lasts 2 turns, counts down; gives the current pricing |
| `entity/PricingPolicy.java` | Interface for restaurant-wide pricing of new orders |
| `entity/StandardPricing.java`, `entity/HappyHourPricing.java` | Normal price; 80% of the price |
| `runs/stage3-*.txt`, `docs/test-notes-stage3.md` | Stage 3 replays and test notes |

**Changed files, and why**

| File | Change | Why it was necessary |
|---|---|---|
| `entity/Waiter.java` | Extends `SeatingStaff` (its `seat()` moved there); `takeOrder()` takes a `PricingPolicy` and applies it after the customer's own price | The Host needs exactly the Waiter's seating rules but must not inherit taking orders or serving (LSP), so the rules moved to a shared parent (D20, revisits D9). The Waiter depends on the pricing interface, not on Happy Hour (DIP, D24) |
| `entity/Menu.java` | Adds the three combos after the dishes | Combos are menu items, so the Menu holds them like any other item (D22) |
| `entity/Customer.java` | Added `recoverSatisfaction()` | Happy Hour's +15 applies only to customers waiting for service, and only the customer knows its own status (D23) |
| `control/GameController.java` | Owns the Host and Happy Hour; turn order is now arrival, display, Happy Hour offer, recovery, Host, Waiter, Chef, ..., Happy Hour countdown | The controller decides the turn order (D21, D23) |
| `boundary/RestaurantUI.java` | Asks whether to activate Happy Hour; shows its status each turn | Input and output stay in the boundary; the rules stay in `HappyHour` (D23) |

**Unchanged:** `Order`, `Chef`, `Cashier`, `Table`, `Scoreboard`,
`MenuItem`, `VIPCustomer`, `CriticCustomer`, `Money`, `ArrivalSchedule`.
Combos needed no change to `Order` or the `Chef`, because a `ComboMeal` is a
`MenuItem`: its name, price and units come through the same methods. The
customer's food-choice rule is also unchanged; with six menu items, C4–C6
now order the combos.

**Earlier behaviour still works:** customers keep their Stage 2 behaviour
(loss rates, VIP discount, Critic review), and invalid choices are refused
with the same messages. The Stage 1 and 2 replay files no longer run, since
each turn now asks two extra questions (Happy Hour and the Host); the same
behaviour is checked in the Stage 3 tests instead.

## Working on this project

### Structure (Boundary-Control-Entity)

| Package | Contains | Rule |
|---|---|---|
| `restaurant.boundary` | `RestaurantUI` | Input and output only. Checks input format, never game rules. |
| `restaurant.control` | `GameController`, `ArrivalSchedule`, `WaiterTask` | Runs the turns, owns the lists of customers and tables. |
| `restaurant.entity` | Customer, Order, Table, Menu, staff, Scoreboard, ... | Data and the rules that belong to it. Never prints or reads input. |

The design is in `docs/design-stage1.md`; design decisions are in `docs/DECISIONS.md`.

### Rules for everyone

1. **Only edit the files you own** (see the "Owner:" line at the top of each
   class). Need a change in someone else's file? Ask the lead first.
2. **Do not change method names, parameters or return types** in the
   skeleton. Other people's code calls them. Ask the lead if one seems wrong.
3. **Follow the comment above each method exactly.** It says what the method
   must do, what it may assume, and when it must throw `InvalidActionException`.
4. **Each stage contains only its own features.** Nothing from a later
   stage (code, classes or comments) until that stage starts.
5. **Comment the purpose of every class and method** (already done in the
   skeleton; keep it accurate if you add private helper methods).
6. **Use only Java covered in the course** (ArrayList, enums, exceptions,
   interfaces, enhanced for, etc.). No Queue/LinkedList, records or Optional.
7. **Record any AI help you use for your GAI declaration.**

### Git workflow

```
git checkout main
git pull
git checkout -b feature/<your-part>        # e.g. feature/customer
# ... write code, build, test ...
git add <your files>
git commit -m "Implement Customer state changes"
git push -u origin feature/<your-part>
```

Then open a **Pull Request** into `main` on GitHub. The lead reviews and
merges it. Commit in small steps with clear messages; the history is marked.

### Testing your part on its own

Make a throwaway class, e.g. `src/restaurant/CustomerCheck.java`, with a
`main` method that creates objects and prints results. Run it with
`javac -d out (all files)` then `java -cp out restaurant.CustomerCheck`.
**Do not commit it.** Delete it before your pull request.

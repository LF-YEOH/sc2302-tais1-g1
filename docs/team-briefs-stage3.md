# Stage 3 team briefs

Read `docs/design-stage3.md` first (one page). Same rules as before: only
edit your own files, keep method names and parameters as in the skeleton,
follow the comment above each method, and **delete each `TODO` note once it
is done**. Never check a customer's type, and never check whether Happy
Hour is active outside `HappyHour` and `GameController`.

`git checkout main`, then `git pull` before creating your branch: the
Stage 3 skeleton is in. **Due: Sun 1 Nov, 11:59 PM** (pull request opened).

---

## C: SeatingStaff, Host, Waiter

**Branch:** `feature/host`
**Files:** `entity/SeatingStaff.java`, `entity/Host.java`, `entity/Waiter.java`

Every `TODO (C)`:
- **`SeatingStaff.seat()`**: move your seating checks here from
  `Waiter.seat()`, unchanged (same rules, same messages).
- **`Waiter`**: make it extend `SeatingStaff` and delete its own `seat()`.
  It keeps `takeOrder()` and `serve()`.
- **`Waiter.takeOrder()`** now receives a `PricingPolicy`: the price locked
  into the order is the customer's own price with the policy applied on
  top (customer's discount first, then the policy).
- `Host` is already written: it extends `SeatingStaff` and adds nothing.

**Why it matters:** the Host and Waiter now share one copy of the seating
rules (D20). `Host extends Waiter` was rejected because a Host would then
inherit `takeOrder()` and `serve()`, which it cannot do (LSP). The Waiter
depends on the `PricingPolicy` interface, not on Happy Hour (DIP, D24).

**Test:** in the game, the Host can seat a waiting customer and the Waiter
can take their order in the same turn; seating someone already seated is
refused with the same message as before. A VIP's Pasta taken during Happy
Hour costs $12.96.

**Video:** segment 2 (manager choices; you can now include the Host).

---

## D: ComboMeal, Menu, RestaurantUI

**Branch:** `feature/combos-ui`
**Files:** `entity/ComboMeal.java`, `entity/Menu.java`,
`boundary/RestaurantUI.java`

Every `TODO (D)`:
- **`ComboMeal` constructor**: pass the combo's real name
  (`"Salad + Pasta"`), price and preparation units to `MenuItem`'s
  constructor, as the class comment describes (price: 90% of the two prices
  added together, using `Money`'s percentage helper; units: the two units
  added together).
- **`Menu`**: after the three dishes, add the three combos in this order:
  Salad + Burger, Salad + Pasta, Burger + Pasta, built from the dish
  objects. Update the class comment.
- **`RestaurantUI.askActivateHappyHour()`**: explain what Happy Hour does
  (20% off new orders and +15 satisfaction for waiting customers, for 2
  turns, once per game) and read 1 (yes) or 0 (no).
- **`RestaurantUI.showHappyHourStatus()`**: print one line, e.g.
  `Happy Hour: available`, `Happy Hour: ACTIVE (1 turn left, 20% off new orders)`
  or `Happy Hour: used`.

**Why it matters:** a combo *is a* menu item and is *made of* two menu
items (inheritance and aggregation, D22), so orders, the Chef and the
Waiter handle combos with no changes. The UI only displays Happy Hour; its
rules stay in the `HappyHour` entity (D23).

**Test:** the menu lists Salad + Burger ($21.60, 2 units), Salad + Pasta
($24.30, 3 units) and Burger + Pasta ($29.70, 3 units); C4 orders Salad +
Burger in a game; the Happy Hour prompt and status line appear each turn
until it is used.

**Video:** segments 1 (game launching) and 5 (a Stage 3 feature).

---

## Lead

Already in the skeleton: `HappyHour`, `PricingPolicy`, `StandardPricing`,
`HappyHourPricing`, `Customer.recoverSatisfaction()`, and the
`GameController` changes (Host step, Happy Hour offer, recovery, countdown).
After C's and D's pull requests are merged: Stage 3 replays and test notes,
README change analysis (Stage 2 → 3), tag `stage-3` on Mon 2 Nov.

# Stage 2 team briefs

Read `docs/design-stage2.md` first: it explains the design in one page.
Same rules as Stage 1: only edit your own files, keep method names and
parameters exactly as in the skeleton, and follow the comment above each
method. **Never check a customer's type** (no `instanceof`) anywhere:
that is the point of Stage 2 (brief §4.2).

Work on your own branch and open a pull request into `main`.
**Due: Wed 28 Oct, 11:59 PM.**

---

## A: Customer, VIPCustomer, CriticCustomer

**Branch:** `feature/customer-types`
**Files:** `entity/Customer.java`, `entity/VIPCustomer.java` (new),
`entity/CriticCustomer.java` (new)

Every `TODO (A)` in these files:
- `Customer.adjustSatisfaction(amount)`: add `amount` (may be negative),
  keeping satisfaction within 0 to 100.
- `Customer.loseSatisfaction()`: use `adjustSatisfaction(-getSatisfactionLoss())`
  instead of the `SATISFACTION_LOSS` constant.
- `Customer.pay()`: call `reactToService()` as the very first step.
- `VIPCustomer`: loss 5, price `Money.applyPercent(priceCents, 90)`,
  label `C3 [VIP]` (use `super.toString() + " [VIP]"`).
- `CriticCustomer`: loss 12; `reactToService()`: satisfaction ≥ 50 →
  `adjustSatisfaction(+20)`, otherwise `adjustSatisfaction(-20)`;
  label `C2 [Critic]`.

**Why it matters:** `getSatisfactionLoss()`, `getDiscountedPrice()`,
`reactToService()` and `toString()` are overridden so the rest of the game
never needs to know the type (polymorphism). Satisfaction stays `private`;
subclasses change it only through `adjustSatisfaction()`, which keeps it in
0–100 (encapsulation).

**Test (throwaway main, don't commit):**
- A `VIPCustomer` left waiting 4 turns has satisfaction 80; a
  `CriticCustomer` has 52; a `Customer` has 68.
- `new VIPCustomer(3).getDiscountedPrice(1800)` is 1620 once B's
  `Money.applyPercent` is merged (until then the stub returns 1800).
- A Critic at 76 who pays ends at 96; at 40 ends at 20; at 90 ends at 100.

**Video:** segment 4 (VIP/Critic behaviour).

---

## B: Money, Order, Waiter pricing

**Branch:** `feature/locked-pricing`
**Files:** `entity/Money.java`, `entity/Order.java`, `entity/Waiter.java`
(only the `takeOrder` change)

Every `TODO (B)`:
- `Money.applyPercent(cents, percent)`: return the percentage rounded to
  the nearest cent, e.g. `(int) Math.round(cents * percent / 100.0)`.
  `applyPercent(1800, 90)` is 1620; `applyPercent(1500, 90)` is 1350.
- `Order`: replace `Order(MenuItem item)` with
  `Order(MenuItem item, int priceCents)`, which locks the given price.
  Throw `IllegalArgumentException` if `priceCents` is not positive. Update
  the constructor's comment to match.
- `Waiter.takeOrder()`: replace `new Order(item)` with
  `int price = customer.getDiscountedPrice(item.getPriceCents());`
  `new Order(item, price)`. Remove the TODO comments once done.

**Why it matters:** the Waiter writes the price on the order once, when it
is taken, and nothing can change it afterwards (brief §4.3: "The price is
locked when the order is taken"). The customer decides its own price, so
the Waiter never checks the customer's type.

**Test:** both Stage 1 replays must still give exactly the same result
(VICTORY $66.00 and DEFEAT), because Regular customers pay full price:
`.\build.bat --replay runs\stage1-victory.txt`

**Video:** segment 3 (a dish going READY → SERVED → payment).

---

## Lead

After A's and B's pull requests are merged: `ArrivalSchedule` (C2 Critic,
C3 VIP), Stage 2 replays and test notes, README change analysis
(Stage 1 → 2), tag `stage-2` on Thu 29 Oct.

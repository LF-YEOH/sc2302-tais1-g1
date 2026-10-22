# Stage 1 team briefs

Everyone: read `README.md` ("Working on this project") and
`docs/design-stage1.md` first. Every method you implement has a comment in
the skeleton saying exactly what it must do. Follow it; do not rename or
change any method signature.

Video segments (brief Section 6.2) are assigned so you explain what you built.

---

## A: Customer

**Files:** `entity/Customer.java`
**Branch:** `feature/customer`
**Due:** Sun 25 Oct, 11:59 PM (pull request opened)

Implement every method marked `TODO (A)`. Key rules:
- A new customer starts with satisfaction 100, status WAITING.
- Food choice: customer n orders menu item `(n - 1) % menu.getNumItems()`.
- `finishEating(turn)` only succeeds if the customer was served on an
  EARLIER turn (`servedTurn < turn`). This is what makes eating take a full turn.
- `loseSatisfaction()` only affects WAITING, SEATED and ORDERED customers,
  and never goes below 0.
- `pay()` and `leave()` must free the table and update the order.

**Test:** create a Customer, a Menu, an Order and a Table in a throwaway
main; walk one customer from WAITING to PAID and print the status after
each call. Check C1 chooses Salad, C2 Burger, C3 Pasta, C4 Salad.

**Later:** Stage 2 VIP and Critic customers (you will own this).
**Video:** segment 4 (VIP/Critic behaviour).

---

## B: Order, Table, Scoreboard

**Files:** `entity/Order.java`, `entity/Table.java`, `entity/Scoreboard.java`
**Branch:** `feature/order-table-scoreboard`
**Due:** Sun 25 Oct, 11:59 PM

Implement every method marked `TODO (B)`. Key rules:
- An order's price is locked when the order is created.
- `addPreparationUnit()` makes the order READY when the last unit is done.
- `Table.seat(customer)` must also call `customer.sitAt(this)`.
- `Scoreboard.isVictory()`: at least 4 paid AND revenue at least 4500 cents
  AND average satisfaction of paid customers at least 60.

**Test:** an Order for Pasta needs two `addPreparationUnit()` calls to be
READY; a Scoreboard with payments (900, 84), (1500, 84), (1800, 76),
(900, 76), (1500, 76) is a victory with average 79.2.

**Later:** Stage 2 README change analysis; Stage 3 ComboMeal.
**Video:** segment 3 (a dish going READY -> SERVED -> payment).

---

## C: Waiter, Chef, Cashier

**Files:** `entity/Waiter.java`, `entity/Chef.java`, `entity/Cashier.java`
**Branch:** `feature/staff`
**Due:** Sun 25 Oct, 11:59 PM

Implement every method marked `TODO (C)`. Key rules:
- **Check first, act second.** If any rule is broken, throw
  `InvalidActionException` with a clear message BEFORE changing anything,
  so the manager can choose again without losing the task.
- Messages should help the player, e.g.
  "C3's Pasta is not ready yet (1/2 units done)."
- The Waiter seats via `table.seat(customer)`; takes orders via
  `customer.chooseItem(menu)`, `new Order(...)`, `customer.placeOrder(order)`;
  serves via `customer.receiveFood(turn)`.

**Test:** try every invalid case (serving an unready dish, seating a seated
customer, cooking a READY order) and check the message and that nothing changed.

**Later:** Stage 3 Host.
**Video:** segment 2 (manager choices when staff are limited).

---

## D: RestaurantUI

**Files:** `boundary/RestaurantUI.java`
**Branch:** `feature/ui`
**Due:** Sun 25 Oct, 11:59 PM

Implement every method marked `TODO (D)`. Key rules:
- The UI never checks game rules; it only displays and reads.
- `readInt` reads a whole line, prints it if `echoInput` is true, and asks
  again on anything that is not a whole number in range. At the end of a
  replay file it returns `min`.
- Show money with `Money.format(...)` and the average as one decimal place,
  or "N/A" when `board.hasPaidCustomers()` is false.
- The start-of-turn display must show: turn number, revenue, average
  satisfaction, the waiting queue, each table's customer with their
  satisfaction, status and order, and the available staff tasks.

**Test:** in a throwaway main, create a few Customers, Tables and a
Scoreboard and call each show method; type bad input ("abc", "9") into
`askWaiterTask()` and check it asks again.

**Later:** UI changes for Stages 2 and 3; replay files.
**Video:** segments 1 (game launching) and 5 (a Stage 3 feature).

---

## Lead: GameController, ArrivalSchedule, Main

**Files:** `control/GameController.java`, `control/ArrivalSchedule.java`, `Main.java`
**Due:** integrated and tagged `stage-1` by Mon 26 Oct, 11:59 PM

Review and merge pull requests in order B -> A -> C -> D. Implement the turn
engine; write the victory and defeat replay files; tag `stage-1`.
**Video:** segments 6 (victory and defeat) and 7 (tags and diagrams).

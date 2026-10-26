# Stage 1 test notes

All tests use the base setting (brief §2.1) and are fully reproducible: each
one is a replay file in `runs/` holding the player's inputs, one per line.

Run any test from the repository root:

| Windows | Mac / Linux |
|---|---|
| `.\build.bat --replay runs\<file>` | `./build.sh --replay runs/<file>` |

Input format: Waiter task (`1` Seat, `2` Take order, `3` Serve, `0` Wait),
then the customer number if a task was chosen; then the Chef's customer
number (`0` = wait).

---

## Test 1: Victory run (brief §4.1)

**File:** `runs/stage1-victory.txt`

The manager seats, takes the order and serves each customer as soon as
possible, and the Chef cooks every order immediately.

| Turn | Arrives | Waiter | Chef | Result |
|---|---|---|---|---|
| 1 | C1 | Seat C1 | wait | |
| 2 | | Take order C1 (Salad) | cook C1 | Salad READY |
| 3 | | Serve C1 | wait | |
| 4 | C2 | Seat C2 | wait | C1 READY_TO_PAY |
| 5 | | Take order C2 (Burger) | cook C2 | C1 pays $9.00 |
| 6 | | Serve C2 | wait | |
| 7 | C3 | Seat C3 | wait | C2 READY_TO_PAY |
| 8 | | Take order C3 (Pasta) | cook C3 (1/2) | C2 pays $15.00 |
| 9 | | wait | cook C3 (2/2) | Pasta READY |
| 10 | C4 | Serve C3 (C4 waits) | wait | |
| 11 | | Seat C4 | wait | C3 READY_TO_PAY |
| 12 | | Take order C4 (Salad) | cook C4 | C3 pays $18.00 |
| 13 | C5 | Serve C4 | wait | |
| 14 | | Seat C5 | wait | C4 READY_TO_PAY |
| 15 | | Take order C5 (Burger) | cook C5 | C4 pays $9.00 |
| 16 | C6 | Serve C5 | wait | |
| 17 | | Seat C6 | wait | C5 READY_TO_PAY |
| 18 | | wait | wait | C5 pays $15.00 |

**Expected result:** VICTORY. 5 paid customers, revenue $66.00, average
satisfaction 79.2 (84, 84, 76, 76, 76). All three targets marked `[MET]`.
C6 (arrived on turn 16) cannot pay within 18 turns, as the brief intends.

**Actual result:** as expected. **PASS**

---

## Test 2: Defeat run (brief §4.1)

**File:** `runs/stage1-defeat.txt`

The manager makes every staff member wait on every turn.

**Expected result:** customers stay in the queue losing 8 satisfaction per
turn. C1 (arrived turn 1) reaches 0 and leaves on turn 13; C2 (arrived
turn 4) leaves on turn 16. Nobody is served or pays. DEFEAT: 0 paid,
$0.00, average satisfaction N/A, all three targets `[NOT MET]`. The game
still runs all 18 turns (no early ending, brief §3.2 step 6).

**Actual result:** as expected. **PASS**

---

## Test 3: The brief's worked example (brief §3.3)

**File:** `runs/stage1-victory.txt`, customer C3 (Pasta, 2 units), turns 7–12.

| Turn | What happens | Satisfaction at turn end |
|---|---|---|
| 7 | Arrives, seated | 92 |
| 8 | Order taken, Chef does unit 1 of 2 | 84 |
| 9 | Chef does unit 2 of 2: READY; the Waiter has already acted, so it cannot be served this turn | 76 |
| 10 | Served | 76 (no further loss) |
| 11 | Finishes eating: READY_TO_PAY | 76 |
| 12 | Cashier collects $18.00, table freed | paid at 76 |

**Expected result:** identical to the brief's example table (92, 84, 76,
76, 76, $18.00 at satisfaction 76).

**Actual result:** as expected. **PASS**

This test also confirms that the Chef can cook an order the Waiter took
in the same turn (turn 8), that a dish finished this turn is served next
turn at the earliest (turns 9 → 10), and that a customer pays the turn
after becoming READY_TO_PAY (turns 11 → 12).

---

## Test 4: Invalid choices (brief §3.4)

**File:** `runs/stage1-invalid-input.txt`. All inputs are on turn 1, when
only C1 has arrived (waiting in the queue).

| Input | Expected response |
|---|---|
| Serve C1 | `ERROR: Customer has no dish waiting to be served (customer is waiting). Please choose again.` |
| Take order from C1 | `ERROR: Customer is not seated. Please choose again.` |
| `abc` | `Not a whole number. Enter 0 to 3:` |
| `7` | `Out of range. Enter 0 to 3:` |
| Seat C1 | `C1 is seated at Table 1.` (the task succeeds) |
| Chef: cook C1 | `ERROR: That customer has no order. Please choose again.` |
| Chef: `0` | `The Chef waits this turn.` |

**Expected result:** every invalid choice explains why it failed and asks
again. Refused choices do not use up the task: after four refused or
malformed inputs, the Waiter still seats C1 that turn.

**Actual result:** as expected. **PASS**

---

## Test 5: A seated customer with an order runs out of patience (brief §3.2 step 5)

**File:** `runs/stage1-abandonment.txt`. C1 is seated on turn 1 and orders
on turn 2, but the Chef never cooks and the Waiter never serves.

**Expected result:** C1 loses 8 satisfaction per turn (92, 84, …, 12, 4)
and never goes below 0. On turn 13 C1 reaches 0 and leaves without
paying: the order is cancelled and Table 1 shows `empty` from turn 14.
Customers still in the queue keep losing satisfaction; C2 leaves on
turn 16. Revenue stays $0.00 (unpaid orders never count, brief §3.4).

**Actual result:** as expected. **PASS**

---

## Summary

| Test | Brief | Result |
|---|---|---|
| 1. Victory run | §4.1 | PASS |
| 2. Defeat run | §4.1 | PASS |
| 3. Worked example | §3.3 | PASS |
| 4. Invalid choices | §3.4 | PASS |
| 5. Abandonment with an order | §3.2 step 5, §3.4 | PASS |

Tested on 26 Oct 2026 against the code tagged `stage-1`.

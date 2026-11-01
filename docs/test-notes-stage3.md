# Stage 3 test notes

All tests use the base setting with Stage 3 features: a Host who seats
customers before the Waiter acts, three combo meals on the menu, and Happy
Hour (once per game, 2 turns). Arrivals are the same as Stage 2 (C2 Critic,
C3 VIP). Run any test with `.\build.bat --replay runs\<file>` (Windows) or
`./build.sh --replay runs/<file>` (Mac/Linux).

---

## Test 1: Victory run with Host, combos and Happy Hour

**File:** `runs/stage3-victory.txt`. The Host seats each arrival; Happy Hour
is activated on turn 7, when the VIP arrives.

| Customer | Order | Price | Satisfaction at payment |
|---|---|---|---|
| C1 | Salad | $9.00 | 92 |
| C2 Critic | Burger | $15.00 | **100** (good review, capped at 100) |
| C3 VIP | Pasta, taken during Happy Hour | **$12.96** ($18.00 × 0.9 × 0.8: VIP discount first, then Happy Hour) | 95 |
| C4 | **Salad + Burger** | **$21.60** (90% of $24.00), 2 units | 84 |
| C5 | **Salad + Pasta** | **$24.30** (90% of $27.00), 3 units | 76 |
| C6 | **Burger + Pasta** | $29.70, 3 units (ordered, not finished by turn 18) | |

Also checked in the output:
- The status line reads `Happy Hour: available` until turn 7, then
  `ACTIVE (1 turn left, 20% off new orders)` on turn 8, then `used`; the
  offer is not repeated after it has been used.
- Orders taken after Happy Hour (C4, C5, C6) are at the normal price.

**Expected result:** VICTORY. 5 paid, revenue $82.86, average satisfaction
89.4.

**Actual result:** as expected. **PASS**

---

## Test 2: Defeat run

**File:** `runs/stage3-defeat.txt` (the manager always says no to Happy Hour
and always waits)

**Expected result:** nobody is seated. As in Stage 2, the Critic leaves on
**turn 12**, before C1 leaves on turn 13. Happy Hour stays `available` all
game. Nobody pays. DEFEAT: 0 paid, $0.00, average N/A, 2 unhappy departures.

**Actual result:** as expected. **PASS**

---

## Test 3: Host refusals and Happy Hour rules

**File:** `runs/stage3-host-happy-hour.txt`

| Turn | Input | Expected |
|---|---|---|
| 1 | Activate Happy Hour | `Happy Hour starts!`; C1 recovers 15 but stays at **100** (capped) |
| 1 | Host seats C9 | `ERROR: There is no such customer. Please choose again.` |
| 1 | Host seats C1, Waiter takes C1's order | Same turn (Host acts before the Waiter): `C1 orders Salad ($7.20, 1 unit).` (80% of $9.00) |
| 2 | (no offer: Happy Hour is active) | `Happy Hour: ACTIVE (1 turn left, 20% off new orders)`; C1 is waiting for food and recovers from **92 to 100** |
| 2 | Host seats C1 again | `ERROR: Customer is already ordered. Please choose again.` (the same rule and message as the Waiter's in Stage 2) |
| 3 | | `Happy Hour: used`, and the offer is not shown again |
| 4 | | `C1 pays $7.20 (satisfaction 100)`: the Happy Hour price stays locked in after Happy Hour ends |

**Actual result:** as expected. **PASS**

---

## Test 4: Regression (Stage 1 and 2 behaviour is preserved)

The Stage 1 and Stage 2 replay files cannot be replayed on Stage 3, because
every turn now has two extra questions (the Happy Hour offer and the Host).
The earlier behaviour is checked in the Stage 3 runs instead:
- Regular customers lose 8 per waiting turn (C1: 100 to 92 in Test 1).
- The VIP pays 90% and loses 5 per turn; the Critic loses 12 and reviews
  when paying (Test 1, Test 2).
- Customers who run out of patience leave in the same turns as Stage 2
  (Critic turn 12, C1 turn 13 in Test 2).
- Invalid choices are refused with the same messages and asked again
  (Test 3).
- `Order`, `Chef`, `Cashier`, `Table`, `Scoreboard`, `MenuItem`,
  `VIPCustomer` and `CriticCustomer` are unchanged since Stage 2.
- No class checks a customer's type (`instanceof` appears nowhere), and the
  `Waiter` never checks whether Happy Hour is active: it only applies the
  `PricingPolicy` it is given.

**Actual result:** as expected. **PASS**

---

| Test | Brief | Result |
|---|---|---|
| 1. Victory with Host, combos and Happy Hour | §4.3 | PASS |
| 2. Defeat | §4.3 (same win and lose criteria) | PASS |
| 3. Host refusals and Happy Hour rules | §4.3 (Host; Happy Hour once, 2 turns, 20% off, +15) | PASS |
| 4. Regression | §4.4 (earlier behaviour still works) | PASS |

Tested on 1 Nov 2026 against the Stage 3 code on `main`.

# Stage 2 test notes

All tests use the base setting with Stage 2 arrivals: C2 (turn 4) is a
Critic and C3 (turn 7) is a VIP; the other customers are Regular. Run any
test with `.\build.bat --replay runs\<file>` (Windows) or
`./build.sh --replay runs/<file>` (Mac/Linux).

---

## Test 1: Victory run

**File:** `runs/stage2-victory.txt` (the same inputs as the Stage 1 victory run)

| Customer | Type | Satisfaction at payment | Bill |
|---|---|---|---|
| C1 | Regular | 84 | $9.00 |
| C2 | Critic | 76, then **96** after a good review (≥ 50, +20) | $15.00 |
| C3 | VIP | **85** (loses only 5 per waiting turn) | **$16.20** (90% of $18.00) |
| C4 | Regular | 76 | $9.00 |
| C5 | Regular | 76 | $15.00 |

**Expected result:** VICTORY. 5 paid, revenue $64.20, average satisfaction
83.4. Orders show the locked price, e.g. `C3 [VIP] orders Pasta ($16.20, 2 units).`

**Actual result:** as expected. **PASS**

---

## Test 2: Defeat run

**File:** `runs/stage2-defeat.txt` (the manager always waits)

**Expected result:** every customer stays in the queue. The Critic (arrived
turn 4, loses 12 per turn) runs out of patience and leaves on **turn 12**,
before the Regular C1 (arrived turn 1) leaves on turn 13. The VIP (loses 5
per turn) is still waiting at satisfaction 45 when the game ends. Nobody
pays. DEFEAT: 0 paid, $0.00, average N/A.

**Actual result:** as expected. **PASS**

---

## Test 3: The Critic's bad review (slow service)

**File:** `runs/stage2-slow-critic.txt`. The Critic is seated on turn 4 but
the order is only taken on turn 7, and served on turn 9.

| Turn | Satisfaction at turn end |
|---|---|
| 4 | 88 (seated) |
| 5 | 76 |
| 6 | 64 |
| 7 | 52 (order taken, Chef finishes the Burger) |
| 8 | 40 (READY but not served) |
| 9 | 40 (served, no further loss) |
| 10 | 40 (READY_TO_PAY) |
| 11 | pays at **20** (below 50: bad review, −20) |

**Expected result:** `C2 [Critic] pays $15.00 (satisfaction 20)`.

**Actual result:** as expected. **PASS**

---

## Test 4: Regression (Stage 1 behaviour is preserved)

Regular customers use `Customer`'s default methods (loss 8, full price, no
reaction), so Regular behaviour is unchanged from Stage 1:
- In Test 1, C1, C4 and C5 pay exactly what they did in Stage 1 (84, 76, 76;
  full price).
- `runs/stage1-invalid-input.txt` still refuses every invalid choice with
  the same messages and asks again.
- `runs/stage1-abandonment.txt` still shows a seated customer with an order
  leaving on turn 13 with the order cancelled and the table freed.
- No class checks a customer's type (`instanceof` appears nowhere).

**Actual result:** as expected. **PASS**

---

| Test | Brief | Result |
|---|---|---|
| 1. Victory with VIP and Critic | §4.2 | PASS |
| 2. Defeat | §4.2 (same win and lose criteria) | PASS |
| 3. Critic's bad review | §4.2 (Critic reaction) | PASS |
| 4. Regression | §4.4 (earlier behaviour still works) | PASS |

Tested on 29 Oct 2026 against the code tagged `stage-2`.

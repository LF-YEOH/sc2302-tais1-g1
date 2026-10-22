# Stage 1 Design (draft)

Working design for Stage 1 of Restaurant Rush, drawn before implementation.
The final, submitted class diagram is produced in Visual Paradigm from the
Stage 3 code; this draft records the Stage 1 design for the change analysis.

## Class diagram

```mermaid
---
config:
  layout: elk
  elk:
    mergeEdges: true
    nodePlacementStrategy: BRANDES_KOEPF
  class:
    hideEmptyMembersBox: true
---
classDiagram
    direction TB

    namespace boundary {
        class RestaurantUI {
            <<boundary>>
            -sc : Scanner
            -echoInput : boolean
            +RestaurantUI(sc : Scanner, echoInput : boolean)
            +showWelcome() void
            +showTurnStart(turn : int, board : Scoreboard, queue : ArrayList~Customer~, tables : ArrayList~Table~) void
            +showAvailableTasks(toSeat, toOrder, toServe, toCook : ArrayList~Customer~) void
            +askWaiterTask() WaiterTask
            +askCustomerId(prompt : String) int
            +showOrderPlaced(customer : Customer) void
            +showMessage(message : String) void
            +showError(message : String) void
            +showTurnEnd(turn : int, board : Scoreboard) void
            +showResult(board : Scoreboard) void
            -readInt(min : int, max : int) int
        }
    }

    namespace control {
        class GameController {
            <<control>>
            -MAX_TURNS : int = 18$
            -NUM_TABLES : int = 2$
            -turn : int
            -ui : RestaurantUI
            -menu : Menu
            -tables : ArrayList~Table~
            -customers : ArrayList~Customer~
            -waitingQueue : ArrayList~Customer~
            -waiter : Waiter
            -chef : Chef
            -cashier : Cashier
            -scoreboard : Scoreboard
            -schedule : ArrivalSchedule
            +GameController(ui : RestaurantUI)
            +run() void
            -playTurn() void
            -admitArrival() void
            -runWaiterStep() void
            -runChefStep() void
            -runPaymentStep() void
            -runEatingStep() void
            -runWaitingStep() void
            -findCustomer(id : int) Customer
            -findFreeTable() Table
            -findNextToPay() Customer
            -customersWithStatus(status : CustomerStatus) ArrayList~Customer~
        }
        class ArrivalSchedule {
            <<control>>
            -ARRIVAL_TURNS : int[] = 1,4,7,10,13,16$
            -nextId : int
            +hasArrival(turn : int) boolean
            +createCustomer() Customer
        }
        class WaiterTask {
            <<enumeration>>
            SEAT
            TAKE_ORDER
            SERVE
            WAIT
        }
    }

    namespace entity {
        class Customer {
            <<entity>>
            -START_SATISFACTION : int = 100$
            -SATISFACTION_LOSS : int = 8$
            -id : int
            -satisfaction : int
            -status : CustomerStatus
            -order : Order
            -table : Table
            -servedTurn : int
            -readyToPayTurn : int
            +Customer(id : int)
            +chooseItem(menu : Menu) MenuItem
            +sitAt(table : Table) void
            +placeOrder(order : Order) void
            +receiveFood(turn : int) void
            +finishEating(turn : int) boolean
            +pay() int
            +isWaitingForService() boolean
            +loseSatisfaction() void
            +hasRunOutOfPatience() boolean
            +leave() void
        }
        class CustomerStatus {
            <<enumeration>>
            WAITING
            SEATED
            ORDERED
            EATING
            READY_TO_PAY
            PAID
            LEFT
        }
        class Order {
            <<entity>>
            -item : MenuItem
            -priceCents : int
            -unitsDone : int
            -status : OrderStatus
            +Order(item : MenuItem)
            +isReady() boolean
            +addPreparationUnit() void
            +markServed() void
            +markPaid() void
            +cancel() void
        }
        class OrderStatus {
            <<enumeration>>
            PREPARING
            READY
            SERVED
            PAID
            CANCELLED
        }
        class Table {
            <<entity>>
            -number : int
            -occupant : Customer
            +Table(number : int)
            +isFree() boolean
            +seat(customer : Customer) void
            +clear() void
        }
        class Menu {
            <<entity>>
            -items : ArrayList~MenuItem~
            +Menu()
            +getNumItems() int
            +getItem(index : int) MenuItem
        }
        class MenuItem {
            <<entity>>
            -name : String
            -priceCents : int
            -prepUnits : int
            +MenuItem(name : String, priceCents : int, prepUnits : int)
            +getName() String
            +getPriceCents() int
            +getPrepUnits() int
        }
        class Waiter {
            <<entity>>
            +seat(customer : Customer, table : Table) void
            +takeOrder(customer : Customer, menu : Menu) Order
            +serve(customer : Customer, turn : int) void
        }
        class Chef {
            <<entity>>
            +cook(order : Order) void
        }
        class Cashier {
            <<entity>>
            +collectPayment(customer : Customer) int
        }
        class Scoreboard {
            <<entity>>
            -MIN_PAID : int = 4$
            -MIN_REVENUE_CENTS : int = 4500$
            -MIN_AVG_SATISFACTION : int = 60$
            -revenueCents : int
            -paidCount : int
            -totalPaidSatisfaction : int
            -servedCount : int
            -departures : int
            +recordPayment(amountCents : int, satisfaction : int) void
            +recordServed() void
            +recordDeparture() void
            +hasPaidCustomers() boolean
            +getAverageSatisfaction() double
            +isVictory() boolean
        }
        class InvalidActionException {
            <<entity>>
            +InvalidActionException(message : String)
        }
        class Money {
            <<entity>>
            +format(cents : int)$ String
        }
    }

    GameController "1" --> "1" RestaurantUI : ui
    GameController "1" *-- "1" Menu
    GameController "1" *-- "2" Table : tables
    GameController "1" *-- "1" Waiter
    GameController "1" *-- "1" Chef
    GameController "1" *-- "1" Cashier
    GameController "1" *-- "1" Scoreboard
    GameController "1" *-- "1" ArrivalSchedule
    GameController "1" --> "0..6" Customer : customers
    GameController "1" --> "0..*" Customer : waitingQueue {ordered}
    Menu "1" *-- "3" MenuItem : items
    Customer "1" --> "0..1" Order : order
    Customer "0..1" -- "0..1" Table : seated at
    Order "0..*" --> "1" MenuItem : item
    Customer ..> CustomerStatus
    Order ..> OrderStatus
    ArrivalSchedule ..> Customer : creates
    Waiter ..> Order : creates
    Waiter ..> InvalidActionException : throws
    Chef ..> InvalidActionException : throws
    Cashier ..> InvalidActionException : throws
```

## Class responsibilities

| Class | Stereotype | Responsibility |
|---|---|---|
| `RestaurantUI` | boundary | Shows the game state and reads the manager's choices. Checks input format only (e.g. "is it a number?"); never checks game rules. |
| `GameController` | control | Runs the 18 turns and the six turn steps in order. Owns the customers, waiting queue, tables, staff and scoreboard, and holds the loops over them. |
| `ArrivalSchedule` | control | Knows which turns have an arrival and creates the arriving customer. |
| `WaiterTask` | enumeration | The four Waiter choices: SEAT, TAKE_ORDER, SERVE, WAIT. |
| `Customer` | entity | Holds satisfaction and lifecycle state; chooses its own food; updates its own state when seated, served, finished eating, paying or leaving. |
| `Order` | entity | Holds the ordered item, the locked price and preparation progress; becomes READY when the last unit is done. |
| `Table` | entity | Knows whether it is free and who sits at it. |
| `Menu`, `MenuItem` | entity | The three dishes with price and preparation units. Menu creates and owns its items. |
| `Waiter`, `Chef`, `Cashier` | entity | Each performs its staff action after checking the rules, and refuses an invalid action with a reason. |
| `Scoreboard` | entity | Records revenue, paid customers, satisfaction at payment, dishes served and departures; computes the average and the victory check. |
| `InvalidActionException` | entity | Carries the reason an action was refused, so the manager can be told and choose again. |
| `Money` | entity | Formats cents as dollars. |

## Turn sequence (sketch)

```mermaid
sequenceDiagram
    autonumber
    actor M as Manager
    participant ui as ui : RestaurantUI
    participant gc as gc : GameController
    participant sch as schedule : ArrivalSchedule
    participant w as waiter : Waiter
    participant ch as chef : Chef
    participant ca as cashier : Cashier
    participant c as c : Customer

    loop turn = 1 to 18
        Note over gc: Step 1 - Arrivals
        gc->>sch: hasArrival(turn)
        opt arrival this turn
            gc->>sch: createCustomer()
            sch-->>gc: new Customer (satisfaction 100)
        end
        gc->>ui: showTurnStart(...) / showAvailableTasks(...)

        Note over gc: Steps 2-3 - Waiter acts BEFORE Chef
        loop until valid task or WAIT
            gc->>ui: askWaiterTask()
            ui->>M: prompt
            M-->>ui: choice
            ui-->>gc: task, customer id
            gc->>w: seat / takeOrder / serve(...)
            alt rule broken
                w-->>gc: throws InvalidActionException
                gc->>ui: showError(reason)
            else valid
                w-->>gc: done
            end
        end
        loop until valid order or WAIT
            gc->>ui: askCustomerId("Chef")
            ui-->>gc: customer id
            gc->>ch: cook(order)
        end

        Note over gc: Step 4 - Payment BEFORE eating
        opt someone already READY_TO_PAY
            gc->>gc: findNextToPay() (oldest first)
            gc->>ca: collectPayment(c)
            ca->>c: pay()
            c-->>ca: bill
            ca-->>gc: bill (Scoreboard records it)
        end
        loop each EATING customer served on an earlier turn
            gc->>c: finishEating(turn)
        end

        Note over gc: Step 5 - Waiting
        loop each customer not yet served
            gc->>c: loseSatisfaction()
            opt satisfaction reached 0
                gc->>c: leave()
            end
        end
        gc->>ui: showTurnEnd(turn, scoreboard)
    end
    Note over gc: Step 6 - Result
    gc->>ui: showResult(scoreboard)
```

## Game rules as implemented

- **Food choice:** customer *n* orders menu item number `(n - 1) % 3`, so
  C1 Salad, C2 Burger, C3 Pasta, C4 Salad, C5 Burger, C6 Pasta. The choice
  is shown when the order is placed.
- **Arrivals happen before the start-of-turn display**, so the manager sees
  the new customer before choosing staff tasks.
- **Seating:** the manager may seat any queued customer (not only the first
  in line); the Waiter uses the lowest-numbered free table.
- **Waiter before Chef:** the Chef may cook an order taken this turn. A dish
  the Chef finishes this turn cannot be served until next turn, because the
  Waiter has already acted.
- **Payment before eating:** in step 4 the Cashier first charges the oldest
  customer who was already READY_TO_PAY; only then do customers who were
  served on an earlier turn become READY_TO_PAY. This stops anyone paying in
  the same turn they finish eating.
- **Waiting loss:** at turn end, customers who are WAITING, SEATED or ORDERED
  (including those whose dish is READY but not served) lose 8 satisfaction.
  At 0 they leave: the order is cancelled and the table freed.

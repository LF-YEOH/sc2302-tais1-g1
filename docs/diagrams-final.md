# Final diagrams (Stage 3)

Sources for the diagrams in the report. PNGs: `class-diagram-final.png`, `sequence-diagram.png`. Rendered with Mermaid.

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
            +showWelcome() void
            +showTurnStart(turn : int, board : Scoreboard, queue : ArrayList~Customer~, tables : ArrayList~Table~) void
            +showAvailableTasks(toSeat : ArrayList~Customer~, toOrder : ArrayList~Customer~, toServe : ArrayList~Customer~, toCook : ArrayList~Customer~) void
            +askWaiterTask() WaiterTask
            +askActivateHappyHour() boolean
            +showHappyHourStatus(happyHour : HappyHour) void
            +askCustomerId(prompt : String) int
            +showOrderPlaced(customer : Customer) void
            +showMessage(message : String) void
            +showError(message : String) void
            +showTurnEnd(turn : int, board : Scoreboard) void
            +showResult(board : Scoreboard) void
        }
    }
    namespace control {
        class ArrivalSchedule {
            <<control>>
            -ARRIVAL_TURNS : int[] = 1, 4, 7, 10, 13, 16$
            -CRITIC_ID : int = 2$
            -VIP_ID : int = 3$
            -nextId : int
            +hasArrival(turn : int) boolean
            +createCustomer() Customer
        }
        class GameController {
            <<control>>
            +MAX_TURNS : int = 18$
            -NUM_TABLES : int = 2$
            -turn : int
            -ui : RestaurantUI
            -menu : Menu
            -tables : ArrayList~Table~
            -customers : ArrayList~Customer~
            -waitingQueue : ArrayList~Customer~
            -host : Host
            -waiter : Waiter
            -chef : Chef
            -cashier : Cashier
            -scoreboard : Scoreboard
            -schedule : ArrivalSchedule
            -happyHour : HappyHour
            +GameController(ui : RestaurantUI)
            +run() void
            -playTurn() void
            -admitArrival() void
            -offerHappyHour() void
            -applyHappyHourRecovery() void
            -runHostStep() void
            -runWaiterStep() void
            -runChefStep() void
            -runPaymentStep() void
            -runEatingStep() void
            -runWaitingStep() void
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
        class Cashier {
            <<entity>>
            +collectPayment(customer : Customer) int
        }
        class Chef {
            <<entity>>
            +cook(order : Order) void
        }
        class ComboMeal {
            <<entity>>
            -COMBO_PRICE_PERCENT : int = 90$
            -firstItem : MenuItem
            -secondItem : MenuItem
            +ComboMeal(firstItem : MenuItem, secondItem : MenuItem)
            +getFirstItem() MenuItem
            +getSecondItem() MenuItem
        }
        class CriticCustomer {
            <<entity>>
            -CRITIC_SATISFACTION_LOSS : int = 12$
            -REVIEW_THRESHOLD : int = 50$
            -REVIEW_EFFECT : int = 20$
            +CriticCustomer(id : int)
            #getSatisfactionLoss() int
            #reactToService() void
            +toString() String
        }
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
            +getId() int
            +getSatisfaction() int
            +getStatus() CustomerStatus
            +getOrder() Order
            +getTable() Table
            +getReadyToPayTurn() int
            +chooseItem(menu : Menu) MenuItem
            +sitAt(table : Table) void
            +placeOrder(order : Order) void
            +receiveFood(turn : int) void
            +finishEating(turn : int) boolean
            +pay() int
            +isWaitingForService() boolean
            +loseSatisfaction() void
            +recoverSatisfaction(amount : int) void
            +hasRunOutOfPatience() boolean
            +leave() void
            #getSatisfactionLoss() int
            +getDiscountedPrice(priceCents : int) int
            #reactToService() void
            #adjustSatisfaction(amount : int) void
            +toString() String
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
        class HappyHour {
            <<entity>>
            -DURATION_TURNS : int = 2$
            +RECOVERY : int = 15$
            -used : boolean
            -turnsRemaining : int
            -standardPricing : PricingPolicy
            -happyHourPricing : PricingPolicy
            +HappyHour()
            +canActivate() boolean
            +activate() void
            +isActive() boolean
            +getTurnsRemaining() int
            +endTurn() void
            +getCurrentPricing() PricingPolicy
        }
        class HappyHourPricing {
            <<entity>>
            -HAPPY_HOUR_PRICE_PERCENT : int = 80$
            +apply(priceCents : int) int
        }
        class Host {
            <<entity>>
        }
        class InvalidActionException {
            <<entity>>
            +InvalidActionException(message : String)
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
            +toString() String
        }
        class Money {
            <<entity>>
            +format(cents : int)$ String
            +applyPercent(cents : int, percent : int)$ int
        }
        class Order {
            <<entity>>
            -item : MenuItem
            -priceCents : int
            -unitsDone : int
            -status : OrderStatus
            +Order(item : MenuItem, priceCents : int)
            +getItem() MenuItem
            +getPriceCents() int
            +getUnitsDone() int
            +getStatus() OrderStatus
            +isReady() boolean
            +addPreparationUnit() void
            +markServed() void
            +markPaid() void
            +cancel() void
            +toString() String
        }
        class OrderStatus {
            <<enumeration>>
            PREPARING
            READY
            SERVED
            PAID
            CANCELLED
        }
        class PricingPolicy {
            <<interface>>
            +apply(priceCents : int) int
        }
        class Scoreboard {
            <<entity>>
            +MIN_PAID : int = 4$
            +MIN_REVENUE_CENTS : int = 4500$
            +MIN_AVG_SATISFACTION : int = 60$
            -revenueCents : int
            -paidCount : int
            -totalPaidSatisfaction : int
            -servedCount : int
            -departures : int
            +recordPayment(amountCents : int, satisfaction : int) void
            +recordServed() void
            +recordDeparture() void
            +getRevenueCents() int
            +getPaidCount() int
            +getServedCount() int
            +getDepartures() int
            +hasPaidCustomers() boolean
            +getAverageSatisfaction() double
            +isVictory() boolean
            +isPaidTargetMet() boolean
            +isRevenueTargetMet() boolean
            +isSatisfactionTargetMet() boolean
        }
        class SeatingStaff["SeatingStaff {abstract}"] {
            <<entity>>
            +seat(customer : Customer, table : Table) void
        }
        class StandardPricing {
            <<entity>>
            +apply(priceCents : int) int
        }
        class Table {
            <<entity>>
            -number : int
            -occupant : Customer
            +Table(number : int)
            +getNumber() int
            +getOccupant() Customer
            +isFree() boolean
            +seat(customer : Customer) void
            +clear() void
        }
        class VIPCustomer {
            <<entity>>
            -VIP_SATISFACTION_LOSS : int = 5$
            -VIP_PRICE_PERCENT : int = 90$
            +VIPCustomer(id : int)
            #getSatisfactionLoss() int
            +getDiscountedPrice(priceCents : int) int
            +toString() String
        }
        class Waiter {
            <<entity>>
            +takeOrder(customer : Customer, menu : Menu, pricing : PricingPolicy) Order
            +serve(customer : Customer, turn : int) void
        }
    }
    class Main {
        +main(args : String[])$ void
    }
    class Exception {
        <<Java library>>
    }
    note for Customer "Encapsulation: satisfaction is private and only<br/>changed through adjustSatisfaction(), which keeps it in 0..100.<br/>Polymorphism: VIPCustomer and CriticCustomer override<br/>getSatisfactionLoss(), getDiscountedPrice() and reactToService()"
    note for PricingPolicy "Polymorphism (DIP/OCP): the Waiter applies whichever<br/>policy it is given; Happy Hour is a new class, not an if"
    note for Menu "Composition: the Menu creates and owns its items;<br/>ComboMeal is-a MenuItem and is made of two (aggregation)"
    note for SeatingStaff "Inheritance: Host and Waiter share one copy of the<br/>seating rules; the Chef and Cashier cannot seat"

    Main ..> RestaurantUI : creates
    Main ..> GameController : creates
    GameController "1" --> "1" RestaurantUI : ui
    GameController "1" *-- "1" Menu : menu
    GameController "1" *-- "2" Table : tables
    GameController "1" *-- "1" Host
    GameController "1" *-- "1" Waiter
    GameController "1" *-- "1" HappyHour
    GameController "1" *-- "1" Chef
    GameController "1" *-- "1" Cashier
    GameController "1" *-- "1" Scoreboard
    GameController "1" *-- "1" ArrivalSchedule : schedule
    GameController "1" --> "0..6" Customer : customers
    GameController "1" --> "0..*" Customer : waitingQueue {ordered}
    Menu "1" *-- "6" MenuItem : items
    Customer "1" --> "0..1" Order : order
    Customer "0..1" -- "0..1" Table : seated at
    Order "0..*" --> "1" MenuItem : item
    Customer --> CustomerStatus : status
    Order --> OrderStatus : status
    ArrivalSchedule ..> Customer : creates
    Waiter ..> Order : creates
    SeatingStaff ..> InvalidActionException : throws
    Chef ..> InvalidActionException : throws
    Cashier ..> InvalidActionException : throws
    InvalidActionException --|> Exception
    RestaurantUI ..> Scoreboard : displays
    RestaurantUI ..> WaiterTask : returns
    Customer <|-- VIPCustomer
    Customer <|-- CriticCustomer
    ArrivalSchedule ..> VIPCustomer : creates
    ArrivalSchedule ..> CriticCustomer : creates
    VIPCustomer ..> Money : applyPercent
    SeatingStaff <|-- Host
    SeatingStaff <|-- Waiter
    MenuItem <|-- ComboMeal
    ComboMeal "0..*" o-- "2" MenuItem : made of
    PricingPolicy <|.. StandardPricing
    PricingPolicy <|.. HappyHourPricing
    HappyHour "1" *-- "2" PricingPolicy
    Waiter ..> PricingPolicy : applies
    HappyHour ..> InvalidActionException : throws
    RestaurantUI ..> HappyHour : displays
    ComboMeal ..> Money : applyPercent
    HappyHourPricing ..> Money : applyPercent
```

## Sequence diagram: a VIP served during Happy Hour

```mermaid
sequenceDiagram
    autonumber
    actor M as Manager
    participant ui as :RestaurantUI
    participant gc as :GameController
    participant sch as :ArrivalSchedule
    participant hh as :HappyHour
    participant host as :Host
    participant t as t1 : Table
    participant w as :Waiter
    participant ch as :Chef
    participant cs as :Cashier
    participant sb as :Scoreboard

    Note over M,sb: Scenario: the VIP (C3) arrives on turn 7 and the manager activates Happy Hour, so the VIP pays $12.96 instead of $16.20

    loop turn = 1 to MAX_TURNS (18), only turns 7 to 11 shown
    rect rgb(245,245,245)
    Note over M,sb: Turn 7: arrival, Happy Hour, seating, order
    gc->>+sch: hasArrival(7)
    sch-->>-gc: true
    gc->>+sch: createCustomer()
    create participant c3 as c3 : VIPCustomer
    sch->>c3: «create» VIPCustomer(3)
    sch-->>-gc: c3
    gc->>ui: showTurnStart(7, sb, waitingQueue, tables)
    gc->>+hh: canActivate()
    hh-->>-gc: true
    gc->>+ui: askActivateHappyHour()
    ui->>M: "Activate Happy Hour now?"
    M-->>ui: 1 (Yes)
    ui-->>-gc: true
    alt manager chooses Yes
        gc->>hh: activate()
        Note right of hh: used = true, turnsRemaining = 2
    else manager chooses No
        Note right of gc: Standard pricing stays in force:<br/>C3 would pay $16.20
    end
    opt hh.isActive()
        loop for each customer
            gc->>c3: recoverSatisfaction(15)
        end
    end
    gc->>+ui: askCustomerId("Host: seat which customer?")
    M-->>ui: 3
    ui-->>-gc: 3
    gc->>+host: seat(c3, t1)
    Note right of host: inherited from SeatingStaff:<br/>checks c3 is WAITING and t1 is free
    host->>+t: seat(c3)
    t->>c3: sitAt(t1)
    t-->>-host: 
    host-->>-gc: 
    gc->>+ui: askWaiterTask()
    M-->>ui: 2 (Take order), customer 3
    ui-->>-gc: TAKE_ORDER
    gc->>+hh: getCurrentPricing()
    hh-->>-gc: happyHourPricing
    gc->>+w: takeOrder(c3, menu, happyHourPricing)
    w->>+c3: chooseItem(menu)
    c3-->>-w: Pasta
    w->>+c3: getDiscountedPrice(1800)
    Note right of c3: VIP override: 90%
    c3-->>-w: 1620
    w->>w: pricing.apply(1620) = 1296
    Note right of w: Happy Hour 80%, applied after the VIP discount
    create participant o as o : Order
    w->>o: «create» Order(Pasta, 1296)
    w->>c3: placeOrder(o)
    w-->>-gc: o
    gc->>+ui: askCustomerId("Chef: cook whose order?")
    M-->>ui: 3
    ui-->>-gc: 3
    gc->>+ch: cook(o)
    ch->>o: addPreparationUnit()
    Note right of o: 1/2 PREPARING
    ch-->>-gc: 
    gc->>hh: endTurn()
    end

    rect rgb(245,245,245)
    Note over M,sb: Turn 8: the Chef finishes the Pasta
    gc->>+ui: askCustomerId("Chef: cook whose order?")
    M-->>ui: 3
    ui-->>-gc: 3
    gc->>+ch: cook(o)
    ch->>o: addPreparationUnit()
    Note right of o: 2/2 READY
    ch-->>-gc: 
    gc->>hh: endTurn()
    Note right of hh: turnsRemaining = 0 (Happy Hour over, o keeps its price)
    end

    rect rgb(245,245,245)
    Note over M,sb: Turn 9: READY then SERVED
    gc->>+ui: askWaiterTask()
    M-->>ui: 3 (Serve), customer 3
    ui-->>-gc: SERVE
    gc->>+w: serve(c3, 9)
    w->>+o: isReady()
    o-->>-w: true
    w->>c3: receiveFood(9)
    c3->>o: markServed()
    Note right of o: SERVED
    w-->>-gc: 
    gc->>sb: recordServed()
    end

    rect rgb(245,245,245)
    Note over M,sb: Turn 10: eating finishes
    loop for each customer
        gc->>+c3: finishEating(10)
        c3-->>-gc: true (READY_TO_PAY)
    end
    end

    rect rgb(245,245,245)
    Note over M,sb: Turn 11: payment
    gc->>+cs: collectPayment(c3)
    cs->>+c3: pay()
    c3->>c3: reactToService()
    c3->>o: markPaid()
    c3->>t: clear()
    c3-->>-cs: 1296
    cs-->>-gc: 1296
    gc->>sb: recordPayment(1296, 95)
    end

    end
    rect rgb(245,245,245)
    Note over M,sb: After turn 18: result check
    gc->>+ui: showResult(sb)
    ui->>+sb: isVictory()
    sb->>sb: isPaidTargetMet() && isRevenueTargetMet()<br/>&& isSatisfactionTargetMet()
    sb-->>-ui: true
    ui->>M: "VICTORY!" ($82.86, average 89.4)
    ui-->>-gc: 
    end
```

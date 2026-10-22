package restaurant.entity;

/**
 * Represents one Regular customer. A customer knows its satisfaction, its
 * current status, its order and its table, and is the only class that
 * changes its own status. Staff classes check the game rules and then call
 * these methods.
 *
 * Owner: A
 */
public class Customer {

    /** Satisfaction every customer starts with. */
    private static final int START_SATISFACTION = 100;
    /** Satisfaction lost per turn while waiting to be served. */
    private static final int SATISFACTION_LOSS = 8;

    private int id;
    private int satisfaction;
    private CustomerStatus status;
    private Order order;          // null until an order is placed
    private Table table;          // null when not seated
    private int servedTurn;       // turn the food was served; -1 if not served
    private int readyToPayTurn;   // turn the customer became READY_TO_PAY; -1 if not yet

    /**
     * Creates a newly arrived customer: satisfaction 100, status WAITING,
     * no order, no table, servedTurn and readyToPayTurn both -1.
     *
     * @param id the customer's number, starting from 1 (shown as "C1")
     */
    public Customer(int id) {
        // TODO (A): implement
    }

    /** @return the customer's number, e.g. 1 for "C1" */
    public int getId() {
        return 0; // TODO (A)
    }

    /** @return current satisfaction, from 0 to 100 */
    public int getSatisfaction() {
        return 0; // TODO (A)
    }

    /** @return the customer's current status */
    public CustomerStatus getStatus() {
        return null; // TODO (A)
    }

    /** @return the customer's order, or null if none has been placed */
    public Order getOrder() {
        return null; // TODO (A)
    }

    /** @return the table the customer sits at, or null if not seated */
    public Table getTable() {
        return null; // TODO (A)
    }

    /** @return the turn the customer became READY_TO_PAY, or -1 if not yet */
    public int getReadyToPayTurn() {
        return 0; // TODO (A)
    }

    /**
     * Chooses what this customer orders. Rule: customer n takes menu item
     * number (n - 1) % (number of items), so C1 Salad, C2 Burger, C3 Pasta,
     * C4 Salad, and so on. Does not change any state.
     *
     * @param menu the restaurant's menu
     * @return the chosen menu item
     */
    public MenuItem chooseItem(Menu menu) {
        return null; // TODO (A)
    }

    /**
     * Records that the customer has sat down: stores the table and sets
     * status to SEATED. Called only by Table.seat(), which keeps the
     * table-customer link consistent in both directions.
     *
     * @param table the table the customer now sits at
     */
    public void sitAt(Table table) {
        // TODO (A): implement
    }

    /**
     * Records the customer's order and sets status to ORDERED.
     * Precondition (checked by Waiter): status is SEATED.
     *
     * @param order the newly created order
     */
    public void placeOrder(Order order) {
        // TODO (A): implement
    }

    /**
     * Receives the food: marks the order SERVED (order.markServed()),
     * remembers the turn in servedTurn, and sets status to EATING.
     * Precondition (checked by Waiter): status is ORDERED and the order is READY.
     *
     * @param turn the current turn number
     */
    public void receiveFood(int turn) {
        // TODO (A): implement
    }

    /**
     * Finishes eating if the customer was served on an EARLIER turn:
     * if status is EATING and servedTurn < turn, sets status to
     * READY_TO_PAY, stores readyToPayTurn = turn and returns true.
     * Otherwise changes nothing and returns false.
     *
     * @param turn the current turn number
     * @return true if the customer became READY_TO_PAY this turn
     */
    public boolean finishEating(int turn) {
        return false; // TODO (A)
    }

    /**
     * Pays the bill: marks the order PAID (order.markPaid()), sets status to
     * PAID, frees the table (table.clear()) and forgets it (table = null).
     * Satisfaction is NOT changed; the caller records it.
     * Precondition (checked by Cashier): status is READY_TO_PAY.
     *
     * @return the bill in cents (the order's locked price)
     */
    public int pay() {
        return 0; // TODO (A)
    }

    /**
     * @return true if the customer is still waiting for a table or for food,
     *         i.e. status is WAITING, SEATED or ORDERED (including an order
     *         that is READY but not yet served)
     */
    public boolean isWaitingForService() {
        return false; // TODO (A)
    }

    /**
     * Applies one turn of waiting: if isWaitingForService(), lowers
     * satisfaction by SATISFACTION_LOSS (8), but never below 0.
     * Does nothing for customers who are eating, ready to pay, paid or left.
     */
    public void loseSatisfaction() {
        // TODO (A): implement
    }

    /** @return true if the customer is still waiting for service and satisfaction is 0 */
    public boolean hasRunOutOfPatience() {
        return false; // TODO (A)
    }

    /**
     * Leaves without paying: cancels the order if there is one
     * (order.cancel()), frees the table if seated (table.clear(), then
     * table = null), and sets status to LEFT.
     * The controller removes the customer from the waiting queue.
     */
    public void leave() {
        // TODO (A): implement
    }

    /** @return the customer's label, e.g. "C3" */
    @Override
    public String toString() {
        return "C" + id;
    }
}

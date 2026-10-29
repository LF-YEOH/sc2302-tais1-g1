package restaurant.entity;

/**
 * Represents a customer. A customer knows its satisfaction, its current
 * status, its order and its table, and is the only class that changes its
 * own status. Staff classes check the game rules and then call these methods.
 *
 * Customer objects are Regular customers. VIPCustomer and CriticCustomer
 * extend this class and change its behaviour only by overriding
 * getSatisfactionLoss(), getDiscountedPrice(), reactToService() and
 * toString(), so no other class needs to check a customer's type.
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
        this.id = id;
        this.satisfaction = START_SATISFACTION;
        this.status = CustomerStatus.WAITING;
        this.order = null;
        this.table = null;
        this.servedTurn = -1;
        this.readyToPayTurn = -1;
    }

    /** @return the customer's number, e.g. 1 for "C1" */
    public int getId() {
        return id; 
    }

    /** @return current satisfaction, from 0 to 100 */
    public int getSatisfaction() {
        return satisfaction; 
    }

    /** @return the customer's current status */
    public CustomerStatus getStatus() {
        return status; 
    }

    /** @return the customer's order, or null if none has been placed */
    public Order getOrder() {
        return order;
    }

    /** @return the table the customer sits at, or null if not seated */
    public Table getTable() {
        return table;
    }

    /** @return the turn the customer became READY_TO_PAY, or -1 if not yet */
    public int getReadyToPayTurn() {
        return readyToPayTurn;
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
        int itemNumber = (id - 1) % (menu.getNumItems());
        return menu.getItem(itemNumber);
    }

    /**
     * Records that the customer has sat down: stores the table and sets
     * status to SEATED. Called only by Table.seat(), which keeps the
     * table-customer link consistent in both directions.
     *
     * @param table the table the customer now sits at
     */
    public void sitAt(Table table) {
        this.table = table;
        this.status = CustomerStatus.SEATED;
    }

    /**
     * Records the customer's order and sets status to ORDERED.
     * Precondition (checked by Waiter): status is SEATED.
     *
     * @param order the newly created order
     */
    public void placeOrder(Order order) {
        this.order = order;
        this.status = CustomerStatus.ORDERED;
    }

    /**
     * Receives the food: marks the order SERVED (order.markServed()),
     * remembers the turn in servedTurn, and sets status to EATING.
     * Precondition (checked by Waiter): status is ORDERED and the order is READY.
     *
     * @param turn the current turn number
     */
    public void receiveFood(int turn) {
        this.order.markServed();
        this.servedTurn = turn;
        this.status = CustomerStatus.EATING;
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
        if(this.servedTurn < turn && this.status == CustomerStatus.EATING){
            this.status = CustomerStatus.READY_TO_PAY;
            this.readyToPayTurn = turn;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Pays the bill: first calls reactToService() (a customer type may adjust
     * its satisfaction here, e.g. a Critic), then marks the order PAID
     * (order.markPaid()), sets status to PAID, frees the table (table.clear())
     * and forgets it (table = null). The caller records getSatisfaction()
     * after pay() returns, so any reaction is included.
     * Precondition (checked by Cashier): status is READY_TO_PAY.
     *
     * @return the bill in cents (the order's locked price)
     */
    public int pay() {
        reactToService();
        this.order.markPaid();
        this.status = CustomerStatus.PAID;
        this.table.clear();
        this.table = null;
        return this.order.getPriceCents();
    }

    /**
     * @return true if the customer is still waiting for a table or for food,
     *         i.e. status is WAITING, SEATED or ORDERED (including an order
     *         that is READY but not yet served)
     */
    public boolean isWaitingForService() {
        if(status == CustomerStatus.WAITING || status == CustomerStatus.SEATED || status == CustomerStatus.ORDERED){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Applies one turn of waiting: if isWaitingForService(), lowers
     * satisfaction by getSatisfactionLoss(), but never below 0.
     * Does nothing for customers who are eating, ready to pay, paid or left.
     */
    public void loseSatisfaction() {
        if(isWaitingForService()){
            adjustSatisfaction(-getSatisfactionLoss());
        }
    }

    /**
     * Happy Hour recovery: if isWaitingForService(), raises satisfaction by
     * the given amount, up to 100. Does nothing for other customers.
     *
     * @param amount points to recover
     */
    public void recoverSatisfaction(int amount) {
        if (isWaitingForService()) {
            adjustSatisfaction(amount);
        }
    }

    /** @return true if the customer is still waiting for service and satisfaction is 0 */
    public boolean hasRunOutOfPatience() {
       if(isWaitingForService() && satisfaction == 0){
            return true;
       }else{
            return false;
       }
    }

    /**
     * Leaves without paying: cancels the order if there is one
     * (order.cancel()), frees the table if seated (table.clear(), then
     * table = null), and sets status to LEFT.
     * The controller removes the customer from the waiting queue.
     */
    public void leave() {
        if(order != null){
            order.cancel();
        }
        if(table != null){
            table.clear();
            table = null;
        }
        status = CustomerStatus.LEFT;
    }

    /**
     * Satisfaction this customer loses for each turn spent waiting for
     * service. Regular customers lose SATISFACTION_LOSS (8).
     * Subclasses override this to change the rate (VIP 5, Critic 12).
     *
     * @return points lost per waiting turn
     */
    protected int getSatisfactionLoss() {
        return SATISFACTION_LOSS;
    }

    /**
     * The price this customer pays for a menu item, before any restaurant-wide
     * pricing. Regular customers pay the full price. Called by the Waiter
     * when taking the order; the result is locked into the Order.
     * Subclasses override this to give a discount (VIP: 10% off).
     *
     * @param priceCents the item's menu price in cents
     * @return the price this customer pays, in cents
     */
    public int getDiscountedPrice(int priceCents) {
        return priceCents;
    }

    /**
     * The customer's reaction to the service, applied when paying (called
     * by pay() before the satisfaction is recorded). Regular and VIP
     * customers do not react, so this does nothing.
     * Subclasses override it to react (Critic: +20 or -20).
     */
    protected void reactToService() {
        // Regular customers do not react.
    }

    /**
     * Changes satisfaction by the given amount (negative to lower it),
     * keeping it within 0 to 100. The only way subclasses can change
     * satisfaction, so the 0-100 limits are always enforced here.
     *
     * @param amount points to add (or subtract, if negative)
     */
    protected void adjustSatisfaction(int amount) {
        satisfaction+=amount;
        satisfaction = Math.max(0,Math.min(satisfaction,100));
    }

    /** @return the customer's label, e.g. "C3" */
    @Override
    public String toString() {
        return "C" + id;
    }
}

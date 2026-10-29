package restaurant.entity;

/**
 * Represents one customer's order: the chosen menu item, the price locked
 * when the order was taken, and how many preparation units are done.
 * The order becomes READY by itself when its last unit is done.
 *
 * Owner: B
 */
public class Order {

    private MenuItem item;
    private int priceCents;   // locked when the order is created
    private int unitsDone = 0;
    private OrderStatus status = OrderStatus.PREPARING;

    /**
     * Creates a new order and locks its price. The price is decided by the
     * Waiter when the order is taken (the customer's price for the item,
     * e.g. 90% for a VIP) and can never change afterwards. unitsDone starts
     * at 0 and status at PREPARING.
     *
     * @param item       the menu item the customer chose
     * @param priceCents the price this customer pays, in cents
     * @throws IllegalArgumentException if priceCents is not positive
     */
    public Order(MenuItem item, int priceCents) {
        if (priceCents <= 0) {
            throw new IllegalArgumentException("Order price must be positive: " + priceCents);
        }
        this.item = item;
        this.priceCents = priceCents;
    }

    /** @return the ordered menu item */
    public MenuItem getItem() {
        return this.item;
    }

    /** @return the bill for this order in cents */
    public int getPriceCents() {
        return this.priceCents;
    }

    /** @return the number of preparation units completed */
    public int getUnitsDone() {
        return this.unitsDone;
    }

    /** @return the order's current status */
    public OrderStatus getStatus() {
        return this.status; 
    }

    /** @return true if status is READY */
    public boolean isReady() {
        return (this.status == OrderStatus.READY); 
    }

    /**
     * Completes one preparation unit: increases unitsDone by 1, and if it
     * now equals the item's preparation units, sets status to READY.
     * Precondition (checked by Chef): status is PREPARING.
     */
    public void addPreparationUnit() {
        this.unitsDone+=1;
        if (this.unitsDone == this.item.getPrepUnits()) {this.status = OrderStatus.READY;}
    }

    /** Sets status to SERVED. Precondition: status is READY. */
    public void markServed() {
        
        this.status = OrderStatus.SERVED;
    }

    /** Sets status to PAID. Precondition: status is SERVED. */
    public void markPaid() {
        this.status = OrderStatus.PAID;
    }

    /**
     * Cancels the order because its customer left: sets status to
     * CANCELLED if it is PREPARING or READY; otherwise does nothing.
     */
    public void cancel() {
        if (this.status == OrderStatus.PREPARING || this.status == OrderStatus.READY) {this.status = OrderStatus.CANCELLED;}
    }

    /** @return a readable summary, e.g. "Pasta 1/2 PREPARING" */
    @Override
    public String toString() {
        return (this.item.getName() + " " + this.unitsDone + "/" + this.item.getPrepUnits() + " " + this.status);
    }
}

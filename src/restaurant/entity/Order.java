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
    private int unitsDone;
    private OrderStatus status;

    /**
     * Creates a new order: locks the price at item.getPriceCents(),
     * sets unitsDone to 0 and status to PREPARING.
     *
     * @param item the menu item the customer chose
     */
    public Order(MenuItem item) {
        // TODO (B): implement
    }

    /** @return the ordered menu item */
    public MenuItem getItem() {
        return null; // TODO (B)
    }

    /** @return the bill for this order in cents */
    public int getPriceCents() {
        return 0; // TODO (B)
    }

    /** @return the number of preparation units completed */
    public int getUnitsDone() {
        return 0; // TODO (B)
    }

    /** @return the order's current status */
    public OrderStatus getStatus() {
        return null; // TODO (B)
    }

    /** @return true if status is READY */
    public boolean isReady() {
        return false; // TODO (B)
    }

    /**
     * Completes one preparation unit: increases unitsDone by 1, and if it
     * now equals the item's preparation units, sets status to READY.
     * Precondition (checked by Chef): status is PREPARING.
     */
    public void addPreparationUnit() {
        // TODO (B): implement
    }

    /** Sets status to SERVED. Precondition: status is READY. */
    public void markServed() {
        // TODO (B): implement
    }

    /** Sets status to PAID. Precondition: status is SERVED. */
    public void markPaid() {
        // TODO (B): implement
    }

    /**
     * Cancels the order because its customer left: sets status to
     * CANCELLED if it is PREPARING or READY; otherwise does nothing.
     */
    public void cancel() {
        // TODO (B): implement
    }

    /** @return a readable summary, e.g. "Pasta 1/2 PREPARING" */
    @Override
    public String toString() {
        return ""; // TODO (B)
    }
}

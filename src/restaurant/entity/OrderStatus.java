package restaurant.entity;

/**
 * The stages an order passes through.
 * Normal path: PREPARING -> READY -> SERVED -> PAID.
 * An order whose customer leaves before being served becomes CANCELLED.
 */
public enum OrderStatus {
    /** The Chef has not finished all preparation units yet. */
    PREPARING,
    /** All preparation units are done; waiting for the Waiter to serve it. */
    READY,
    /** Served to the customer. */
    SERVED,
    /** The customer has paid for it. */
    PAID,
    /** The customer left before it was served. */
    CANCELLED
}

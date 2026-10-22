package restaurant.entity;

/**
 * The stages a customer passes through during the game.
 * Normal path: WAITING -> SEATED -> ORDERED -> EATING -> READY_TO_PAY -> PAID.
 * A customer who runs out of patience before being served becomes LEFT.
 */
public enum CustomerStatus {
    /** In the waiting queue, not yet seated. */
    WAITING,
    /** Seated at a table, has not ordered yet. */
    SEATED,
    /** Has ordered; the food is being prepared or is READY but not yet served. */
    ORDERED,
    /** Has been served and is eating. */
    EATING,
    /** Finished eating and waiting for the Cashier. */
    READY_TO_PAY,
    /** Has paid and left happily. */
    PAID,
    /** Ran out of patience and left without paying. */
    LEFT
}

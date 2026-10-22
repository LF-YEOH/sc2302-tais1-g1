package restaurant.control;

/**
 * The choices the manager can give the Waiter in one turn.
 */
public enum WaiterTask {
    /** Seat one queued customer at a free table. */
    SEAT,
    /** Take one seated customer's order. */
    TAKE_ORDER,
    /** Serve one READY dish. */
    SERVE,
    /** Do nothing this turn. */
    WAIT
}

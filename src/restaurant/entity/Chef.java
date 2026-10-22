package restaurant.entity;

/**
 * The Chef advances one order by one preparation unit per turn.
 *
 * Owner: C
 */
public class Chef {

    /**
     * Prepares one unit of an order by calling order.addPreparationUnit().
     * Refuses if order is null ("That customer has no order.") or the
     * order's status is not PREPARING (e.g. "The Burger is already READY.").
     *
     * @param order the order to work on
     * @throws InvalidActionException if the order cannot be cooked
     */
    public void cook(Order order) throws InvalidActionException {
        // TODO (C): implement
    }
}

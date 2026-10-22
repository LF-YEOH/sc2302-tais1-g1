package restaurant.control;

import restaurant.entity.Customer;

/**
 * Decides when customers arrive and creates them. In the base setting one
 * customer arrives at the start of turns 1, 4, 7, 10, 13 and 16.
 *
 * Owner: Lead
 */
public class ArrivalSchedule {

    /** The turns on which a customer arrives. */
    private static final int[] ARRIVAL_TURNS = {1, 4, 7, 10, 13, 16};

    private int nextId = 1;

    /**
     * @param turn the current turn number
     * @return true if a customer arrives at the start of this turn
     */
    public boolean hasArrival(int turn) {
        return false; // TODO (Lead)
    }

    /**
     * Creates the next arriving customer, numbered 1, 2, 3, ... in order.
     *
     * @return a new customer with status WAITING and satisfaction 100
     */
    public Customer createCustomer() {
        return null; // TODO (Lead)
    }
}

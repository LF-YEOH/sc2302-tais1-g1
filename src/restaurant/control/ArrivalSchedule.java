package restaurant.control;

import restaurant.entity.CriticCustomer;
import restaurant.entity.Customer;
import restaurant.entity.VIPCustomer;

/**
 * Decides when customers arrive and creates them. In the base setting one
 * customer arrives at the start of turns 1, 4, 7, 10, 13 and 16.
 *
 * This is the only class that names the concrete customer types: customer 2
 * is a Critic and customer 3 a VIP; the others are Regular (see DECISIONS.md
 * D19). After creation, no other class checks a customer's type.
 *
 * Owner: Lead
 */
public class ArrivalSchedule {

    /** The turns on which a customer arrives. */
    private static final int[] ARRIVAL_TURNS = {1, 4, 7, 10, 13, 16};

    /** The customer number that arrives as a Critic. */
    private static final int CRITIC_ID = 2;
    /** The customer number that arrives as a VIP. */
    private static final int VIP_ID = 3;

    private int nextId = 1;

    /**
     * @param turn the current turn number
     * @return true if a customer arrives at the start of this turn
     */
    public boolean hasArrival(int turn) {
        for (int arrivalTurn : ARRIVAL_TURNS) {
            if (arrivalTurn == turn) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates the next arriving customer, numbered 1, 2, 3, ... in order:
     * a CriticCustomer for CRITIC_ID, a VIPCustomer for VIP_ID, and a
     * Regular Customer otherwise.
     *
     * @return a new customer with status WAITING and satisfaction 100
     */
    public Customer createCustomer() {
        Customer customer;
        if (nextId == CRITIC_ID) {
            customer = new CriticCustomer(nextId);
        } else if (nextId == VIP_ID) {
            customer = new VIPCustomer(nextId);
        } else {
            customer = new Customer(nextId);
        }
        nextId++;
        return customer;
    }
}

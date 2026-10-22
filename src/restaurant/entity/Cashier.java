package restaurant.entity;

/**
 * The Cashier charges a customer who is ready to pay. The controller
 * decides who pays (at most one per turn, oldest first).
 *
 * Owner: C
 */
public class Cashier {

    /**
     * Charges the customer by calling customer.pay().
     * Refuses if customer is null or the status is not READY_TO_PAY.
     *
     * @param customer the customer paying
     * @return the amount paid, in cents
     * @throws InvalidActionException if the customer cannot pay
     */
    public int collectPayment(Customer customer) throws InvalidActionException {
        return 0; // TODO (C)
    }
}

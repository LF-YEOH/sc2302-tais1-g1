package restaurant.entity;

/**
 * A member of staff who can seat customers: the Host and the Waiter. Holds
 * the seating rules in one place, so both seat customers in exactly the same
 * way. The Chef and Cashier cannot seat anyone, so they do not extend this
 * class. Abstract because there is no "general" seating staff in the game,
 * only Hosts and Waiters.
 *
 * Owner: C
 */
public abstract class SeatingStaff {

    /**
     * Seats a waiting customer at a table by calling table.seat(customer).
     * Refuses (with a message) if: customer is null ("There is no such
     * customer."), the customer's status is not WAITING, the table is null
     * ("No table is free."), or the table is not free.
     * The controller removes the customer from the waiting queue afterwards.
     *
     * @param customer the customer to seat (null if the id was not found)
     * @param table    the table to use (null if none is free)
     * @throws InvalidActionException if the customer cannot be seated
     */
    public void seat(Customer customer, Table table) throws InvalidActionException {
        if (customer == null) {
            throw new InvalidActionException("There is no such customer.");
        } else if (customer.getStatus() != CustomerStatus.WAITING) {
            String status = customer.getStatus().toString().toLowerCase();
            throw new InvalidActionException("Customer is already " + status + ".");
        } else if (table == null) {
            throw new InvalidActionException("No table is free.");
        } else if (!table.isFree()) {
            throw new InvalidActionException("Table is not free.");
        } else {
            table.seat(customer);
        }
    }
}

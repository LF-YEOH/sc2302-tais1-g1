package restaurant.entity;

/**
 * Represents one restaurant table. A table is either free or occupied by
 * exactly one customer, and stays occupied until that customer pays or leaves.
 *
 * Owner: B
 */
public class Table {

    private int number;
    private Customer occupant;   // null when free

    /**
     * Creates a free table.
     *
     * @param number the table number, starting from 1
     */
    public Table(int number) {
        // TODO (B): implement
    }

    /** @return the table number */
    public int getNumber() {
        return 0; // TODO (B)
    }

    /** @return the customer at this table, or null if free */
    public Customer getOccupant() {
        return null; // TODO (B)
    }

    /** @return true if no customer sits here */
    public boolean isFree() {
        return false; // TODO (B)
    }

    /**
     * Seats a customer: stores them as occupant, then calls
     * customer.sitAt(this) so both sides of the link are set together.
     * Precondition (checked by Waiter): the table is free.
     *
     * @param customer the customer to seat
     */
    public void seat(Customer customer) {
        // TODO (B): implement
    }

    /** Frees the table (occupant = null). Called by Customer.pay() and Customer.leave(). */
    public void clear() {
        // TODO (B): implement
    }
}

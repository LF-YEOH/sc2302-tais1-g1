package restaurant.entity;

/**
 * The Waiter performs one of three tasks: seat a queued customer, take a
 * seated customer's order, or serve a READY dish. Each method checks the
 * rules first and throws InvalidActionException, without changing
 * anything, if the task is not allowed.
 *
 * Owner: C
 */
public class Waiter {

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
        // TODO (C): implement
    }

    /**
     * Takes a seated customer's order: the customer chooses an item
     * (customer.chooseItem(menu)), a new Order is created for it and given
     * to the customer (customer.placeOrder(order)).
     * Refuses if customer is null or the customer's status is not SEATED.
     *
     * @param customer the customer ordering
     * @param menu     the restaurant's menu
     * @return the new order
     * @throws InvalidActionException if the order cannot be taken
     */
    public Order takeOrder(Customer customer, Menu menu) throws InvalidActionException {
        return null; // TODO (C)
    }

    /**
     * Serves a customer's READY dish by calling customer.receiveFood(turn).
     * Refuses if customer is null, the status is not ORDERED, or the order
     * is not READY (message should say how many units are done,
     * e.g. "C3's Pasta is not ready yet (1/2 units done).").
     *
     * @param customer the customer to serve
     * @param turn     the current turn number
     * @throws InvalidActionException if the dish cannot be served
     */
    public void serve(Customer customer, int turn) throws InvalidActionException {
        // TODO (C): implement
    }
}

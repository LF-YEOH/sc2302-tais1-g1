package restaurant.entity;

/**
 * The Waiter performs one of three tasks: seat a queued customer, take a
 * seated customer's order, or serve a READY dish. Each method checks the
 * rules first and throws InvalidActionException, without changing
 * anything, if the task is not allowed.
 *
 * Owner: C
 */
public class Waiter extends SeatingStaff {

    /**
     * Takes a seated customer's order: the customer chooses an item
     * (customer.chooseItem(menu)) and states its price for it
     * (customer.getDiscountedPrice(...)); a new Order is created with that
     * price locked in and given to the customer (customer.placeOrder(order)).
     * The Waiter never checks what type of customer it is serving.
     * Refuses if customer is null or the customer's status is not SEATED.
     *
     * @param customer the customer ordering
     * @param menu     the restaurant's menu
     * @param pricing  the restaurant-wide pricing for orders taken now
     * @return the new order
     * @throws InvalidActionException if the order cannot be taken
     */
    public Order takeOrder(Customer customer, Menu menu, PricingPolicy pricing) throws InvalidActionException {
        if(customer == null){
            throw new InvalidActionException("There is no such customer.");
        }else if(customer.getStatus() != CustomerStatus.SEATED){
            throw new InvalidActionException("Customer is not seated.");
        }else{
            MenuItem item = customer.chooseItem(menu);
            int price = pricing.apply(customer.getDiscountedPrice(item.getPriceCents()));
            Order order = new Order(item, price);
            customer.placeOrder(order);
            return order;
        }
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
        if(customer == null){
            throw new InvalidActionException("There is no such customer.");
        }else if(customer.getStatus() != CustomerStatus.ORDERED){
            String status = customer.getStatus().toString().toLowerCase();
            throw new InvalidActionException("Customer has no dish waiting to be served (customer is " + status + ").");
        }else{
            Order order = customer.getOrder();
            if(order.isReady()){
                customer.receiveFood(turn);
            }else{
                MenuItem item=order.getItem();
                throw new InvalidActionException(
                    customer.toString() + 
                    "'s " +
                    item.getName() +
                    " is not ready yet (" +
                    order.getUnitsDone() + 
                    "/" +
                    item.getPrepUnits() +
                    " units done)."
                );
            }
        }
    }
}

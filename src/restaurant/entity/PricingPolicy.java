package restaurant.entity;

/**
 * A restaurant-wide way of pricing new orders, applied after the customer's
 * own price (e.g. after a VIP's discount). The Waiter depends only on this
 * interface, so a new promotion is a new class with no change to the Waiter.
 *
 * Owner: Lead
 */
public interface PricingPolicy {

    /**
     * @param priceCents the customer's price for an item, in cents
     * @return the price to lock into the order, in cents
     */
    int apply(int priceCents);
}

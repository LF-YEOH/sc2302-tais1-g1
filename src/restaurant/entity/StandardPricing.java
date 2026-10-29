package restaurant.entity;

/**
 * Normal pricing: the customer's price is charged unchanged.
 *
 * Owner: Lead
 */
public class StandardPricing implements PricingPolicy {

    /** @return the price unchanged */
    @Override
    public int apply(int priceCents) {
        return priceCents;
    }
}

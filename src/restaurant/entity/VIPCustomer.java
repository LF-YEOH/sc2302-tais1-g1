package restaurant.entity;

/**
 * A VIP customer: more patient than a Regular customer (loses 5 satisfaction
 * per waiting turn instead of 8) and receives 10% off the bill. Otherwise
 * follows exactly the same order and service flow as every customer.
 *
 * Owner: A
 */
public class VIPCustomer extends Customer {

    /** Satisfaction lost per waiting turn. */
    private static final int VIP_SATISFACTION_LOSS = 5;
    /** Percentage of the menu price a VIP pays (10% off). */
    private static final int VIP_PRICE_PERCENT = 90;

    /**
     * Creates a newly arrived VIP customer (satisfaction 100, WAITING).
     *
     * @param id the customer's number, starting from 1
     */
    public VIPCustomer(int id) {
        super(id);
    }

    /** @return VIP_SATISFACTION_LOSS (5) */
    @Override
    protected int getSatisfactionLoss() {
        return 0; // TODO (A)
    }

    /**
     * @param priceCents the item's menu price in cents
     * @return 90% of the price, rounded to the nearest cent (Money has a
     *         helper for percentages)
     */
    @Override
    public int getDiscountedPrice(int priceCents) {
        return 0; // TODO (A)
    }

    /** @return the customer's label with its type, e.g. "C3 [VIP]" */
    @Override
    public String toString() {
        return ""; // TODO (A)
    }
}

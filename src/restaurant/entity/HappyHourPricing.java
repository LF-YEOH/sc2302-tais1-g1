package restaurant.entity;

/**
 * Happy Hour pricing: 20% off orders taken while Happy Hour is active.
 *
 * Owner: Lead
 */
public class HappyHourPricing implements PricingPolicy {

    /** Percentage of the price charged during Happy Hour (20% off). */
    private static final int HAPPY_HOUR_PRICE_PERCENT = 80;

    /** @return 80% of the price, rounded to the nearest cent */
    @Override
    public int apply(int priceCents) {
        return Money.applyPercent(priceCents, HAPPY_HOUR_PRICE_PERCENT);
    }
}

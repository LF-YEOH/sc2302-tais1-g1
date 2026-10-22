package restaurant.entity;

/**
 * Keeps the game's results: revenue, paid customers, their satisfaction at
 * payment, dishes served and unhappy departures. Also decides victory.
 *
 * Owner: B
 */
public class Scoreboard {

    /** Victory target: at least this many paid customers. */
    private static final int MIN_PAID = 4;
    /** Victory target: at least this much revenue, in cents ($45.00). */
    private static final int MIN_REVENUE_CENTS = 4500;
    /** Victory target: average satisfaction of paid customers at least this. */
    private static final int MIN_AVG_SATISFACTION = 60;

    private int revenueCents;
    private int paidCount;
    private int totalPaidSatisfaction;
    private int servedCount;
    private int departures;

    /**
     * Records one payment: adds the amount to revenue, adds 1 to paidCount
     * and adds the customer's satisfaction at payment to the total.
     *
     * @param amountCents  the bill paid, in cents
     * @param satisfaction the customer's satisfaction when paying
     */
    public void recordPayment(int amountCents, int satisfaction) {
        // TODO (B): implement
    }

    /** Records that one dish was served. */
    public void recordServed() {
        // TODO (B): implement
    }

    /** Records that one customer left without paying. */
    public void recordDeparture() {
        // TODO (B): implement
    }

    /** @return total revenue in cents */
    public int getRevenueCents() {
        return 0; // TODO (B)
    }

    /** @return number of customers who have paid */
    public int getPaidCount() {
        return 0; // TODO (B)
    }

    /** @return number of dishes served */
    public int getServedCount() {
        return 0; // TODO (B)
    }

    /** @return number of customers who left without paying */
    public int getDepartures() {
        return 0; // TODO (B)
    }

    /** @return true if at least one customer has paid */
    public boolean hasPaidCustomers() {
        return false; // TODO (B)
    }

    /**
     * @return average satisfaction of paid customers, or 0.0 if nobody has
     *         paid yet (the UI shows "N/A" in that case, using hasPaidCustomers())
     */
    public double getAverageSatisfaction() {
        return 0.0; // TODO (B)
    }

    /**
     * @return true if ALL three targets are met: paidCount >= 4,
     *         revenueCents >= 4500, and (someone has paid and) average
     *         satisfaction >= 60
     */
    public boolean isVictory() {
        return false; // TODO (B)
    }
}

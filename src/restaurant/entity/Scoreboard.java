package restaurant.entity;

/**
 * Keeps the game's results: revenue, paid customers, their satisfaction at
 * payment, dishes served and unhappy departures. Also decides victory.
 *
 * Owner: B
 */
public class Scoreboard {

    /** Victory target: at least this many paid customers. */
    public static final int MIN_PAID = 4;
    /** Victory target: at least this much revenue, in cents ($45.00). */
    public static final int MIN_REVENUE_CENTS = 4500;
    /** Victory target: average satisfaction of paid customers at least this. */
    public static final int MIN_AVG_SATISFACTION = 60;

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
        this.revenueCents += amountCents;
        this.paidCount += 1;
        this.totalPaidSatisfaction += satisfaction;
    }

    /** Records that one dish was served. */
    public void recordServed() {
        this.servedCount += 1;
    }

    /** Records that one customer left without paying. */
    public void recordDeparture() {
        this.departures += 1;
    }

    /** @return total revenue in cents */
    public int getRevenueCents() {
        return this.revenueCents;
    }

    /** @return number of customers who have paid */
    public int getPaidCount() {
        return this.paidCount;
    }

    /** @return number of dishes served */
    public int getServedCount() {
        return this.servedCount; 
    }

    /** @return number of customers who left without paying */
    public int getDepartures() {
        return this.departures;
    }

    /** @return true if at least one customer has paid */
    public boolean hasPaidCustomers() {
        return (this.paidCount > 0); 
    }

    /**
     * @return average satisfaction of paid customers, or 0.0 if nobody has
     *         paid yet (the UI shows "N/A" in that case, using hasPaidCustomers())
     */
    public double getAverageSatisfaction() {
        return (hasPaidCustomers() ? (double)this.totalPaidSatisfaction/this.paidCount : 0.0); 
    }

    /** @return true if ALL three targets are met */
    public boolean isVictory() {
        return isPaidTargetMet() && isRevenueTargetMet() && isSatisfactionTargetMet();
    }

    /** @return true if at least MIN_PAID customers have paid */
    public boolean isPaidTargetMet() {
        return paidCount >= MIN_PAID;
    }

    /** @return true if revenue is at least MIN_REVENUE_CENTS */
    public boolean isRevenueTargetMet() {
        return revenueCents >= MIN_REVENUE_CENTS;
    }

    /** @return true if someone has paid and their average satisfaction is at least MIN_AVG_SATISFACTION */
    public boolean isSatisfactionTargetMet() {
        return hasPaidCustomers() && getAverageSatisfaction() >= MIN_AVG_SATISFACTION;
    }
}

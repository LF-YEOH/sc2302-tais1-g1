package restaurant.entity;

/**
 * A food Critic: impatient (loses 12 satisfaction per waiting turn instead
 * of 8) and reacts strongly to the service when paying. If their
 * satisfaction is REVIEW_THRESHOLD (50) or more, they write a good review
 * and gain REVIEW_EFFECT (20) satisfaction, up to 100; below 50 they write
 * a bad review and lose 20, down to 0. Otherwise follows exactly the same
 * order and service flow as every customer, and pays full price.
 *
 * Owner: A
 */
public class CriticCustomer extends Customer {

    /** Satisfaction lost per waiting turn. */
    private static final int CRITIC_SATISFACTION_LOSS = 12;
    /** Satisfaction at or above which the Critic's review is good. */
    private static final int REVIEW_THRESHOLD = 50;
    /** How much the review raises or lowers satisfaction. */
    private static final int REVIEW_EFFECT = 20;

    /**
     * Creates a newly arrived Critic (satisfaction 100, WAITING).
     *
     * @param id the customer's number, starting from 1
     */
    public CriticCustomer(int id) {
        super(id);
    }

    /** @return CRITIC_SATISFACTION_LOSS (12) */
    @Override
    protected int getSatisfactionLoss() {
        return 0; // TODO (A)
    }

    /**
     * The Critic's review when paying: if getSatisfaction() is at least
     * REVIEW_THRESHOLD, adjustSatisfaction(+REVIEW_EFFECT); otherwise
     * adjustSatisfaction(-REVIEW_EFFECT). adjustSatisfaction keeps the
     * result within 0 to 100.
     */
    @Override
    protected void reactToService() {
        // TODO (A): implement
    }

    /** @return the customer's label with its type, e.g. "C2 [Critic]" */
    @Override
    public String toString() {
        return ""; // TODO (A)
    }
}

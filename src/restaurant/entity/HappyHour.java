package restaurant.entity;

/**
 * The Happy Hour promotion. The manager may activate it once per game, at
 * the start of a turn; it then lasts for that turn and the next. While it
 * is active, new orders are priced with HappyHourPricing and customers
 * still waiting for a table or food recover RECOVERY satisfaction at the
 * start of each active turn.
 *
 * Owner: Lead
 */
public class HappyHour {

    /** Number of turns Happy Hour lasts once activated. */
    private static final int DURATION_TURNS = 2;
    /** Satisfaction waiting customers recover at the start of an active turn. */
    public static final int RECOVERY = 15;

    private boolean used;
    private int turnsRemaining;
    private PricingPolicy standardPricing;
    private PricingPolicy happyHourPricing;

    /** Creates an unused Happy Hour, with both pricing policies ready. */
    public HappyHour() {
        used = false;
        turnsRemaining = 0;
        standardPricing = new StandardPricing();
        happyHourPricing = new HappyHourPricing();
    }

    /** @return true if Happy Hour has not been used yet this game */
    public boolean canActivate() {
        return !used;
    }

    /**
     * Activates Happy Hour for this turn and the next.
     *
     * @throws InvalidActionException if it has already been used this game
     */
    public void activate() throws InvalidActionException {
        if (used) {
            throw new InvalidActionException("Happy Hour has already been used this game.");
        }
        used = true;
        turnsRemaining = DURATION_TURNS;
    }

    /** @return true if Happy Hour is active this turn */
    public boolean isActive() {
        return turnsRemaining > 0;
    }

    /** @return active turns left, counting the current turn (0 if not active) */
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    /** Called at the end of every turn: an active Happy Hour has one turn fewer left. */
    public void endTurn() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
        }
    }

    /** @return the pricing for orders taken now: Happy Hour pricing if active, else standard */
    public PricingPolicy getCurrentPricing() {
        if (isActive()) {
            return happyHourPricing;
        }
        return standardPricing;
    }
}

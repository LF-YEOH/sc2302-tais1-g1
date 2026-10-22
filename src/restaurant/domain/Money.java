package restaurant.domain;

/**
 * Helper for money stored as integer cents.
 * All formatting and percentage rounding happens here, so the rule is applied
 * consistently everywhere in the game.
 */
public final class Money {

    private Money() {
        // Utility class: never instantiated.
    }

    /** Formats cents as dollars with two decimals, e.g. 1830 -> "$18.30". */
    public static String format(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }

    /**
     * Returns the given percentage of an amount, rounded to the nearest cent.
     * Example: applyPercent(1500, 90) -> 1350.
     */
    public static int applyPercent(int cents, int percent) {
        return (int) Math.round(cents * percent / 100.0);
    }
}
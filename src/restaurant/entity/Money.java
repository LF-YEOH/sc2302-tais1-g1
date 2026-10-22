package restaurant.entity;

/**
 * Formats amounts of money, which the game stores as whole cents
 * (e.g. 1800 means $18.00) so that no rounding errors can occur.
 * This class only has static methods and is never instantiated.
 */
public class Money {

    /** Private constructor: no Money objects are ever created. */
    private Money() {
    }

    /**
     * Formats cents as dollars with two decimal places.
     *
     * @param cents an amount in cents, e.g. 1830
     * @return the amount in dollars, e.g. "$18.30"
     */
    public static String format(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
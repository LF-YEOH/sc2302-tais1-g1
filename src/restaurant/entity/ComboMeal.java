package restaurant.entity;

/**
 * A combo of two existing menu items. A combo is a menu item (so orders,
 * the Chef and the Waiter treat it like any dish) and is made of two menu
 * items: its preparation units are the sum of its parts, and its price is
 * 90% of their combined price, rounded to the nearest cent.
 * Example: Salad + Pasta = ($9.00 + $18.00) x 0.9 = $24.30, 3 units.
 *
 * Owner: D
 */
public class ComboMeal extends MenuItem {

    /** Percentage of the combined price a combo costs (10% off). */
    private static final int COMBO_PRICE_PERCENT = 90;

    private final MenuItem firstItem;
    private final MenuItem secondItem;

    /**
     * Creates a combo of two menu items. Its name is "<first> + <second>",
     * its price COMBO_PRICE_PERCENT of the two prices added together, and its
     * preparation units the two units added together.
     *
     * @param firstItem  the first item in the combo
     * @param secondItem the second item in the combo
     */
    public ComboMeal(MenuItem firstItem, MenuItem secondItem) {
        super(firstItem.getName() + " + " + secondItem.getName(),
                Money.applyPercent(firstItem.getPriceCents() + secondItem.getPriceCents(),
                        COMBO_PRICE_PERCENT),
                firstItem.getPrepUnits() + secondItem.getPrepUnits());
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    /** @return the first item in the combo */
    public MenuItem getFirstItem() {
        return firstItem;
    }

    /** @return the second item in the combo */
    public MenuItem getSecondItem() {
        return secondItem;
    }
}

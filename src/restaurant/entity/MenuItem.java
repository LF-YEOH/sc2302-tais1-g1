package restaurant.entity;

/**
 * Represents one item on the restaurant menu, such as Salad or Burger.
 * A menu item knows its name, its price and how many preparation units
 * the Chef needs to cook it. It cannot change after it is created.
 *
 * @author Yeoh Lin Feng
 * @version 1.0
 * @since 2026-10-22
 */
public class MenuItem {

    private final String name;
    private final int priceCents;
    private final int prepUnits;

    /**
     * Creates a menu item.
     *
     * @param name       the item's name, e.g. "Pasta"
     * @param priceCents the price in cents, e.g. 1800 for $18.00
     * @param prepUnits  the number of Chef turns needed to prepare it
     */
    public MenuItem(String name, int priceCents, int prepUnits) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name must not be empty");
        }
        if (priceCents <= 0) {
            throw new IllegalArgumentException("Price must be positive: " + priceCents);
        }
        if (prepUnits < 1) {
            throw new IllegalArgumentException("Preparation units must be at least 1: " + prepUnits);
        }
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    /** @return the item's name */
    public String getName() {
        return name;
    }

    /** @return the menu price in cents */
    public int getPriceCents() {
        return priceCents;
    }

    /** @return the number of Chef turns needed to prepare this item */
    public int getPrepUnits() {
        return prepUnits;
    }

    /** @return a readable description, e.g. "Pasta ($18.00, 2 units)" */
    @Override
    public String toString() {
        String unitWord = (prepUnits == 1) ? " unit)" : " units)";
        return name + " (" + Money.format(priceCents) + ", " + prepUnits + unitWord;
    }
}
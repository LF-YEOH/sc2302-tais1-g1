package restaurant.domain;

/**
 * A single menu item with a fixed name, price and preparation time.
 * Immutable: all fields are final and there are no setters, so one Dish
 * object can be safely shared by every order of that dish.
 */
public class Dish implements MenuItem {

    private final String name;
    private final int priceCents;
    private final int prepUnits;

    public Dish(String name, int priceCents, int prepUnits) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Dish name must not be empty");
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriceCents() {
        return priceCents;
    }

    @Override
    public int getPrepUnits() {
        return prepUnits;
    }

    @Override
    public String toString() {
        return name + " (" + Money.format(priceCents) + ", " + prepUnits
                + (prepUnits == 1 ? " unit)" : " units)");
    }
}
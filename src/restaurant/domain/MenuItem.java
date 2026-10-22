package restaurant.domain;

/**
 * Anything a customer can order.
 * An interface rather than an abstract class: Dish stores its values while
 * ComboMeal (Stage 3) derives them from its components, so they share
 * behaviour but no data. See DECISIONS.md D2.
 */
public interface MenuItem {

    String getName();

    /** Full menu price in cents, before any customer or pricing discount. */
    int getPriceCents();

    /** Number of Chef turns needed to prepare this item. */
    int getPrepUnits();
}
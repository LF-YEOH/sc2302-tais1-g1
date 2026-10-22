package restaurant.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The fixed list of items customers choose from.
 * The list is private and exposed read-only, so no other class can add,
 * remove or reorder items during a game.
 */
public class Menu {

    private final List<MenuItem> items;

    public Menu(List<MenuItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Menu must contain at least one item");
        }
        this.items = new ArrayList<>(items); // defensive copy
    }

    /** The Stage 1 base menu: Salad, Burger, Pasta. */
    public static Menu standard() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new Dish("Salad", 900, 1));
        items.add(new Dish("Burger", 1500, 1));
        items.add(new Dish("Pasta", 1800, 2));
        return new Menu(items);
    }

    /** Read-only view of the items, in menu order. */
    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int size() {
        return items.size();
    }

    public MenuItem get(int index) {
        return items.get(index);
    }
}
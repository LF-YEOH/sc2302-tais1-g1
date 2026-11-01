package restaurant.entity;

import java.util.ArrayList;

/**
 * Represents the restaurant's menu: Salad, Burger and Pasta, followed by
 * the three combos of two different dishes.
 * The menu creates and owns its items (composition), and only lets
 * other classes read them, never add or remove them.
 */
public class Menu {

    private ArrayList<MenuItem> items;

    /** Creates the menu: the three dishes, then the three combos, in menu order. */
    public Menu() {
        items = new ArrayList<>();
        items.add(new MenuItem("Salad", 900, 1));
        items.add(new MenuItem("Burger", 1500, 1));
        items.add(new MenuItem("Pasta", 1800, 2));

        MenuItem salad = items.get(0);
        MenuItem burger = items.get(1);
        MenuItem pasta = items.get(2);
        items.add(new ComboMeal(salad, burger));
        items.add(new ComboMeal(salad, pasta));
        items.add(new ComboMeal(burger, pasta));
    }

    /** @return the number of items on the menu */
    public int getNumItems() {
        return items.size();
    }

    /**
     * Returns the item at a position on the menu.
     *
     * @param index position on the menu, starting from 0
     * @return the menu item at that position
     */
    public MenuItem getItem(int index) {
        return items.get(index);
    }
}

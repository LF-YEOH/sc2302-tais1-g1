package restaurant.entity;

import java.util.ArrayList;

/**
 * Represents the restaurant's menu: Salad, Burger and Pasta.
 * The menu creates and owns its items (composition), and only lets
 * other classes read them, never add or remove them.
 */
public class Menu {

    private ArrayList<MenuItem> items;

    /** Creates the base menu with its three items, in menu order. */
    public Menu() {
        items = new ArrayList<>();
        items.add(new MenuItem("Salad", 900, 1));
        items.add(new MenuItem("Burger", 1500, 1));
        items.add(new MenuItem("Pasta", 1800, 2));
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

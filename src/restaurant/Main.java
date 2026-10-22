package restaurant;

import restaurant.entity.Menu;

/**
 * Starting point of the Restaurant Rush game.
 *
 * @author Yeoh Lin Feng
 * @version 1.0
 * @since 2026-10-22
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Restaurant Rush");

        // Temporary check for Milestone 1 - replaced when the game loop exists.
        Menu menu = new Menu();
        for (int i = 0; i < menu.getNumItems(); i++) {
            System.out.println(menu.getItem(i));
        }
    }
}
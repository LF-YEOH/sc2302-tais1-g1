package restaurant;

import restaurant.entity.Menu;

/**
 * Restaurant Rush: a turn-based restaurant management game (SC2002 group assignment).
 * Starting point of the program.
 *
 * Author: Yeoh Lin Feng
 * Written: October 2026
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
package restaurant;

import restaurant.domain.Menu;
import restaurant.domain.MenuItem;
import restaurant.domain.Money;

public class Main {
    public static void main(String[] args) {
        System.out.println("Restaurant Rush");

        // Temporary check for Milestone 1 - replaced when the game loop exists.
        Menu menu = Menu.standard();
        for (MenuItem item : menu.getItems()) {
            System.out.println(item);
        }
        System.out.println(Money.format(Money.applyPercent(1500, 90))); // expect $13.50
        System.out.println(Money.format(Money.applyPercent(2700, 90))); // expect $24.30
    }
}
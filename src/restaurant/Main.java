package restaurant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import restaurant.boundary.RestaurantUI;
import restaurant.control.GameController;

/**
 * Restaurant Rush: a turn-based restaurant management game (SC2002 group assignment).
 * Starting point of the program. Creates the objects and connects them,
 * then starts the game.
 *
 * Usage:
 *   java -cp out restaurant.Main                          (play with the keyboard)
 *   java -cp out restaurant.Main --replay runs/file.txt   (play back a saved run)
 *
 * Author: Yeoh Lin Feng
 * Written: October 2026
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc;
        boolean echoInput;

        if (args.length == 2 && args[0].equals("--replay")) {
            try {
                sc = new Scanner(new File(args[1]));
            } catch (FileNotFoundException e) {
                System.out.println("Replay file not found: " + args[1]);
                return;
            }
            echoInput = true;
        } else {
            sc = new Scanner(System.in);
            echoInput = false;
        }

        RestaurantUI ui = new RestaurantUI(sc, echoInput);
        GameController controller = new GameController(ui);
        controller.run();
    }
}

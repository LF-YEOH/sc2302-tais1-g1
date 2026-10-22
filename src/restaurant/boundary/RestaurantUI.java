package restaurant.boundary;

import java.util.ArrayList;
import java.util.Scanner;

import restaurant.control.WaiterTask;
import restaurant.entity.Customer;
import restaurant.entity.Scoreboard;
import restaurant.entity.Table;

/**
 * The command-line interface. Displays the game state and reads the
 * manager's choices. It only checks that input is a number in the allowed
 * range; it never checks game rules (the staff classes do that).
 *
 * Input can come from the keyboard or from a replay file. When reading a
 * replay file, each input line is printed (echoed) so the run is readable.
 *
 * Owner: D
 */
public class RestaurantUI {

    private Scanner sc;
    private boolean echoInput;

    /**
     * @param sc        where input is read from (System.in or a replay file)
     * @param echoInput true to print each line read (used for replay files)
     */
    public RestaurantUI(Scanner sc, boolean echoInput) {
        this.sc = sc;
        this.echoInput = echoInput;
    }

    /** Prints the game title and a short summary of the goal and rules. */
    public void showWelcome() {
        // TODO (D): implement
    }

    /**
     * Prints the start-of-turn display: turn number (e.g. "Turn 4/18"),
     * revenue, average satisfaction of paid customers ("N/A" if none has
     * paid), the waiting queue with each customer's satisfaction, and each
     * table with its customer, their satisfaction, status and order
     * (e.g. "Table 1: C2 (sat 84) ORDERED - Pasta 1/2 PREPARING").
     *
     * @param turn   the current turn number
     * @param board  the scoreboard
     * @param queue  customers waiting for a table, in arrival order
     * @param tables all tables
     */
    public void showTurnStart(int turn, Scoreboard board, ArrayList<Customer> queue,
                              ArrayList<Table> tables) {
        // TODO (D): implement
    }

    /**
     * Prints the staff tasks available this turn, e.g.
     * "Waiter can: seat C3 | take order from C2 | serve C1" and
     * "Chef can cook for: C2". Prints "nobody" for an empty list.
     *
     * @param toSeat  customers who can be seated (status WAITING)
     * @param toOrder customers whose order can be taken (status SEATED)
     * @param toServe customers whose order is READY
     * @param toCook  customers whose order is PREPARING
     */
    public void showAvailableTasks(ArrayList<Customer> toSeat, ArrayList<Customer> toOrder,
                                   ArrayList<Customer> toServe, ArrayList<Customer> toCook) {
        // TODO (D): implement
    }

    /**
     * Asks the manager for the Waiter's task:
     * 1 = Seat, 2 = Take order, 3 = Serve, 0 = Wait.
     *
     * @return the chosen task
     */
    public WaiterTask askWaiterTask() {
        return WaiterTask.WAIT; // TODO (D)
    }

    /**
     * Prints the prompt and reads a customer number (0 or more).
     *
     * @param prompt e.g. "Seat which customer? Enter id (e.g. 3 for C3): "
     * @return the number typed
     */
    public int askCustomerId(String prompt) {
        return 0; // TODO (D)
    }

    /**
     * Announces a new order, e.g. "C2 orders Burger ($15.00, 1 unit)."
     *
     * @param customer the customer who just ordered (their order is set)
     */
    public void showOrderPlaced(Customer customer) {
        // TODO (D): implement
    }

    /** @param message an information message to print */
    public void showMessage(String message) {
        // TODO (D): implement
    }

    /** @param message the reason an action was refused, printed clearly as an error */
    public void showError(String message) {
        // TODO (D): implement
    }

    /**
     * Prints the end-of-turn results: dishes served, unhappy departures,
     * revenue, average satisfaction of paid customers ("N/A" if none) and
     * turns used (e.g. "4/18").
     *
     * @param turn  the turn just finished
     * @param board the scoreboard
     */
    public void showTurnEnd(int turn, Scoreboard board) {
        // TODO (D): implement
    }

    /**
     * Prints VICTORY or DEFEAT and whether each of the three targets was
     * met (paid customers >= 4, revenue >= $45.00, average satisfaction >= 60).
     *
     * @param board the final scoreboard
     */
    public void showResult(Scoreboard board) {
        // TODO (D): implement
    }

    /**
     * Reads one whole line and returns it as a number between min and max.
     * If echoInput is true, prints the line first. If the line is not a
     * whole number or is out of range, prints a short message and asks again.
     * If there is no more input (end of a replay file), returns min.
     *
     * @param min smallest allowed value
     * @param max largest allowed value
     * @return a number from min to max
     */
    private int readInt(int min, int max) {
        return min; // TODO (D)
    }
}

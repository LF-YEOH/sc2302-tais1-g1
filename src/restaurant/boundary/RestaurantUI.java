package restaurant.boundary;

import java.util.ArrayList;
import java.util.Scanner;

import restaurant.control.WaiterTask;
import restaurant.entity.Customer;
import restaurant.entity.Money;
import restaurant.entity.Order;
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

    // Display-only copies of the game's limits and targets, used in
    // "Turn 4/18" and in the target lines of the result. The real rules
    // live in GameController and Scoreboard.
    private static final int MAX_TURNS = 18;
    private static final int TARGET_PAID = 4;
    private static final int TARGET_REVENUE_CENTS = 4500;
    private static final int TARGET_AVG_SATISFACTION = 60;

    private static final String LINE = "==================================================";

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
        System.out.println(LINE);
        System.out.println("                 RESTAURANT RUSH");
        System.out.println(LINE);
        System.out.println("You manage a small restaurant with 2 tables for " + MAX_TURNS + " turns.");
        System.out.println("Each turn you choose one task for the Waiter (seat a customer,");
        System.out.println("take an order or serve a dish) and one order for the Chef to");
        System.out.println("work on. Customers eat for a turn, then pay the Cashier.");
        System.out.println("Customers who are not yet served lose satisfaction every turn,");
        System.out.println("and leave without paying if it reaches 0.");
        System.out.println();
        System.out.println("To win, by the end of turn " + MAX_TURNS + " you need:");
        System.out.println("  - at least " + TARGET_PAID + " paying customers");
        System.out.println("  - revenue of at least " + Money.format(TARGET_REVENUE_CENTS));
        System.out.println("  - average satisfaction of paying customers of at least "
                + TARGET_AVG_SATISFACTION);
        System.out.println();
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
        System.out.println();
        System.out.println("==================== Turn " + turn + "/" + MAX_TURNS + " ====================");
        System.out.println("Revenue: " + Money.format(board.getRevenueCents())
                + " | Avg satisfaction (paid): " + formatAverage(board));

        // Waiting queue, e.g. "Waiting queue: C3 (sat 92), C4 (sat 100)"
        String queueText = "";
        for (Customer c : queue) {
            if (!queueText.isEmpty()) {
                queueText += ", ";
            }
            queueText += c + " (sat " + c.getSatisfaction() + ")";
        }
        if (queueText.isEmpty()) {
            queueText = "empty";
        }
        System.out.println("Waiting queue: " + queueText);

        // One line per table
        for (Table t : tables) {
            Customer c = t.getOccupant();
            if (c == null) {
                System.out.println("Table " + t.getNumber() + ": empty");
            } else {
                Order order = c.getOrder();
                String orderText = (order == null) ? "no order yet" : order.toString();
                System.out.println("Table " + t.getNumber() + ": " + c + " (sat " + c.getSatisfaction()
                        + ") " + c.getStatus() + " - " + orderText);
            }
        }
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
        System.out.println("Waiter can: seat " + listCustomers(toSeat)
                + " | take order from " + listCustomers(toOrder)
                + " | serve " + listCustomers(toServe));
        System.out.println("Chef can cook for: " + listCustomers(toCook));
    }

    /**
     * Asks the manager for the Waiter's task:
     * 1 = Seat, 2 = Take order, 3 = Serve, 0 = Wait.
     *
     * @return the chosen task
     */
    public WaiterTask askWaiterTask() {
        System.out.println("Waiter task: 1 = Seat, 2 = Take order, 3 = Serve, 0 = Wait");
        System.out.print("Choice: ");
        int choice = readInt(0, 3);
        switch (choice) {
            case 1:
                return WaiterTask.SEAT;
            case 2:
                return WaiterTask.TAKE_ORDER;
            case 3:
                return WaiterTask.SERVE;
            default:
                return WaiterTask.WAIT;
        }
    }

    /**
     * Prints the prompt and reads a customer number (0 or more).
     *
     * @param prompt e.g. "Seat which customer? Enter id (e.g. 3 for C3): "
     * @return the number typed
     */
    public int askCustomerId(String prompt) {
        System.out.print(prompt);
        return readInt(0, Integer.MAX_VALUE);
    }

    /**
     * Announces a new order, e.g. "C2 orders Burger ($15.00, 1 unit)."
     *
     * @param customer the customer who just ordered (their order is set)
     */
    public void showOrderPlaced(Customer customer) {
        Order order = customer.getOrder();
        int units = order.getItem().getPrepUnits();
        String unitWord = (units == 1) ? "unit" : "units";
        System.out.println(customer + " orders " + order.getItem().getName()
                + " (" + Money.format(order.getPriceCents()) + ", " + units + " " + unitWord + ").");
    }

    /** @param message an information message to print */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** @param message the reason an action was refused, printed clearly as an error */
    public void showError(String message) {
        System.out.println("ERROR: " + message + " Please choose again.");
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
        System.out.println("--- End of turn " + turn + " ---");
        System.out.println("Dishes served: " + board.getServedCount()
                + " | Unhappy departures: " + board.getDepartures());
        System.out.println("Revenue: " + Money.format(board.getRevenueCents())
                + " | Avg satisfaction (paid): " + formatAverage(board)
                + " | Turns used: " + turn + "/" + MAX_TURNS);
    }

    /**
     * Prints VICTORY or DEFEAT and whether each of the three targets was
     * met (paid customers >= 4, revenue >= $45.00, average satisfaction >= 60).
     *
     * @param board the final scoreboard
     */
    public void showResult(Scoreboard board) {
        // The overall result comes from the Scoreboard; the UI only shows it.
        String result = board.isVictory() ? "VICTORY!" : "DEFEAT";

        boolean paidMet = board.getPaidCount() >= TARGET_PAID;
        boolean revenueMet = board.getRevenueCents() >= TARGET_REVENUE_CENTS;
        boolean satisfactionMet = board.hasPaidCustomers()
                && board.getAverageSatisfaction() >= TARGET_AVG_SATISFACTION;

        System.out.println();
        System.out.println(LINE);
        System.out.println("                    " + result);
        System.out.println(LINE);
        System.out.println(metLabel(paidMet) + "Paid customers: " + board.getPaidCount()
                + " (target: " + TARGET_PAID + " or more)");
        System.out.println(metLabel(revenueMet) + "Revenue: " + Money.format(board.getRevenueCents())
                + " (target: " + Money.format(TARGET_REVENUE_CENTS) + " or more)");
        System.out.println(metLabel(satisfactionMet) + "Average satisfaction: " + formatAverage(board)
                + " (target: " + TARGET_AVG_SATISFACTION + " or more)");
        System.out.println("Dishes served: " + board.getServedCount()
                + " | Unhappy departures: " + board.getDepartures());
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
        while (true) {
            if (!sc.hasNextLine()) {
                System.out.println();
                return min; // end of replay file
            }
            String line = sc.nextLine().trim();
            if (echoInput) {
                System.out.println(line);
            }
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.print("Out of range. " + rangeHint(min, max));
            } catch (NumberFormatException e) {
                System.out.print("Not a whole number. " + rangeHint(min, max));
            }
        }
    }

    /**
     * @param min smallest allowed value
     * @param max largest allowed value
     * @return a prompt asking for a number in the range, e.g. "Enter 0 to 3: "
     */
    private String rangeHint(int min, int max) {
        if (max == Integer.MAX_VALUE) {
            return "Enter a number of " + min + " or more: ";
        }
        return "Enter " + min + " to " + max + ": ";
    }

    /**
     * @param customers the customers to list
     * @return their labels separated by commas, e.g. "C2, C3", or "nobody" if empty
     */
    private String listCustomers(ArrayList<Customer> customers) {
        if (customers.isEmpty()) {
            return "nobody";
        }
        String text = "";
        for (Customer c : customers) {
            if (!text.isEmpty()) {
                text += ", ";
            }
            text += c;
        }
        return text;
    }

    /**
     * @param board the scoreboard
     * @return the average satisfaction of paid customers to one decimal place,
     *         or "N/A" if nobody has paid yet
     */
    private String formatAverage(Scoreboard board) {
        if (!board.hasPaidCustomers()) {
            return "N/A";
        }
        return String.format("%.1f", board.getAverageSatisfaction());
    }

    /**
     * @param met whether a target was met
     * @return a fixed-width label for the result lines
     */
    private String metLabel(boolean met) {
        return met ? "[MET]     " : "[NOT MET] ";
    }
}

package restaurant.control;

import java.util.ArrayList;

import restaurant.boundary.RestaurantUI;
import restaurant.entity.Cashier;
import restaurant.entity.Chef;
import restaurant.entity.Customer;
import restaurant.entity.CustomerStatus;
import restaurant.entity.Menu;
import restaurant.entity.Scoreboard;
import restaurant.entity.Table;
import restaurant.entity.Waiter;

/**
 * Runs the game: 18 turns, each made of six steps in a fixed order.
 * Owns the customers, waiting queue, tables, staff and scoreboard, and asks
 * the UI only for the manager's decisions and to display the state.
 *
 * Turn steps:
 *   1. Arrivals: the scheduled customer joins the waiting queue.
 *      (Then the start-of-turn state is displayed.)
 *   2-3. Manager choices: the Waiter acts first, then the Chef. An invalid
 *      choice shows the reason and asks again without using up the task.
 *   4. Payment, then eating: the Cashier charges the oldest customer who was
 *      ALREADY ready to pay; only then do customers served on an earlier
 *      turn become READY_TO_PAY.
 *   5. Waiting: customers still waiting for service lose satisfaction;
 *      those reaching 0 leave.
 *   6. Result: after turn 18, victory if all Scoreboard targets are met.
 *
 * Owner: Lead
 */
public class GameController {

    private static final int MAX_TURNS = 18;
    private static final int NUM_TABLES = 2;

    private int turn;
    private RestaurantUI ui;
    private Menu menu;
    private ArrayList<Table> tables;
    private ArrayList<Customer> customers;     // everyone who has arrived
    private ArrayList<Customer> waitingQueue;  // arrived but not yet seated, in arrival order
    private Waiter waiter;
    private Chef chef;
    private Cashier cashier;
    private Scoreboard scoreboard;
    private ArrivalSchedule schedule;

    /**
     * Sets up a new game: creates the menu, NUM_TABLES tables numbered from 1,
     * empty customer and queue lists, the three staff, the scoreboard and the
     * arrival schedule. turn starts at 0.
     *
     * @param ui the user interface used for all input and output
     */
    public GameController(RestaurantUI ui) {
        this.ui = ui;
        // TODO (Lead): create the other objects
    }

    /** Plays the whole game: welcome, turns 1 to MAX_TURNS, then the result. */
    public void run() {
        // TODO (Lead): implement
    }

    /** Plays one turn: arrival, display, Waiter, Chef, payment, eating, waiting, end-of-turn display. */
    private void playTurn() {
        // TODO (Lead): implement
    }

    /** Step 1: if a customer is scheduled this turn, create them and add them to customers and waitingQueue. */
    private void admitArrival() {
        // TODO (Lead): implement
    }

    /**
     * Step 2-3 (Waiter): asks for a task and a customer id, and performs it.
     * Repeats while the Waiter throws InvalidActionException (showing the
     * reason each time). Stops after one successful task or WAIT.
     * SEAT also removes the customer from waitingQueue; SERVE also records a
     * served dish on the scoreboard; TAKE_ORDER shows what was ordered.
     */
    private void runWaiterStep() {
        // TODO (Lead): implement
    }

    /**
     * Step 2-3 (Chef): asks for a customer id (0 = wait) and cooks that
     * customer's order. Repeats while the Chef throws InvalidActionException.
     */
    private void runChefStep() {
        // TODO (Lead): implement
    }

    /**
     * Step 4a: if anyone is READY_TO_PAY, the Cashier charges the one who
     * became ready earliest (findNextToPay()) and the scoreboard records the
     * payment and their satisfaction.
     */
    private void runPaymentStep() {
        // TODO (Lead): implement
    }

    /** Step 4b: every EATING customer is asked to finishEating(turn). */
    private void runEatingStep() {
        // TODO (Lead): implement
    }

    /**
     * Step 5: every customer calls loseSatisfaction(); those who have run
     * out of patience leave(), are removed from waitingQueue, and are
     * recorded as departures.
     */
    private void runWaitingStep() {
        // TODO (Lead): implement
    }

    /**
     * @param id the customer number typed by the manager
     * @return the customer with that id, or null if there is none
     */
    private Customer findCustomer(int id) {
        return null; // TODO (Lead)
    }

    /** @return the lowest-numbered free table, or null if all are occupied */
    private Table findFreeTable() {
        return null; // TODO (Lead)
    }

    /**
     * @return the READY_TO_PAY customer with the smallest readyToPayTurn
     *         (ties: smallest id), or null if nobody is ready to pay
     */
    private Customer findNextToPay() {
        return null; // TODO (Lead)
    }

    /**
     * @param status the status to look for
     * @return all customers that currently have this status, in arrival order
     */
    private ArrayList<Customer> customersWithStatus(CustomerStatus status) {
        return new ArrayList<>(); // TODO (Lead)
    }
}

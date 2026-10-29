package restaurant.control;

import java.util.ArrayList;

import restaurant.boundary.RestaurantUI;
import restaurant.entity.Cashier;
import restaurant.entity.Chef;
import restaurant.entity.Customer;
import restaurant.entity.CustomerStatus;
import restaurant.entity.HappyHour;
import restaurant.entity.Host;
import restaurant.entity.InvalidActionException;
import restaurant.entity.Menu;
import restaurant.entity.Money;
import restaurant.entity.Order;
import restaurant.entity.OrderStatus;
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
 *      (Then the start-of-turn state is displayed.) Happy Hour may be
 *      activated, and during Happy Hour waiting customers recover.
 *   Host: seats one queued customer, before the Waiter acts.
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

    /** Number of turns in a game. Public so the UI can display it. */
    public static final int MAX_TURNS = 18;
    private static final int NUM_TABLES = 2;

    private int turn;
    private RestaurantUI ui;
    private Menu menu;
    private ArrayList<Table> tables;
    private ArrayList<Customer> customers;     // everyone who has arrived
    private ArrayList<Customer> waitingQueue;  // arrived but not yet seated, in arrival order
    private Host host;
    private Waiter waiter;
    private Chef chef;
    private Cashier cashier;
    private Scoreboard scoreboard;
    private ArrivalSchedule schedule;
    private HappyHour happyHour;

    /**
     * Sets up a new game: creates the menu, NUM_TABLES tables numbered from 1,
     * empty customer and queue lists, the three staff, the scoreboard and the
     * arrival schedule. turn starts at 0.
     *
     * @param ui the user interface used for all input and output
     */
    public GameController(RestaurantUI ui) {
        this.ui = ui;
        menu = new Menu();
        tables = new ArrayList<>();
        for (int i = 1; i <= NUM_TABLES; i++) {
            tables.add(new Table(i));
        }
        customers = new ArrayList<>();
        waitingQueue = new ArrayList<>();
        host = new Host();
        waiter = new Waiter();
        chef = new Chef();
        cashier = new Cashier();
        scoreboard = new Scoreboard();
        schedule = new ArrivalSchedule();
        happyHour = new HappyHour();
        turn = 0;
    }

    /** Plays the whole game: welcome, turns 1 to MAX_TURNS, then the result. */
    public void run() {
        ui.showWelcome();
        for (turn = 1; turn <= MAX_TURNS; turn++) {
            playTurn();
        }
        ui.showResult(scoreboard);
    }

    /** Plays one turn: arrival, display, Waiter, Chef, payment, eating, waiting, end-of-turn display. */
    private void playTurn() {
        // Step 1: arrivals, then show the state the manager decides from
        admitArrival();
        ui.showTurnStart(turn, scoreboard, waitingQueue, tables);
        ui.showHappyHourStatus(happyHour);

        // Turn start: Happy Hour may be activated, then waiting customers recover
        offerHappyHour();
        applyHappyHourRecovery();

        ui.showAvailableTasks(customersWithStatus(CustomerStatus.WAITING),
                customersWithStatus(CustomerStatus.SEATED),
                customersWithOrderStatus(OrderStatus.READY),
                customersWithOrderStatus(OrderStatus.PREPARING));

        // Host seats before the Waiter, so the Waiter can take that customer's order this turn
        runHostStep();

        // Steps 2-3: the Waiter acts before the Chef
        runWaiterStep();
        runChefStep();

        // Step 4: payment BEFORE eating, so nobody pays in the turn they finish eating
        runPaymentStep();
        runEatingStep();

        // Step 5: waiting customers lose satisfaction; some may leave
        runWaitingStep();

        happyHour.endTurn();
        ui.showTurnEnd(turn, scoreboard);
    }

    /** Step 1: if a customer is scheduled this turn, create them and add them to customers and waitingQueue. */
    private void admitArrival() {
        if (schedule.hasArrival(turn)) {
            Customer customer = schedule.createCustomer();
            customers.add(customer);
            waitingQueue.add(customer);
            ui.showMessage(customer + " arrives and joins the waiting queue.");
        }
    }

    /**
     * Turn start: if Happy Hour has not been used, asks the manager whether to
     * activate it now. It then lasts for this turn and the next.
     */
    private void offerHappyHour() {
        if (happyHour.canActivate() && ui.askActivateHappyHour()) {
            try {
                happyHour.activate();
                ui.showMessage("Happy Hour starts! 20% off new orders this turn and next.");
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Turn start: during each active Happy Hour turn, every customer still
     * waiting for a table or food recovers HappyHour.RECOVERY satisfaction
     * (up to 100).
     */
    private void applyHappyHourRecovery() {
        if (happyHour.isActive()) {
            for (Customer customer : customers) {
                customer.recoverSatisfaction(HappyHour.RECOVERY);
            }
            ui.showMessage("Happy Hour: waiting customers recover " + HappyHour.RECOVERY + " satisfaction.");
        }
    }

    /**
     * Host step: asks which queued customer the Host should seat (0 = wait)
     * and seats them. Repeats while the Host throws InvalidActionException.
     * Runs before the Waiter's step.
     */
    private void runHostStep() {
        while (true) {
            int id = ui.askCustomerId("Host: seat which customer? Enter id (0 = wait): ");
            if (id == 0) {
                ui.showMessage("The Host waits this turn.");
                return;
            }
            Customer customer = findCustomer(id);
            try {
                host.seat(customer, findFreeTable());
                waitingQueue.remove(customer);
                ui.showMessage("The Host seats " + customer + " at Table " + customer.getTable().getNumber() + ".");
                return;
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Step 2-3 (Waiter): asks for a task and a customer id, and performs it.
     * Repeats while the Waiter throws InvalidActionException (showing the
     * reason each time). Stops after one successful task or WAIT.
     * SEAT also removes the customer from waitingQueue; SERVE also records a
     * served dish on the scoreboard; TAKE_ORDER shows what was ordered.
     */
    private void runWaiterStep() {
        while (true) {
            WaiterTask task = ui.askWaiterTask();
            if (task == WaiterTask.WAIT) {
                ui.showMessage("The Waiter waits this turn.");
                return;
            }

            Customer customer = findCustomer(ui.askCustomerId("Which customer? Enter id (e.g. 3 for C3): "));
            try {
                switch (task) {
                    case SEAT:
                        waiter.seat(customer, findFreeTable());
                        waitingQueue.remove(customer);
                        ui.showMessage(customer + " is seated at Table " + customer.getTable().getNumber() + ".");
                        break;
                    case TAKE_ORDER:
                        waiter.takeOrder(customer, menu, happyHour.getCurrentPricing());
                        ui.showOrderPlaced(customer);
                        break;
                    case SERVE:
                        waiter.serve(customer, turn);
                        scoreboard.recordServed();
                        ui.showMessage(customer + " is served their " + customer.getOrder().getItem().getName() + ".");
                        break;
                    default:
                        break;
                }
                return; // one successful task per turn
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage()); // nothing changed; ask again
            }
        }
    }

    /**
     * Step 2-3 (Chef): asks for a customer id (0 = wait) and cooks that
     * customer's order. Repeats while the Chef throws InvalidActionException.
     */
    private void runChefStep() {
        while (true) {
            int id = ui.askCustomerId("Chef: cook whose order? Enter id (0 = wait): ");
            if (id == 0) {
                ui.showMessage("The Chef waits this turn.");
                return;
            }

            Customer customer = findCustomer(id);
            Order order = null;
            if (customer != null) {
                order = customer.getOrder();
            }
            try {
                chef.cook(order);
                ui.showMessage("The Chef works on " + customer + "'s order: " + order + ".");
                return; // one unit per turn
            } catch (InvalidActionException e) {
                ui.showError(e.getMessage()); // nothing changed; ask again
            }
        }
    }

    /**
     * Step 4a: if anyone is READY_TO_PAY, the Cashier charges the one who
     * became ready earliest (findNextToPay()) and the scoreboard records the
     * payment and their satisfaction.
     */
    private void runPaymentStep() {
        Customer next = findNextToPay();
        if (next == null) {
            return;
        }
        try {
            int bill = cashier.collectPayment(next);
            scoreboard.recordPayment(bill, next.getSatisfaction());
            ui.showMessage(next + " pays " + Money.format(bill) + " (satisfaction "
                    + next.getSatisfaction() + ") and leaves. The table is free.");
        } catch (InvalidActionException e) {
            // Cannot happen: findNextToPay() only returns READY_TO_PAY customers.
            ui.showError(e.getMessage());
        }
    }

    /** Step 4b: every EATING customer is asked to finishEating(turn). */
    private void runEatingStep() {
        for (Customer customer : customers) {
            if (customer.finishEating(turn)) {
                ui.showMessage(customer + " has finished eating and is ready to pay.");
            }
        }
    }

    /**
     * Step 5: every customer calls loseSatisfaction(); those who have run
     * out of patience leave(), are removed from waitingQueue, and are
     * recorded as departures.
     */
    private void runWaitingStep() {
        for (Customer customer : customers) {
            customer.loseSatisfaction();
            if (customer.hasRunOutOfPatience()) {
                customer.leave();
                waitingQueue.remove(customer);
                scoreboard.recordDeparture();
                ui.showMessage(customer + " ran out of patience and left without paying.");
            }
        }
    }

    /**
     * @param id the customer number typed by the manager
     * @return the customer with that id, or null if there is none
     */
    private Customer findCustomer(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    /** @return the lowest-numbered free table, or null if all are occupied */
    private Table findFreeTable() {
        for (Table table : tables) {
            if (table.isFree()) {
                return table;
            }
        }
        return null;
    }

    /**
     * @return the READY_TO_PAY customer with the smallest readyToPayTurn
     *         (ties: smallest id), or null if nobody is ready to pay
     */
    private Customer findNextToPay() {
        Customer oldest = null;
        for (Customer customer : customers) {  // customers are in id order
            if (customer.getStatus() == CustomerStatus.READY_TO_PAY) {
                if (oldest == null || customer.getReadyToPayTurn() < oldest.getReadyToPayTurn()) {
                    oldest = customer;
                }
            }
        }
        return oldest;
    }

    /**
     * @param status the status to look for
     * @return all customers that currently have this status, in arrival order
     */
    private ArrayList<Customer> customersWithStatus(CustomerStatus status) {
        ArrayList<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.getStatus() == status) {
                result.add(customer);
            }
        }
        return result;
    }

    /**
     * @param status the order status to look for
     * @return all customers whose order currently has this status, in arrival order
     */
    private ArrayList<Customer> customersWithOrderStatus(OrderStatus status) {
        ArrayList<Customer> result = new ArrayList<>();
        for (Customer customer : customers) {
            Order order = customer.getOrder();
            if (order != null && order.getStatus() == status) {
                result.add(customer);
            }
        }
        return result;
    }
}

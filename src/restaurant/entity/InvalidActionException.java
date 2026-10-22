package restaurant.entity;

/**
 * Thrown when the manager chooses a staff action that breaks a game rule,
 * for example serving a dish that is not ready. The message explains why,
 * so it can be shown to the manager, who may then choose again.
 * An action that throws this exception must not have changed anything.
 */
public class InvalidActionException extends Exception {

    /**
     * Creates the exception with the reason the action was refused.
     *
     * @param message a short explanation shown to the manager,
     *                e.g. "C2's Pasta is not ready yet (1/2 units done)."
     */
    public InvalidActionException(String message) {
        super(message);
    }
}

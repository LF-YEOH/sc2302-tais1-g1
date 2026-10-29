package restaurant.entity;

/**
 * The Host seats at most one queued customer per turn, in addition to the
 * Waiter's task. Seating is the Host's only job, so it inherits seat() from
 * SeatingStaff and adds nothing else.
 *
 * Owner: C
 */
public class Host extends SeatingStaff {
}

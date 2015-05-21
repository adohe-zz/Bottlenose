package com.xqbase.baiji.common.timeout;

/**
 * Execute tasks when timeout occurs.
 *
 * @author Tony He
 */
public interface TimeoutExecutor {

    /**
     * Executes the given task when the associated timeout occurs, or immediately if the timeout
     * has already occurred.  If the timeout does not occur, the task will not be executed.
     * The task will never be executed more than once.
     *
     * @param task the task to be associated with the timeout.
     */
    void addTimeoutTask(Runnable task);
}

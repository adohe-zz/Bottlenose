package com.xqbase.baiji.loadbalancer.reactive;

/**
 * A listener to be invoked by load balancer at different stage of execution.
 *
 * @param <I> Input type used by {@link ExecutionContext} passed to
 *           listener where it can call {@link ExecutionContext#getRequest()} to examine the
 *           request object of the execution
 * @param <O> Output type from the load balancer execution, used by {@link #onExecutionSuccess(ExecutionContext, Object, ExecutionInfo)}
 *           API
 */
public interface ExecutionListener<I, O> {

    /**
     * An exception to indicate that the listener wants to abort execution
     */
    class AbortExecutionException extends RuntimeException {
        public AbortExecutionException(String message) {
            super(message);
        }

        public AbortExecutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Called when execution is about to start.
     *
     * @throws ExecutionListener.AbortExecutionException if the listener would
     *              like to abort the execution
     */
    public void onExecutionStart(ExecutionContext<I> context) throws AbortExecutionException;

    /**
     * Called when a instance is chosen and the request is going to be executed on the instance.
     *
     * @throws ExecutionListener.AbortExecutionException if the listener would
     *              like to abort the execution
     */
    public void onStartWithInstance(ExecutionContext<I> context, ExecutionInfo info) throws AbortExecutionException;

    /**
     * Called when an exception is received from executing the request on a instance.
     *
     * @param exception Exception received
     */
    public void onExceptionWithInstance(ExecutionContext<I> context, Throwable exception, ExecutionInfo info);

    /**
     * Called when the request is executed successfully on the instance
     *
     * @param response Object received from the execution
     */
    public void onExecutionSuccess(ExecutionContext<I> context, O response, ExecutionInfo info);

    /**
     * Called when the request is considered failed after all retries.
     *
     * @param finalException Final exception received. This may be a wrapped exception indicating that all
     *                       retries have been exhausted.
     */
    public void onExecutionFailed(ExecutionContext<I> context, Throwable finalException, ExecutionInfo info);
}

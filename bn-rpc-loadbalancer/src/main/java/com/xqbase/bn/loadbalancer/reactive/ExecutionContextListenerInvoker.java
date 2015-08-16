package com.xqbase.bn.loadbalancer.reactive;

import com.xqbase.bn.common.logging.Logger;
import com.xqbase.bn.common.logging.LoggerFactory;

import java.util.List;

/**
 * Utility class to invoke the list of {@link ExecutionListener} with {@link ExecutionContext}
 *
 * @author Tony He
 */
@SuppressWarnings("unchecked")
public class ExecutionContextListenerInvoker<I, O> {

    private final static Logger logger = LoggerFactory.getLogger(ExecutionContextListenerInvoker.class);

    private final ExecutionContext<I> executionContext;
    private final List<ExecutionListener<I, O>> listeners;

    public ExecutionContextListenerInvoker(ExecutionContext<I> executionContext, List<ExecutionListener<I, O>> listeners) {
        this.executionContext = executionContext;
        this.listeners = listeners;
    }

    /**
     * Called when execution is about to start.
     */
    public void onExecutionStart() {
        onExecutionStart(this.executionContext);
    }

    public void onExecutionStart(ExecutionContext<I> context) {
        for (ExecutionListener listener : listeners) {
            try {
                listener.onExecutionStart(executionContext.getChildContext(listener));
            } catch (Throwable t) {
                if (t instanceof ExecutionListener.AbortExecutionException) {
                    throw (ExecutionListener.AbortExecutionException) t;
                }
                logger.error("Error invoke listener " + listener, t);
            }
        }
    }

    /**
     * Called when a instance is chosen and the request is going to be executed on the instance.
     */
    public void onStartWithInstance(ExecutionInfo executionInfo) {
        onStartWithInstance(this.executionContext, executionInfo);
    }

    public void onStartWithInstance(ExecutionContext<I> context, ExecutionInfo executionInfo) {
        for (ExecutionListener listener : listeners) {
            try {
                listener.onStartWithInstance(context, executionInfo);
            } catch (Throwable t) {
                if (t instanceof ExecutionListener.AbortExecutionException) {
                    throw (ExecutionListener.AbortExecutionException) t;
                }
                logger.error("Error invoke listener " + listener, t);
            }
        }
    }

    /**
     * Called when an exception is received from executing the request on a server.
     *
     * @param e Exception received
     */
    public void onExceptionWithInstance(Throwable e, ExecutionInfo executionInfo) {
        onExceptionWithInstance(this.executionContext, e, executionInfo);
    }

    public void onExceptionWithInstance(ExecutionContext<I> context, Throwable e, ExecutionInfo executionInfo) {
        for (ExecutionListener listener : listeners) {
            try {
                listener.onExceptionWithInstance(context, e, executionInfo);
            } catch (Throwable t) {
                if (t instanceof ExecutionListener.AbortExecutionException) {
                    throw (ExecutionListener.AbortExecutionException) t;
                }
                logger.error("Error invoke listener " + listener, t);
            }
        }
    }

    /**
     * Called when the request is executed successfully on the server
     *
     * @param response Object received from the execution
     */
    public void onExecutionSuccess(O response, ExecutionInfo executionInfo) {
        onExecutionSuccess(this.executionContext, response, executionInfo);
    }

    public void onExecutionSuccess(ExecutionContext<I> context, O response, ExecutionInfo executionInfo) {
        for (ExecutionListener listener : listeners) {
            try {
                listener.onExecutionSuccess(context, response, executionInfo);
            } catch (Throwable t) {
                if (t instanceof ExecutionListener.AbortExecutionException) {
                    throw (ExecutionListener.AbortExecutionException) t;
                }
                logger.error("Error invoke listener " + listener, t);
            }
        }
    }

    /**
     * Called when the request is considered failed after all retries.
     *
     * @param finalException Final exception received.
     */
    public void onExecutionFailed(Throwable finalException, ExecutionInfo executionInfo) {
        onExecutionFailed(this.executionContext, finalException, executionInfo);
    }

    public void onExecutionFailed(ExecutionContext<I> context, Throwable finalException, ExecutionInfo executionInfo) {
        for (ExecutionListener listener : listeners) {
            try {
                listener.onExecutionFailed(context, finalException, executionInfo);
            } catch (Throwable t) {
                if (t instanceof ExecutionListener.AbortExecutionException) {
                    throw (ExecutionListener.AbortExecutionException) t;
                }
                logger.error("Error invoke listener " + listener, t);
            }
        }
    }
}

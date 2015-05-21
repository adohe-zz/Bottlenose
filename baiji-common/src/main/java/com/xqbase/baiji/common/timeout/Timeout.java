package com.xqbase.baiji.common.timeout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A timeout that stores a reference.  If the reference is not retrieved within
 * the specified timeout, any timeout tasks specified by {@link #addTimeoutTask(Runnable)}
 * are executed.
 *
 * @author Tony He
 */
public class Timeout<T> implements TimeoutExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Timeout.class);

    private final AtomicReference<T> itemRef;
    private final ScheduledFuture<?> future;
    private final ClosableQueue<Runnable> queue = new ClosableQueue<>();

    public Timeout(ScheduledExecutorService executor, int timeout, TimeUnit timeUnit, T item) {
        if (null == item) {
            throw new NullPointerException("item cannot be null");
        }
        itemRef = new AtomicReference<>(item);
        future = executor.schedule(new Runnable() {
            @Override
            public void run() {
                T t = itemRef.getAndSet(null);
                if (t != null) {
                    List<Runnable> actions = queue.close();
                    if (actions.isEmpty()) {
                        LOGGER.warn("timeout elapsed but no action specified");
                    }
                    for (Runnable action : actions) {
                        try {
                            action.run();
                        } catch (Exception e) {
                            LOGGER.error("Failed to execute action");
                        }
                    }
                }
            }
        }, timeout, timeUnit);
    }

    /**
     * Obtain the item from this Timeout instance.
     *
     * @return the item held by this Timeout, or null if the item has already been retrieved.
     */
    public T getItem() {
        T item = itemRef.getAndSet(null);
        if (item != null) {
            future.cancel(false);
            queue.close();
        }
        return item;
    }

    @Override
    public void addTimeoutTask(Runnable task) {
        if (!queue.offer(task)) {
            task.run();
        }
    }
}

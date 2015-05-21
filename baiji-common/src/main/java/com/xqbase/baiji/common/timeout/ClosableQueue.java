package com.xqbase.baiji.common.timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides a mechanism to accumulate objects in a queue until the queue is closed,
 * at which point all the enqueued objects can be retrieved and no further objects can be
 * enqueued.
 *
 * The queue is optimized for the use case that it will be closed most of the time, so
 * testing whether the queue is closed or not is lock-free.  Specifically, calling
 * {@link #offer(Object)} after the queue is closed only reads a single atomic integer.
 *
 * A typical use is to record a list of waiters for some event; the queue is closed
 * when the event occurs.  At that point the waiters can be notified of the event,
 * and subsequent attempts to enqueue return false, indicating the event has already occurred.
 *
 */
public class ClosableQueue<T> {

    private final AtomicInteger count = new AtomicInteger(0);
    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();
    private final AtomicBoolean closing = new AtomicBoolean(false);

    /**
     * Enqueue an object into the queue, if it is still open.
     *
     * @param obj the object to enqueue
     * @return true if the object was inserted, false if the queue was closed
     */
    public boolean offer(T obj) {
        int c;
        while ((c = count.get()) >= 0) {
            if (count.compareAndSet(c, c + 1)) {
                queue.offer(obj);
                return true;
            }
        }
        return false;
    }

    /**
     * Closes the queue and returns the items enqueued prior to closure.
     * No more items can be enqueued after calling this method, and offer() will return false.
     * It is the caller's responsibility to ensure that close() is called at most once
     *
     * @return the items enqueued prior to closure
     * @throws IllegalStateException if close() has been previously called
     */
    public List<T> close()
    {
        List<T> queue = ensureClosed();
        if (queue == null)
        {
            throw new IllegalStateException("Queue is already closed");
        }
        return queue;
    }

    public List<T> ensureClosed()
    {
        if (!closing.compareAndSet(false, true))
        {
            return null;
        }
        boolean interrupted = false;
        int c = count.get();
        List<T> members = new ArrayList<T>(c);
        while (c >= 0)
        {
            if (count.compareAndSet(c, c - 1))
            {
                if (c-- > 0)
                {
                    T removed = null;
                    while (removed == null)
                    {
                        try
                        {
                            removed = queue.take();
                        }
                        catch (InterruptedException e)
                        {
                            interrupted = true;
                        }
                    }
                    members.add(removed);
                }
            }
            else
            {
                c = count.get();
            }
        }
        if (interrupted)
        {
            Thread.currentThread().interrupt();
        }
        return members;
    }

    /**
     * Return true if the queue is already closed.
     *
     * @return true if the queue is already closed.
     */
    public boolean isClosed() {
        return count.get() == -1;
    }
}

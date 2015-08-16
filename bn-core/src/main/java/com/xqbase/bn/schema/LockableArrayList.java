package com.xqbase.bn.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class keeps a boolean variable <tt>locked</tt> which is set
 * to <tt>true</tt> in the lock() method. It's legal to call
 * lock() any number of times. Any lock() other than the first one
 * is a no-op.
 *
 * This class throws <tt>IllegalStateException</tt> if a mutating
 * operation is performed after being locked. Since modifications through
 * iterator also use the list's mutating operations, this effectively
 * blocks all modifications.
 *
 * @author Tony He
 */
public class LockableArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;
    private boolean locked = false;

    public LockableArrayList() {
    }

    public LockableArrayList(int size) {
        super(size);
    }

    public LockableArrayList(List<E> types) {
        super(types);
    }

    public List<E> lock() {
        locked = true;
        return this;
    }

    private void ensureUnlocked() {
        if (locked) {
            throw new IllegalStateException();
        }
    }

    public boolean add(E e) {
        ensureUnlocked();
        return super.add(e);
    }

    public boolean remove(Object o) {
        ensureUnlocked();
        return super.remove(o);
    }

    public E remove(int index) {
        ensureUnlocked();
        return super.remove(index);
    }

    public boolean addAll(Collection<? extends E> c) {
        ensureUnlocked();
        return super.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        ensureUnlocked();
        return super.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        ensureUnlocked();
        return super.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        ensureUnlocked();
        return super.retainAll(c);
    }

    public void clear() {
        ensureUnlocked();
        super.clear();
    }
}

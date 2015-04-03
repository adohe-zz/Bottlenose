package com.xqbase.baiji.exceptions;

import java.io.IOException;

/**
 * Base class for exceptions thrown to client by server.
 */
public class BaijiRemoteException extends IOException {
    private Object value;

    protected BaijiRemoteException() {
    }

    public BaijiRemoteException(Throwable value) {
        this(value.toString());
        initCause(value);
    }

    public BaijiRemoteException(Object value) {
        super(value != null ? value.toString() : null);
        this.value = value;
    }

    public BaijiRemoteException(Object value, Throwable cause) {
        super(value != null ? value.toString() : null, cause);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}


package com.xqbase.baiji.m2;

import java.nio.ByteBuffer;

/**
 * An object that represents a message, either REST or RPC, and either request or response.
 *<p>
 * Messages are immutable and thread-safe.
 *</p>
 * @author Tony He
 */
public interface Message {

    ByteBuffer getEntity();
}

package com.xqbase.baiji.transport.bridge.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Remove any attachment from {@link io.netty.channel.ChannelHandlerContext}
 *
 * @author Tony He
 */
public class InboundHandlerWithAttachment<T> extends ChannelHandlerAdapter {


    /**
     * Set attachment to the {@link ChannelHandlerContext}. We wrap the actual
     * attachment with an {@link AtomicReference} to make the subsequent remove
     * attachment an atomic operation.
     *
     * @param ctx the {@link ChannelHandlerContext} for the attachment.
     * @param attachment the attachment to set
     */
    public void setAttachment(ChannelHandlerContext ctx, T attachment) {
        AtomicReference<T> atomicRef = new AtomicReference<>(attachment);
        ctx.attr(AttributeKey.newInstance("AsyncPool")).set(atomicRef);
    }

    /**
     * Remove any attachment from the {@link io.netty.channel.ChannelHandlerContext}. It is
     * safe to call this method from multiple threads since the actual attachment can be
     * returned only once.
     *
     * @param ctx the {@link ChannelHandlerContext} for the attachment
     * @return the attachment was removed or null if it doesn't exists
     */
    public T removeAttachment(ChannelHandlerContext ctx) {
        @SuppressWarnings("unchecked")
        AtomicReference<T> atomicRef = (AtomicReference<T>) ctx.attr(AttributeKey.valueOf("AsyncPool")).get();
        T attachment = null;
        if (atomicRef != null) {
            attachment = atomicRef.getAndSet(null);
        }
        return attachment;
    }
}

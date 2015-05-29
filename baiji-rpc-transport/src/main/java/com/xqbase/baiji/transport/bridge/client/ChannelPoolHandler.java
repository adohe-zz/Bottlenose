package com.xqbase.baiji.transport.bridge.client;

import com.xqbase.baiji.transport.apool.AsyncPool;

import java.nio.channels.Channel;

/**
 * Listens for upstream events affecting the state of the channel as
 * it relates to the pool. This handler does not call super because
 * it expects to be the last handler in the pipeline, to ensure that
 * every other handler has had a chance to process the event and finish
 * with the channel.
 *
 * Basically, the handler's job is to return the channel to the pool,
 * or ask the pool to dispose of the channel, after the response is
 * received or after an error occurs.
 *
 * @author Tony He
 */
public class ChannelPoolHandler extends InboundHandlerWithAttachment<AsyncPool<Channel>> {
}

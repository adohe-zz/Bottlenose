package com.xqbase.baiji.transport.bridge.common;

import com.xqbase.baiji.transport.bridge.client.TransportClient;

import java.util.Map;

/**
 * Factory for creating TransportClients. This should be immutable.
 *
 * @author Tony He
 */
public interface TransportClientFactory {

    /**
     * Create a new {@link TransportClient} with the specified properties.
     *
     * @param properties map of properties for the {@link TransportClient}
     * @return an appropriate {@link TransportClient} instance, as specified by the properties.
     */
    TransportClient getClient(Map<String, ?> properties);
}

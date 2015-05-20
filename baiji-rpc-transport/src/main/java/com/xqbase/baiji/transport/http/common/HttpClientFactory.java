package com.xqbase.baiji.transport.http.common;

import com.xqbase.baiji.transport.bridge.client.TransportClient;
import com.xqbase.baiji.transport.bridge.common.TransportClientFactory;

import java.util.Map;

/**
 * Factory for creating HttpClient.
 *
 * @author Tony He
 */
public class HttpClientFactory implements TransportClientFactory {

    @Override
    public TransportClient getClient(Map<String, ?> properties) {
        return null;
    }
}

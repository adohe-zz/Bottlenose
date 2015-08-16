package com.xqbase.bn.transport.http.common;

import com.xqbase.bn.transport.bridge.client.TransportClient;
import com.xqbase.bn.transport.bridge.common.TransportClientFactory;

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

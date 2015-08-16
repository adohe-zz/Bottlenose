package com.xqbase.bn.transport.tcp.common;

import com.xqbase.bn.transport.bridge.client.TransportClient;
import com.xqbase.bn.transport.bridge.common.TransportClientFactory;

import java.util.Map;

/**
 * Factory for creating TcpClient.
 *
 * @author Tony He
 */
public class TcpClientFactory implements TransportClientFactory {

    @Override
    public TransportClient getClient(Map<String, ?> properties) {
        return null;
    }
}

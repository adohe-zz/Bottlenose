package com.xqbase.baiji.transport.tcp.common;

import com.xqbase.baiji.transport.bridge.client.TransportClient;
import com.xqbase.baiji.transport.bridge.common.TransportClientFactory;

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

package com.xqbase.bn.rpc.server.plugin;

import com.xqbase.bn.rpc.server.context.ServiceContext;

/**
 * The plugin interface.
 *
 * @author Tony He
 */
public interface Plugin {

    void register(ServiceContext serviceContext);
}

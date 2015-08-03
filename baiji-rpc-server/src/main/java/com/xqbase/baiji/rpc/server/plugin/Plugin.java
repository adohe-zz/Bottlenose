package com.xqbase.baiji.rpc.server.plugin;

import com.xqbase.baiji.rpc.server.context.ServiceContext;

/**
 * The plugin interface.
 *
 * @author Tony He
 */
public interface Plugin {

    void register(ServiceContext serviceContext);
}

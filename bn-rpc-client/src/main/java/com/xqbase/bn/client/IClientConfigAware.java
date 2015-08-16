package com.xqbase.bn.client;

import com.xqbase.bn.client.config.IClientConfig;

/**
 * There are multiple classes (and components) that need access to the configuration.
 * Its easier to do this by using {@link IClientConfig} as the object that carries these configurations
 * and to define a common interface that components that need this can implement and hence be aware of.
 *
 * @author Tony He
 */
public interface IClientConfigAware {

    void initWithConfig(IClientConfig config);
}

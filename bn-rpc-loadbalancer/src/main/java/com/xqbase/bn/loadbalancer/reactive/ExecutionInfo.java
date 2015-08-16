package com.xqbase.bn.loadbalancer.reactive;

import com.xqbase.bn.loadbalancer.Server;

/**
 * Represents the state of execution for an instance of {@link com.xqbase.bn.loadbalancer.reactive.LoadBalancerCommand}
 * and is passed to {@link ExecutionListener}
 *
 * @author Tony He
 */
public class ExecutionInfo {

    private final Server server;
    private final int numberOfPastAttemptsOnServer;
    private final int numberOfPastServersAttempted;

    private ExecutionInfo(Server server, int numberOfPastAttemptsOnServer, int numberOfPastServersAttempted) {
        this.server = server;
        this.numberOfPastAttemptsOnServer = numberOfPastAttemptsOnServer;
        this.numberOfPastServersAttempted = numberOfPastServersAttempted;
    }

    public static ExecutionInfo create(Server server, int numberOfPastAttemptsOnServer, int numberOfPastServersAttempted) {
        return new ExecutionInfo(server, numberOfPastAttemptsOnServer, numberOfPastServersAttempted);
    }

    public Server getServer() {
        return server;
    }

    public int getNumberOfPastAttemptsOnServer() {
        return numberOfPastAttemptsOnServer;
    }

    public int getNumberOfPastServersAttempted() {
        return numberOfPastServersAttempted;
    }

    @Override
    public String toString() {
        return "ExecutionInfo{" +
                "server=" + server +
                ", numberOfPastAttemptsOnServer=" + numberOfPastAttemptsOnServer +
                ", numberOfPastServersAttempted=" + numberOfPastServersAttempted +
                '}';
    }
}

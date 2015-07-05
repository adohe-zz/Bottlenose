package com.xqbase.baiji.loadbalancer;

/**
 * Class that represents a typical Server (or an addressable Node) i.e. a
 * Host:port identifier
 *
 * @author Tony He
 *
 */
public class Server {

    private boolean isAliveFlag;

    public Server(String s) {
    }

    public void setIsAlive(boolean isAliveFlag) {
        this.isAliveFlag = isAliveFlag;
    }

    public boolean isAlive() {
        return this.isAliveFlag;
    }
}

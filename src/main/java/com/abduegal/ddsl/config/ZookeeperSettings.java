package com.abduegal.ddsl.config;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: A.Egal
 * Date: 3/8/14
 * Time: 3:15 PM
 */
public class ZookeeperSettings {

    @NotEmpty
    private int tickTime;
    @NotEmpty
    private int initLimit;
    @NotEmpty
    private int syncLimit;
    @NotEmpty
    private String dataDir;
    @NotEmpty
    private String dataLogDir;
    @NotEmpty
    private String clientHost;
    @NotEmpty
    private int clientPort;

    public ZookeeperSettings() {
    }

    public int getTickTime() {
        return tickTime;
    }

    public void setTickTime(int tickTime) {
        this.tickTime = tickTime;
    }

    public int getInitLimit() {
        return initLimit;
    }

    public void setInitLimit(int initLimit) {
        this.initLimit = initLimit;
    }

    public int getSyncLimit() {
        return syncLimit;
    }

    public void setSyncLimit(int syncLimit) {
        this.syncLimit = syncLimit;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getDataLogDir() {
        return dataLogDir;
    }

    public void setDataLogDir(String dataLogDir) {
        this.dataLogDir = dataLogDir;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }
}

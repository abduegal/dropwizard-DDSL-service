package com.abduegal.ddsl.config;

import com.yammer.dropwizard.config.Configuration;

/**
 * User: A.Egal
 * Date: 3/8/14
 * Time: 3:02 PM
 */
public class DdslServiceConfig extends Configuration {

    private ZookeeperSettings zookeeper;

    private ConfigWriterSettings configwriter;

    public DdslServiceConfig() {
    }

    public ZookeeperSettings getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZookeeperSettings zookeeper) {
        this.zookeeper = zookeeper;
    }

    public ConfigWriterSettings getConfigwriter() {
        return configwriter;
    }

    public void setConfigwriter(ConfigWriterSettings configwriter) {
        this.configwriter = configwriter;
    }
}

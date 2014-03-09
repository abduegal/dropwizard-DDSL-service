package com.abduegal.ddsl;

import com.abduegal.ddsl.config.DdslServiceConfig;
import com.abduegal.ddsl.resource.DdslResource;
import com.abduegal.ddsl.srv.ConfigwriterSrv;
import com.abduegal.ddsl.srv.ZookeeperSrv;
import com.abduegal.ddsl.srv.impl.ConfigWriterImpl;
import com.abduegal.ddsl.srv.impl.ZookeeperSrvImpl;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * Main service class.
 * User: A.Egal
 * Date: 3/8/14
 * Time: 3:01 PM
 */
public class DdslService extends Service<DdslServiceConfig>{

    public static void main(String[] args) throws Exception{
        new DdslService().run(args);
    }

    @Override
    public void initialize(Bootstrap<DdslServiceConfig> bootstrap) {
        bootstrap.setName("Ddsl service");
    }

    @Override
    public void run(DdslServiceConfig config, Environment environment) throws Exception {
        ZookeeperSrv zookeeperSrv = new ZookeeperSrvImpl(config);
        zookeeperSrv.startZookeeper();

        ConfigwriterSrv configwriterSrv = new ConfigWriterImpl(config);
        configwriterSrv.startLoadbalancer();

        environment.addResource(new DdslResource());
        environment.addHealthCheck(new DdslHealthCheck("Ddsl service"));
    }
}

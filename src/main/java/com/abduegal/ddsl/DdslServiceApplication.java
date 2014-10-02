package com.abduegal.ddsl;

import com.abduegal.ddsl.config.DdslServiceConfig;
import com.abduegal.ddsl.resource.DdslResource;
import com.abduegal.ddsl.srv.ConfigwriterSrv;
import com.abduegal.ddsl.srv.ZookeeperSrv;
import com.abduegal.ddsl.srv.impl.ConfigWriterImpl;
import com.abduegal.ddsl.srv.impl.ZookeeperSrvImpl;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Main service class.
 * User: A.Egal
 * Date: 3/8/14
 * Time: 3:01 PM
 */
public class DdslServiceApplication extends Application<DdslServiceConfig> {

    public static void main(String[] args) throws Exception {
        new DdslServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "DDSL Service";
    }

    @Override
    public void initialize(Bootstrap<DdslServiceConfig> bootstrap) {

    }

    @Override
    public void run(DdslServiceConfig config, Environment environment) throws Exception {
        ZookeeperSrv zookeeperSrv = new ZookeeperSrvImpl(config);
        zookeeperSrv.startZookeeper();

        ConfigwriterSrv configwriterSrv = new ConfigWriterImpl(config);
        configwriterSrv.startLoadbalancer();
        environment.jersey().register(new DdslResource());
        environment.healthChecks().register("Ddsl service", new DdslHealthCheck("Ddsl service"));

    }
}

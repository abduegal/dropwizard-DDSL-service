package com.abduegal.ddsl.srv.impl;

import com.abduegal.ddsl.config.DdslServiceConfig;
import com.abduegal.ddsl.config.ZookeeperSettings;
import com.abduegal.ddsl.srv.ConfigwriterSrv;
import com.kjetland.ddsl.DdslClient;
import com.kjetland.ddsl.DdslClientImpl;
import com.kjetland.ddsl.config.DdslConfig;
import com.kjetland.ddsl.config.DdslConfigManualImpl;
import com.kjetland.ddsl.model.ServiceLocation;
import com.kjetland.ddsl.model.ServiceWithLocations;
import io.dropwizard.servlets.assets.ResourceNotFoundException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: A.Egal
 * Date: 3/8/14
 * Time: 10:01 PM
 */
public class ConfigWriterImpl implements ConfigwriterSrv {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigWriterImpl.class);

    private final ExecutorService pool = Executors.newFixedThreadPool(1);

    private final String configDestination;
    private final String templateUrl = "com/abduegal/ddsl/srv/impl/template.conf";
    private final String shellCommand;
    private final int secondsBetweenChecks;
    private final DdslServiceConfig config;

    private DdslClient client = null;

    public ConfigWriterImpl(DdslServiceConfig config) {
        this.config = config;
        this.configDestination = config.getConfigwriter().getDestination();
        this.shellCommand = config.getConfigwriter().getShellCommand();
        this.secondsBetweenChecks = config.getConfigwriter().getSecondsBetweenChecks();
    }

    public Future<Void> startLoadbalancer() {
        return pool.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                startScheduler(config.getZookeeper());
                return null;
            }
        });
    }

    private void startScheduler(final ZookeeperSettings zookeeperSettings) throws InterruptedException {
        while (true) {
            try {

                writeConfig(zookeeperSettings);
                triggerReloading();

            } catch (Exception e) {
                LOGGER.error("an exception has occurred while the configwriter was running, retrying", e);
            }
            Thread.sleep(secondsBetweenChecks * 1000);
        }
    }

    private void triggerReloading() {
        LOGGER.info("Triggering reloading with cmd: {}", shellCommand);

        try {
            Process process = Runtime.getRuntime().exec(shellCommand);
            int errorLevel = process.waitFor();
            LOGGER.info("Triggered reloading. errorCode: {}", errorLevel);

        } catch (IOException | InterruptedException ex) {
            LOGGER.error("Error triggering reloading:", ex);
        }
    }

    private void writeConfig(ZookeeperSettings config) throws MalformedURLException, IOException {

        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
        VelocityContext context = new VelocityContext();

        context.put("serviceLocations", getServiceLocation(config));

        Template template = null;
        try {
            template = Velocity.getTemplate(templateUrl);
        } catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException err) {
            LOGGER.error("Error writing the template", err);
        }

        StringWriter sw = new StringWriter();

        template.merge(context, sw);
        String outputData = sw.toString();

        Files.write(new File(configDestination).toPath(), outputData.getBytes());
    }

    /**
     * Complex object warning! short explanation :)
     * The Path are stored in the Map<Path, Server location> list.
     * The server location (SL) tells the IP # and Port # of the server.
     *
     * @return
     */
    private Map<String, List<SL>> getServiceLocation(ZookeeperSettings config) throws MalformedURLException {
        ServiceWithLocations[] swls = getClient(config).getAllAvailableServices();

        Map<String, List<SL>> map = new LinkedHashMap<>();

        for (ServiceWithLocations serviceWithLocations : swls) {
            for (ServiceLocation serviceLocation : serviceWithLocations.locations()) {
                URL url = new URL(serviceLocation.url());
                SL sl = new SL(url.getHost(), url.getPath(), url.getPort());
                if (map.get(sl.getPathAsKey()) == null) {
                    map.put(sl.getPathAsKey(), new ArrayList<SL>());
                }
                map.get(sl.getPathAsKey()).add(sl);
            }

        }
        return map;
    }

    private DdslClient getClient(ZookeeperSettings config) {
        if (this.client == null) {
            DdslConfig ddslConfig = new DdslConfigManualImpl(
                    String.format("%s:%d", config.getClientHost(), config.getClientPort()));

            this.client = new DdslClientImpl(ddslConfig);
        }
        return this.client;
    }

    public static final class SL {
        private final String host;
        private final Integer port;
        private final String path;

        public SL(String host, String path, Integer port) {
            this.host = host;
            this.path = path.substring(1); //remove first forward slash
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }

        public String getPath() {
            return path;
        }

        public String getPathAsKey() {
            if (path.isEmpty()) {
                return "root";
            }
            return path;
        }

    }


}

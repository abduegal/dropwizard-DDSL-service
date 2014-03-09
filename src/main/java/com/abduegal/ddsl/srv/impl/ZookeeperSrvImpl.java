package com.abduegal.ddsl.srv.impl;

import com.abduegal.ddsl.config.DdslServiceConfig;
import com.abduegal.ddsl.config.ZookeeperSettings;
import com.abduegal.ddsl.srv.ZookeeperSrv;
import org.apache.zookeeper.server.NIOServerCnxn;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: A.Egal
 * Date: 3/8/14
 * Time: 3:08 PM
 */
public class ZookeeperSrvImpl implements ZookeeperSrv {

    private final ExecutorService pool = Executors.newFixedThreadPool(1);

    private NIOServerCnxn.Factory cnxnFactory;

    public ZookeeperSrvImpl(DdslServiceConfig config) {
        this.config = config;
    }

    @Override
    public Future<Void> startZookeeper() throws IOException, InterruptedException{
        return pool.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                runZookeeper(config.getZookeeper());
                return null;
            }
        });
    }

    private final DdslServiceConfig config;

    private void runZookeeper(ZookeeperSettings config) throws IOException, InterruptedException{
        try{
            ZooKeeperServer zkServer = new ZooKeeperServer();
            FileTxnSnapLog ftxn = new FileTxnSnapLog(new File(config.getDataLogDir()), new File(config.getDataDir()));

            zkServer.setTxnLogFactory(ftxn);
            zkServer.setTickTime(config.getTickTime());
            zkServer.setMinSessionTimeout(config.getInitLimit());
            InetSocketAddress address = new InetSocketAddress(config.getClientHost(), config.getClientPort());

            cnxnFactory = new NIOServerCnxn.Factory(address, config.getSyncLimit());

            cnxnFactory.startup(zkServer);
            cnxnFactory.join();
            if (zkServer.isRunning()) {
                zkServer.shutdown();
            }
        } catch (InterruptedException e) {
            // warn, but generally this is ok
        }
    }

}

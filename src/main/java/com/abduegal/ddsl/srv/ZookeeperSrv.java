package com.abduegal.ddsl.srv;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Starts the zookeeper service
 * User: A.Egal
 * Date: 3/8/14
 * Time: 3:07 PM
 */
public interface ZookeeperSrv {

    /**
     * Starts the zookeeper service
     */
    Future<Void> startZookeeper() throws IOException, InterruptedException;

}

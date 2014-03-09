package com.abduegal.ddsl.srv;

import java.util.concurrent.Future;

/**
 * User: A.Egal
 * Date: 3/9/14
 * Time: 1:32 AM
 */
public interface ConfigwriterSrv {

    /**
     * Starts the loadbalancer configwriter.
     */
    Future<Void> startLoadbalancer();

}

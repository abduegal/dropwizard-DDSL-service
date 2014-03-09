package com.abduegal.ddsl;

import com.yammer.metrics.core.HealthCheck;

/**
 * User: A.Egal
 * Date: 3/9/14
 * Time: 1:18 AM
 */
public class DdslHealthCheck extends HealthCheck {

    public DdslHealthCheck(String name) {
        super(name);
    }

    @Override
    protected Result check() throws Exception {
        //TODO create health checks (to monitor if nginx is still available).
        return Result.healthy();
    }
}

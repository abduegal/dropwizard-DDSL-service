package com.abduegal.ddsl.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * User: A.Egal
 * Date: 3/8/14
 * Time: 3:12 PM
 */
@Path("/")
public class DdslResource {

    public DdslResource() {
    }

    @GET
    public String get() {
        return "Ddsl service is running";
    }
}

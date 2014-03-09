package com.abduegal.ddsl.config;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: A.Egal
 * Date: 3/9/14
 * Time: 1:24 AM
 */
public class ConfigWriterSettings {

    @NotEmpty
    private String destination;
    @NotEmpty
    private String shellCommand;
    @NotEmpty
    private int secondsBetweenChecks;

    public ConfigWriterSettings() {
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getShellCommand() {
        return shellCommand;
    }

    public void setShellCommand(String shellCommand) {
        this.shellCommand = shellCommand;
    }

    public int getSecondsBetweenChecks() {
        return secondsBetweenChecks;
    }

    public void setSecondsBetweenChecks(int secondsBetweenChecks) {
        this.secondsBetweenChecks = secondsBetweenChecks;
    }
}

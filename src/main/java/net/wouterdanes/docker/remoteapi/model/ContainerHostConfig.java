package net.wouterdanes.docker.remoteapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by jdcasey on 7/22/16.
 */
public class ContainerHostConfig
{
    @JsonProperty("NetworkMode")
    private String networkMode;

    public String getNetworkMode()
    {
        return networkMode;
    }

    public ContainerHostConfig withNetworkMode( String networkMode )
    {
        this.networkMode = networkMode;
        return this;
    }
}

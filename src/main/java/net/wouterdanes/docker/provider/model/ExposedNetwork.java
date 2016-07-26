package net.wouterdanes.docker.provider.model;

import net.wouterdanes.docker.remoteapi.model.Network;

/**
 * Created by jdcasey on 7/26/16.
 */
public class ExposedNetwork
{
    private String name;

    private String ipAddress;

    public ExposedNetwork( String name, String ipAddress )
    {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public String getName()
    {
        return name;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }
}

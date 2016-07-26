package net.wouterdanes.docker.remoteapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by jdcasey on 7/26/16.
 */
public class Network
{
    @JsonProperty( "Gateway" )
    private String gateway;

    @JsonProperty( "IPAddress" )
    private String ipAddress;

    @JsonProperty( "IPPrefixLen" )
    private int ipPrefixLen;

    public String getGateway()
    {
        return gateway;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public int getIpPrefixLen()
    {
        return ipPrefixLen;
    }

}

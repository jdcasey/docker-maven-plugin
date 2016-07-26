package net.wouterdanes.docker.remoteapi.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jdcasey on 7/26/16.
 */
public class NetworkSettings
        extends Network
{
    @JsonProperty( "Bridge" )
    private String bridge;

    @JsonProperty( "Ports" )
    private Map<String, List<PortMappingInfo>> ports;

    @JsonProperty( "Networks" )
    private Map<String, Network> networks;

    public String getBridge()
    {
        return bridge;
    }

    public Map<String, List<PortMappingInfo>> getPorts()
    {
        return Collections.unmodifiableMap( ports );
    }

    public Map<String, Network> getNetworks()
    {
        return Collections.unmodifiableMap( networks );
    }

    public static class PortMappingInfo
    {
        @JsonProperty( "HostIp" )
        private String hostIp;

        @JsonProperty( "HostPort" )
        private int hostPort;

        public String getHostIp()
        {
            return hostIp;
        }

        public int getHostPort()
        {
            return hostPort;
        }
    }
}

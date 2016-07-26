package net.wouterdanes.docker.provider.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by jdcasey on 7/26/16.
 */
public class ExposedNetworkInfo
{
    private List<ExposedPort> exposedPorts;

    private List<ExposedNetwork> exposedNetworks;

    public List<ExposedPort> getExposedPorts()
    {
        return exposedPorts == null ? Collections.emptyList() : exposedPorts;
    }

    public List<ExposedNetwork> getExposedNetworks()
    {
        return exposedNetworks == null ? Collections.emptyList() : exposedNetworks;
    }

    public ExposedNetworkInfo withExposedNetworks( List<ExposedNetwork> exposedNetworks )
    {
        this.exposedNetworks = exposedNetworks;
        return this;
    }

    public ExposedNetworkInfo withExposedPorts( List<ExposedPort> exposedPorts )
    {
        this.exposedPorts = exposedPorts;
        return this;
    }
}

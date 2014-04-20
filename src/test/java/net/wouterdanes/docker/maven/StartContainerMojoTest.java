package net.wouterdanes.docker.maven;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import net.wouterdanes.docker.provider.DockerProvider;

public class StartContainerMojoTest {

    @Test
    public void testThatMojoStartsAContainerOnTheProvider() throws Exception {

        Mockito.when(FakeDockerProvider.instance.startContainer(Matchers.any(ContainerStartConfiguration.class)))
                .thenReturn("someId");
        DockerProviderSupplier.registerProvider("fake", FakeDockerProvider.class);

        ContainerStartConfiguration startConfiguration = new ContainerStartConfiguration();
        StartContainerMojo mojo = new StartContainerMojo(Arrays.asList(startConfiguration));
        mojo.setProviderName("fake");
        mojo.setPluginContext(new HashMap());

        mojo.execute();

        Mockito.verify(FakeDockerProvider.instance).startContainer(startConfiguration);
    }

    public static class FakeDockerProvider implements DockerProvider {

        private static FakeDockerProvider instance = Mockito.mock(FakeDockerProvider.class);

        private final FakeDockerProvider proxy;

        public FakeDockerProvider() {
            proxy = instance;
        }

        @Override
        public String startContainer(final ContainerStartConfiguration configuration) {
            return proxy.startContainer(configuration);
        }

        @Override
        public void stopContainer(final String containerId) {

        }

        @Override
        public void deleteContainer(final String containerId) {

        }
    }
}

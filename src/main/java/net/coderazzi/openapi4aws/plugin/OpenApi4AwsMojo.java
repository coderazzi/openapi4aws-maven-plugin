package net.coderazzi.openapi4aws.plugin;


import net.coderazzi.openapi4aws.O4A_Exception;
import net.coderazzi.openapi4aws.Openapi4AWS;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Mojo(name = "transform", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class OpenApi4AwsMojo extends AbstractMojo {
    @Parameter
    private List<Authorizer> authorizers;
    @Parameter
    private List<Integration> integrations;
    @Parameter
    private List<Transform> transforms;

    public void execute() throws MojoExecutionException {
        checkParametersList(authorizers);
        checkParametersList(integrations);
        checkParametersList(transforms);

        ConfigurationAdapter adapter = new ConfigurationAdapter(authorizers, integrations);
        Openapi4AWS openapi4AWS = new Openapi4AWS(adapter);

        for (Transform transform : transforms) {
            try {
                openapi4AWS.handle(adapter.getPaths(transform), Paths.get(transform.getOutputFolder()));
            } catch (IOException io) {
                throw new MojoExecutionException(io.toString(), io);
            } catch (O4A_Exception iex) {
                throw new MojoExecutionException(iex.getMessage(), iex);
            }
        }
    }

    private <T extends ConfigurationParameter> void checkParametersList(List<T> parameters) throws MojoExecutionException {
        if (parameters != null) {
            for (ConfigurationParameter each : parameters) {
                each.check();
            }
        }
    }
}

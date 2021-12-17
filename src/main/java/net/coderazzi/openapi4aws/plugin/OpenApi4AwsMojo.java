package net.coderazzi.openapi4aws.plugin;


import net.coderazzi.openapi4aws.O4A_Exception;
import net.coderazzi.openapi4aws.Openapi4AWS;
import net.coderazzi.openapi4aws.cli.CliParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mojo(name = "transform", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class OpenApi4AwsMojo extends AbstractMojo {
    @Parameter
    private List<Authorizer> authorizers;
    @Parameter
    private List<Integration> integrations;
    @Parameter
    private List<Transform> transforms;
    @Parameter
    private List<String> external;

    public void execute() throws MojoExecutionException {
        try {
            List<Task> tasks = new ArrayList<>();
            if (external != null) {
                for (String each : external) {
                    try {
                        CliParser parser = new CliParser(each);
                        addToList(authorizers, parser.getAuthorizers(), Authorizer::copyFrom);
                        addToList(integrations, parser.getPathIntegrations(), PathIntegration::copyFrom);
                        addToList(integrations, parser.getTagIntegrations(), TagIntegration::copyFrom);
                        tasks.add(new Task(parser.getPaths(), parser.getOutputFolder()));
                    } catch (O4A_Exception iex) {
                        throw new MojoExecutionException("External file '" + each + "': " + iex.getMessage(), iex);
                    }
                }
            }
            ConfigurationAdapter adapter = new ConfigurationAdapter(authorizers, integrations);
            Openapi4AWS openapi4AWS = new Openapi4AWS(adapter);

            try {
                for (Transform t : transforms) {
                    tasks.add(new Task(adapter.getPaths(t), Paths.get(t.getOutputFolder())));
                }
            } catch (IOException io) {
                throw new MojoExecutionException(io.toString(), io);
            }

            for (Task task : tasks) {
                openapi4AWS.handle(task.paths, task.outputFolder);
            }
        } catch (O4A_Exception iex) {
            throw new MojoExecutionException(iex.getMessage(), iex);
        }
    }

    private <T, U extends T> void addToList(List<U> target, Map<String, T> newSources, Creator<T, U> creator) {
        newSources.entrySet().stream()
                .map(x -> creator.create(x.getKey(), x.getValue()))
                .forEachOrdered(target::add);
    }

    private interface Creator<T, U> {
        U create(String key, T base);
    }

    private static class Task {
        Collection<Path> paths;
        Path outputFolder;

        Task(Collection<Path> paths, Path outputFolder) {
            this.paths = paths;
            this.outputFolder = outputFolder;
        }
    }

}

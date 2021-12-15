package net.coderazzi.openapi4aws.plugin;

import net.coderazzi.openapi4aws.Configuration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class ConfigurationAdapter extends Configuration {

    private final Map<String, Authorizer> authorizers;
    private final Map<String, PathIntegration> pathIntegrations;
    private final Map<String, TagIntegration> tagIntegrations;

    ConfigurationAdapter(List<net.coderazzi.openapi4aws.plugin.Authorizer> authorizers,
                         List<net.coderazzi.openapi4aws.plugin.Integration> integrations) {

        this.authorizers = authorizers.stream().collect(Collectors.toMap(
                net.coderazzi.openapi4aws.plugin.Authorizer::getName, Function.identity()));

        pathIntegrations = integrations
                .stream()
                .filter(item -> item instanceof PathIntegration)
                .map(item -> (PathIntegration) item)
                .collect(Collectors.toMap(PathIntegration::getPath, Function.identity()
                ));

        tagIntegrations = integrations
                .stream()
                .filter(item -> item instanceof TagIntegration)
                .map(item -> (TagIntegration) item)
                .collect(Collectors.toMap(TagIntegration::getTag, Function.identity()
                ));
    }


    @Override
    public Map<String, Authorizer> getAuthorizers() {
        return authorizers;
    }

    @Override
    public Integration getIntegration(String path, List<String> tags) {
        return getIntegration(path, tags, pathIntegrations, tagIntegrations);
    }

    public Collection<Path> getPaths(Transform transform) throws IOException {
        return getPaths(transform.getFilenames(), transform.getGlobs());
    }

}

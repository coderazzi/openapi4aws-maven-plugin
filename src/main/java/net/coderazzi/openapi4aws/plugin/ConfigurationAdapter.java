package net.coderazzi.openapi4aws.plugin;

import net.coderazzi.openapi4aws.Configuration;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class ConfigurationAdapter extends Configuration {

    private final Map<String, Authorizer> authorizers;
    private final Map<String, PathIntegration> pathIntegrations;
    private final Map<String, TagIntegration> tagIntegrations;
    private final Log log;

    ConfigurationAdapter(List<net.coderazzi.openapi4aws.plugin.Authorizer> authorizers,
                         List<net.coderazzi.openapi4aws.plugin.Integration> integrations,
                         Log log)
            throws MojoExecutionException {
        this.log = log;

        List<PathIntegration> pathIntegrations = integrations
                .stream()
                .filter(item -> item instanceof PathIntegration)
                .map(item -> (PathIntegration) item)
                .collect(Collectors.toList());

        List<TagIntegration> tagIntegrations = integrations
                .stream()
                .filter(item -> item instanceof TagIntegration)
                .map(item -> (TagIntegration) item)
                .collect(Collectors.toList());

        checkParametersList(authorizers, net.coderazzi.openapi4aws.plugin.Authorizer::getName);
        checkParametersList(pathIntegrations, net.coderazzi.openapi4aws.plugin.PathIntegration::getPath);
        checkParametersList(tagIntegrations, net.coderazzi.openapi4aws.plugin.TagIntegration::getTag);

        this.authorizers = authorizers.stream().collect(Collectors.toMap(
                net.coderazzi.openapi4aws.plugin.Authorizer::getName, Function.identity()));
        this.pathIntegrations = pathIntegrations.stream()
                .collect(Collectors.toMap(PathIntegration::getPath, Function.identity()));
        this.tagIntegrations = tagIntegrations.stream()
                .collect(Collectors.toMap(TagIntegration::getTag, Function.identity()));
    }


    @Override
    public Map<String, Authorizer> getAuthorizers() {
        return authorizers;
    }

    @Override
    public Integration getIntegration(String path, List<String> tags) {
        Integration ret = getIntegration(path, tags, pathIntegrations, tagIntegrations);
        log.debug(String.format("integration for %s, with tags %s, returning %s",
                path, String.join("/", tags), ret==null? "null" : ret.getAuthorizer()));
        return ret;
    }

    public Collection<Path> getPaths(Transform transform) throws IOException {
        Collection<Path> ret = getPaths(transform.getInput(), transform.getGlobInput());
        if (ret.isEmpty()) {
            log.debug("Transform defined no paths");
        } else {
            ret.forEach( x -> log.info("Path to handle: " + x.toString()));
        }
        return ret;
    }

    /**
     * Verifies that all elements in the specified list are correctly / fully specified, and that the list
     * does not define repeated items
     *
     * @param parameters list to verify
     * @param getter     retrieves the unique key for each element, which must be unique in the whole lisy
     * @param <T>        A ConfigurationParameter instance
     * @throws MojoExecutionException if the list is not correct
     */
    private <T extends ConfigurationParameter> void checkParametersList(List<T> parameters, UniqueGetter<T> getter)
            throws MojoExecutionException {
        if (parameters != null) {
            Set<String> keys = new HashSet<>();
            for (T each : parameters) {
                each.check();
                String key = getter.get(each);
                if (!keys.add(key)) {
                    ConfigurationParameter.throwException("Repeated " + each.getElementName() + " " + key);
                }
            }
        }
    }

    private interface UniqueGetter<T> {
        String get(T self);
    }

}

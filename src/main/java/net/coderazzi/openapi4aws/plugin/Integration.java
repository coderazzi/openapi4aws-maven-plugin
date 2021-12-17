package net.coderazzi.openapi4aws.plugin;

import net.coderazzi.openapi4aws.Configuration;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;

abstract class Integration extends ConfigurationParameter implements Configuration.Integration {

    protected String uri;
    protected String authorizer;
    protected List<String> scopes;

    public String getAuthorizer() {
        return authorizer;
    }

    public List<String> getScopes() {
        return scopes;
    }

    @Override
    public void check() throws MojoExecutionException {
        requireField(uri, "uri");
        if (scopes != null && !scopes.isEmpty()) {
            requireField(authorizer, "authorization");
        }
    }

}

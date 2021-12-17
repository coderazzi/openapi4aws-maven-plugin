package net.coderazzi.openapi4aws.plugin;

import net.coderazzi.openapi4aws.Configuration;
import org.apache.maven.plugin.MojoExecutionException;

public class PathIntegration extends Integration {

    private String path;

    static PathIntegration copyFrom(String path, Configuration.Integration base) {
        PathIntegration ret = new PathIntegration();
        ret.path = path;
        ret.uri = base.getUri("");
        ret.authorizer = base.getAuthorizer();
        ret.scopes = base.getScopes();
        return ret;
    }

    public String getPath() {
        return path;
    }

    public String getUri(String path) {
        return uri;
    }

    @Override
    public void check() throws MojoExecutionException {
        requireField(path, "route");
        super.check();
    }
}

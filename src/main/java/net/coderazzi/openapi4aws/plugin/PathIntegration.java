package net.coderazzi.openapi4aws.plugin;

import org.apache.maven.plugin.MojoExecutionException;

public class PathIntegration extends Integration {

    private String path;

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

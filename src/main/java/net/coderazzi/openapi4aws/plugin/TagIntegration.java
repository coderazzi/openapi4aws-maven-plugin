package net.coderazzi.openapi4aws.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.Locale;

public class TagIntegration extends Integration {

    private String tag;

    public String getTag() {
        return tag;
    }

    @Override
    public String getUri(String path) {
        return uri + path;
    }

    @Override
    public void check() throws MojoExecutionException {
        super.check();
        requireField(tag, "tag");
        tag = tag.toLowerCase(Locale.ROOT);
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length()-1);
        }
    }

}

package net.coderazzi.openapi4aws.plugin;

import net.coderazzi.openapi4aws.Configuration;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Locale;

public class TagIntegration extends Integration {

    private String tag;

    static TagIntegration copyFrom(String tag, Configuration.Integration base) {
        TagIntegration ret = new TagIntegration();
        ret.tag = tag;
        ret.uri = base.getUri("");
        ret.authorizer = base.getAuthorizer();
        ret.scopes = base.getScopes();
        return ret;
    }

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
            uri = uri.substring(0, uri.length() - 1);
        }
    }

}

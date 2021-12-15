package net.coderazzi.openapi4aws.plugin;

import net.coderazzi.openapi4aws.Configuration;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Authorizer extends ConfigurationParameter implements Configuration.Authorizer {

    private final static String AUTHORIZATION_TYPE = "oauth2";
    private final static String TYPE = "jwt";

    private String name;
    private String identitySource;
    private String issuer;
    private List<String> audiences;
    private String authorizationType = AUTHORIZATION_TYPE;
    private String type = TYPE;

    public String getName() {
        return name;
    }

    @Override
    public String getIdentitySource() {
        return identitySource;
    }

    @Override
    public List<String> getAudience() {
        return audiences;
    }

    @Override
    public String getAuthorizationType() {
        return authorizationType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public Map<Object, Object> getFlows() {
        return Collections.emptyMap();
    }

    @Override
    public void check() throws MojoExecutionException {
        requireField(name, "name");
        requireField(identitySource, "identity source");
        requireField(issuer, "issuer");
        requireField(audiences, "audiences");
        if (audiences.size() == 0) {
            throwException("Authorizer definition does not include any audience values");
        }
        requireValue(authorizationType, AUTHORIZATION_TYPE, "authorization type");
        requireValue(type, TYPE, "type");
    }
}

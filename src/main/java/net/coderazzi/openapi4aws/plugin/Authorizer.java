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
    private List<String> audience;
    private String authorizationType = AUTHORIZATION_TYPE;
    private String type = TYPE;

    static Authorizer copyFrom(String name, Configuration.Authorizer base) {
        Authorizer ret = new Authorizer();
        ret.name = name;
        ret.identitySource = base.getIdentitySource();
        ret.issuer = base.getIssuer();
        ret.audience = base.getAudience();
        ret.authorizationType = base.getAuthorizationType();
        ret.type = base.getType();
        return ret;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getIdentitySource() {
        return identitySource;
    }

    @Override
    public List<String> getAudience() {
        return audience;
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
        requireField(audience, "audiences");
        if (audience.size() == 0) {
            throwException("Authorizer definition does not include any audience values");
        }
        requireValue(authorizationType, AUTHORIZATION_TYPE, "authorization type");
        requireValue(type, TYPE, "type");
    }
}

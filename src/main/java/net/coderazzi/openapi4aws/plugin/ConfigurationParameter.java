package net.coderazzi.openapi4aws.plugin;

import org.apache.maven.plugin.MojoExecutionException;

abstract class ConfigurationParameter {

    public static void throwException(String msg) throws MojoExecutionException {
        throw new MojoExecutionException(null, msg, msg);
    }

    public abstract void check() throws MojoExecutionException;

    protected void requireField(Object field, String fieldName) throws MojoExecutionException {
        if (field == null) {
            throwException(String.format("Missing %s in %s definition", fieldName, getElementName()));
        }
    }

    protected void requireValue(String fieldValue, String requiredValue, String fieldName) throws MojoExecutionException {
        if (!requiredValue.equals(fieldValue)) {
            throwException(String.format("field %s in %s definition can only have value %s",
                    fieldName, getElementName(), requiredValue));
        }
    }

    public String getElementName() {
        return getClass().getSimpleName().replaceAll("([^^])([A-Z][a-z])", "$1-$2").toLowerCase();
    }
}

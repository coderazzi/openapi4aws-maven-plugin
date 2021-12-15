package net.coderazzi.openapi4aws.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;

public class Transform extends ConfigurationParameter {

    private List<String> filenames;
    private List<String> globs;
    private String outputFolder;

    public List<String> getFilenames() {
        return filenames;
    }

    public List<String> getGlobs() {
        return globs;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    @Override
    public void check() throws MojoExecutionException {
        if ((filenames == null || filenames.isEmpty()) && (globs == null || globs.isEmpty())) {
            throwException(String.format("Missing filenames or globs in %s definition", getElementName()));
        }
    }

}

package net.coderazzi.openapi4aws.plugin;

import java.util.List;

public class Transform extends ConfigurationParameter {

    private List<String> input;
    private List<String> globInput;
    private String outputFolder;

    public List<String> getInput() {
        return input;
    }

    public List<String> getGlobInput() {
        return globInput;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    @Override
    public void check() {
    }

}

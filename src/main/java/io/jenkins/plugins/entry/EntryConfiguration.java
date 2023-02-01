package io.jenkins.plugins.entry;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

/**
 * Example of Jenkins global configuration.
 */
@Extension
public class EntryConfiguration extends GlobalConfiguration {
    /** @return the singleton instance */
    public static EntryConfiguration get() {
        return ExtensionList.lookupSingleton(EntryConfiguration.class);
    }

    private String name;
    private String callback;

    public EntryConfiguration() {
        // When Jenkins is restarted, load any saved configuration from disk.
        load();
    }

    /** @return the currently configured name, if any */
    public String getName() {
        return name;
    }

    /**
     * Together with {@link #getName}, binds to entry in {@code config.jelly}.
     * @param name the new value of this field
     */
    @DataBoundSetter
    public void setName(String name) {
        this.name = name;
        save();
    }


    /** @return the currently configured callback, if any */
    public String getCallback() {
        return callback;
    }

    /**
     * Together with {@link #getCallback}, binds to entry in {@code config.jelly}.
     * @param callback the new value of this field
     */
    @DataBoundSetter
    public void setCallback(String callback) {
        this.callback = callback;
        save();
    }

    public FormValidation doCheckCallback(@QueryParameter String value) {
        if (StringUtils.isEmpty(value)) {
            return FormValidation.warning("Please specify callback address.");
        }
        return FormValidation.ok();
    }

}

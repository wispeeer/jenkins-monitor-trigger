package io.jenkins.plugins.entry;

import java.util.logging.Logger;

public class Loggers {
    private static final Logger logger = Logger.getLogger(StatsdWrapper.class.getName());

    public Logger log() {
        return logger;
    }
}

package be.howest.ti.adria.logic.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private final Properties properties = new Properties();
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    private static final Config INSTANCE = new Config();

    public static Config getInstance() {
        return INSTANCE;
    }

    private Config() {
        try {
            properties.load(getClass().getResourceAsStream("/config/config.properties"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to load properties from file.", ex);
        }
    }

    public String read(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }


}

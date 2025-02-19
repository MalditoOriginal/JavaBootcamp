package edu.school21.chat.Main;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class Application {
    private static final Properties PROPERTIES = new Properties();
    private Application() {}

    static {
        try {
            loadProperties();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void loadProperties() throws IOException {
        var inputStream = Application.class.getClassLoader().getResourceAsStream("application.properties");
        if (inputStream == null) {
            throw new IOException("Could not find application.properties in classpath");
        }
        PROPERTIES.load(inputStream);
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
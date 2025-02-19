package edu.school21.chat.Main;

import java.io.IOException;
import java.util.Properties;


public final class Application {
    private static final Properties PROPERTIES = new Properties();
    private Application() {

    }
    static {
        try {
            loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

private static void loadProperties() throws IOException {
    try (var inputStream = Application.class.getClassLoader().getResourceAsStream("application.properties")) {
        if (inputStream == null) {
            throw new IOException("application.properties not found in classpath");
        }
        PROPERTIES.load(inputStream);
    }
}

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
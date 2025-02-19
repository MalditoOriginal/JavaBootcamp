package edu.school21.chat.Models.Main;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class Application {
    private static final Properties PROPERTIES = new Properties();

    private Application() {
        // Приватный конструктор, чтобы запретить создание экземпляров класса
    }

    static {
        loadProperties();
    }

    private static void loadProperties() {
        // Загрузка файла через ClassLoader, который ищет в classpath
        var inputStream = Application.class.getResourceAsStream("/application.properties");
        if (inputStream == null) {
            throw new UncheckedIOException(new IOException("application.properties not found in classpath"));
        }
        try (inputStream) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
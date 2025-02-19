package edu.school21.chat.Models.Main;

import java.io.IOException;
import java.util.Properties;

public final class Application {
    private static final Properties PROPERTIES = new Properties();

    // Приватный конструктор, чтобы предотвратить создание экземпляра
    private Application() {}

    // Статический блок инициализации для загрузки свойств при запуске программы
    static {
        loadProperties();
    }

    // Метод для загрузки файла конфигурации
    private static void loadProperties() {
        // Использование ClassLoader для поиска файла в ресурсах
        var inputStream = Application.class.getResourceAsStream("/application.properties");
        if (inputStream == null) {
            throw new RuntimeException("Файл application.properties не найден");
        }
        try (inputStream) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке конфигурационного файла", e);
        }
    }

    // Метод для получения значений по ключу
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}

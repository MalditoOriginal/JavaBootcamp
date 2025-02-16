package edu.school21.orm.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.school21.orm.manager.OrmManager;
import edu.school21.orm.models.User;

public class Program {
    public static void main(String[] args) {
        // Настройка подключения к базе данных
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("postgres");
        
        HikariDataSource dataSource = new HikariDataSource(config);
        OrmManager ormManager = new OrmManager(dataSource);

        try {
            // Инициализация таблиц
            System.out.println("Initializing tables...");
            ormManager.init(User.class);

            // Создание и сохранение пользователя
            System.out.println("\nSaving new user...");
            User user = new User("John", "Doe", 25);
            ormManager.save(user);
            System.out.println("Saved user: " + user);

            // Поиск пользователя по ID
            System.out.println("\nFinding user by ID...");
            User foundUser = ormManager.findById(user.getId(), User.class);
            System.out.println("Found user: " + foundUser);

            // Обновление пользователя
            System.out.println("\nUpdating user...");
            foundUser.setAge(26);
            foundUser.setFirstName("Johnny");
            ormManager.update(foundUser);

            // Проверка обновления
            System.out.println("\nVerifying update...");
            User updatedUser = ormManager.findById(user.getId(), User.class);
            System.out.println("Updated user: " + updatedUser);

        } finally {
            dataSource.close();
        }
    }
}

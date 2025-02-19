package edu.school21.chat.Models.Main;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ConnectionDB {

    private static final String DB_URL = Application.get("db.url");
    private static final String DB_LOGIN = Application.get("db.login");
    private static final String DB_PASSWORD = Application.get("db.password");

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        loadDriver();
        setupConfig();
        ds = new HikariDataSource(config);
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setupConfig() {
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_LOGIN);
        config.setPassword(DB_PASSWORD);
        config.setDriverClassName("org.postgresql.Driver");

        // Настройки пула соединений
        config.setMaximumPoolSize(10); // максимальное количество соединений
        config.setMinimumIdle(5); // минимальное количество неактивных соединений
        config.setConnectionTimeout(30000); // таймаут на получение соединения
        config.setIdleTimeout(600000); // время бездействия перед закрытием соединения
    }

    public static DataSource connectToDb() {
        return ds;
    }
}
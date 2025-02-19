package edu.school21.chat.Models.Main;

import java.sql.*;

public class Program {
    private static final String DB_URL = Application.get("db.url");
    private static final String DB_LOGIN = Application.get("db.login");
    private static final String DB_PASSWORD = Application.get("db.password");

    static {
        loadDriver();
    }

    private static void loadDriver() {
        // The driver registers itself when its class is loaded
        // so we just need to reference the class
        try {
            Class<?> clazz = Class.forName("org.postgresql.Driver", true, Program.class.getClassLoader());
            clazz.getClassLoader(); // to avoid class unloading
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASSWORD)) {
            try (PreparedStatement userStatement = connection.prepareStatement(
                "SELECT u.userid, u.login, cr.chatroomname " +
                "FROM Chat.User u " +
                "LEFT JOIN Chat.ChatRoom cr ON cr.chatroomowner = u.userid"
            )) {
                try (ResultSet resultSet = userStatement.executeQuery()) {
                    boolean hasResults = false;
                    while (resultSet.next()) {
                        hasResults = true;
                        System.out.println(
                            "User ID: " + resultSet.getString("userid") +
                            ", Login: " + resultSet.getString("login") +
                            ", Owns Room: " + resultSet.getString("chatroomname")
                        );
                    }
                    if (!hasResults) {
                        System.out.println("No results found in the database.");
                    }
                }
            }
        } catch (SQLException sqlException) {
            System.out.println("Database error occurred:");
            System.out.println("Error message: " + sqlException.getMessage());
            System.out.println("SQL State: " + sqlException.getSQLState());
            System.out.println("Error code: " + sqlException.getErrorCode());
            sqlException.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred:");
            e.printStackTrace();
        }
    }
}
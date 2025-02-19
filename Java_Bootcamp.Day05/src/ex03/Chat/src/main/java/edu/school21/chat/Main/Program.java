package edu.school21.chat.Main;

import edu.school21.chat.Chat.Message;
import edu.school21.chat.Repository.MessagesRepository;
import edu.school21.chat.Repository.MessagesRepositoryJdbcImpl;

import java.sql.*;
import java.util.Optional;

public class Program {
    public static void main(String[] args) {
        try {
            // Сначала выведем список всех сообщений
            Connection connection = DataSource.getDataSourceInstance().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT messageid, messagetext FROM chat.message ORDER BY messageid");
            System.out.println("Available messages in database:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("messageid") + ", Text: " + rs.getString("messagetext"));
            }
            System.out.println("------------------------");

            // Теперь попробуем обновить первое сообщение
            MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(DataSource.getDataSourceInstance());
            Optional<Message> messageOptional = messagesRepository.findById(1L);
            
            if (messageOptional.isPresent()) {
                Message message = messageOptional.get();
                System.out.println("Message before update: " + message);
                
                message.setText("Updated");
                message.setDateTime(null);
                messagesRepository.update(message);
                
                // Проверяем обновление
                Optional<Message> updatedMessage = messagesRepository.findById(1L);
                if (updatedMessage.isPresent()) {
                    System.out.println("Message after update: " + updatedMessage.get());
                }
            } else {
                System.out.println("Message with ID 1 not found");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
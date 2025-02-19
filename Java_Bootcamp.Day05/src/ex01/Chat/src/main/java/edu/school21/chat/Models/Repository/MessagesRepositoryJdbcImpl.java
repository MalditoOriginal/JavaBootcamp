package edu.school21.chat.Models.Repository;

import edu.school21.chat.Models.Chat.ChatRoom;
import edu.school21.chat.Models.Chat.Message;
import edu.school21.chat.Models.Chat.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private final DataSource dataSource;

    public MessagesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Message> findById(Long id) throws SQLException {
        final String messageQuery = "SELECT m.*, " +
                                  "u.login as author_login, u.password as author_password, " +
                                  "c.chatroomname, c.chatroomowner " +
                                  "FROM chat.message m " +
                                  "JOIN chat.user u ON m.messageauthor = u.userid " +
                                  "JOIN chat.chatroom c ON m.messageroom = c.chatroomid " +
                                  "WHERE m.messageid = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(messageQuery)) {
            
            statement.setLong(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                
                User author = new User(
                    resultSet.getLong("messageauthor"),
                    resultSet.getString("author_login"),
                    resultSet.getString("author_password"),
                    new ArrayList<>(),
                    new ArrayList<>()
                );
                
                ChatRoom room = new ChatRoom(
                    resultSet.getLong("messageroom"),
                    resultSet.getString("chatroomname"),
                    resultSet.getString("author_login"),
                    new ArrayList<>()
                );
                
                Message message = new Message(
                    resultSet.getLong("messageid"),
                    author,
                    room,
                    resultSet.getString("messagetext"),
                    resultSet.getTimestamp("messagedate").toLocalDateTime()
                );
                
                return Optional.of(message);
            }
        }
    }

    private User findUserById(Long id) throws SQLException {
        final String userQuery = "SELECT * FROM chat.user WHERE userid = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(userQuery)) {
            
            statement.setLong(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                
                return new User(
                    resultSet.getLong("userid"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    new ArrayList<>(),
                    new ArrayList<>()
                );
            }
        }
    }

    private ChatRoom findRoomById(Long id) throws SQLException {
        final String roomQuery = "SELECT cr.*, u.login as owner_login FROM chat.chatroom cr " +
                               "JOIN chat.user u ON cr.chatroomowner = u.userid " +
                               "WHERE cr.chatroomid = ?";
        
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(roomQuery)) {
            
            statement.setLong(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                
                return new ChatRoom(
                    resultSet.getLong("chatroomid"),
                    resultSet.getString("chatroomname"),
                    resultSet.getString("owner_login"),
                    new ArrayList<>()
                );
            }
        }
    }
}

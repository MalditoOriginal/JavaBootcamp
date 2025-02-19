package edu.school21.chat.Repository;

import edu.school21.chat.Chat.ChatRoom;
import edu.school21.chat.Chat.Message;
import edu.school21.chat.Chat.User;
import edu.school21.chat.Exception.NotSavedSubEntityException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private final Connection connection;
    private static final String QUERY_USER = "SELECT * FROM chat.user WHERE userid = ?";
    private static final String QUERY_ROOM = "SELECT * FROM chat.chatroom WHERE chatroomid = ?";
    private static final String QUERY_MESSAGE = "SELECT * FROM chat.message WHERE messageid = ?";
    private static final String SQL_INSERT = "INSERT INTO chat.message (messageauthor, messageroom, messagetext, messagedate) VALUES (?, ?, ?, ?) RETURNING messageid";
    private static final String SQL_UPDATE = "UPDATE chat.message SET messageauthor=?, messageroom=?, messagetext=?, messagedate=? WHERE messageid=?";

    public MessagesRepositoryJdbcImpl(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }

    @Override
    public Optional<Message> findById(Long id) throws SQLException {
        try (PreparedStatement p = connection.prepareStatement(QUERY_MESSAGE)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? Optional.of(new Message(r.getLong("messageid"),
                        findUserById(r.getLong("messageauthor")),
                        findRoomById(r.getLong("messageroom")),
                        r.getString("messagetext"),
                        r.getTimestamp("messagedate").toLocalDateTime()))
                        : Optional.empty();
            }
        } catch (SQLException sqlException) {
            System.err.println(sqlException);
        }
        return Optional.empty();
    }

    private User findUserById(Long id) throws SQLException {
        try (PreparedStatement p = connection.prepareStatement(QUERY_USER)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new User(r.getLong("userid"),
                        r.getString("username"),
                        r.getString("password"),
                        new ArrayList<>(),
                        new ArrayList<>())
                        : null;
            }
        } catch (SQLException sqlException) {
            System.err.println(sqlException);
        }
        return null;
    }

    private ChatRoom findRoomById(Long id) throws SQLException {
        try (PreparedStatement p = connection.prepareStatement(QUERY_ROOM)) {
            p.setLong(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? new ChatRoom(r.getLong("chatroomid"),
                        r.getString("name"),
                        null,
                        new ArrayList<>())
                        : null;
            }
        } catch (SQLException sqlException) {
            System.err.println(sqlException);
        }
        return null;
    }

    @Override
    public void saveMessage(Message message) throws NotSavedSubEntityException {
        checkMessage(message);
        try (PreparedStatement p = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            p.setLong(1, message.getMessageOwner().getId());
            p.setLong(2, message.getChatRoom().getId());
            p.setString(3, message.getMessageText());
            p.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));

            int affectedRows = p.executeUpdate();
            if (affectedRows == 0) {
                throw new NotSavedSubEntityException("Failed to save the message, no rows affected.");
            }

            try (ResultSet generatedKeys = p.getGeneratedKeys()) {
                message.setId(generatedKeys.next() ? generatedKeys.getLong(1) : null);
            }
        } catch (SQLException sqlException) {
            System.err.println(sqlException);
        }
    }

    @Override
    public void updateMessage(Message message) {
        checkMessage(message);
        try (PreparedStatement p = connection.prepareStatement(SQL_UPDATE)) {
            p.setLong(1, message.getMessageOwner().getId());
            p.setLong(2, message.getChatRoom().getId());
            p.setString(3, message.getMessageText());
            p.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));
            p.setLong(5, message.getId());
            p.executeUpdate();
        } catch (SQLException sqlException) {
            System.err.println(sqlException);
        }
    }

    private void checkMessage(Message message) {
        if (message.getMessageOwner() == null) {
            throw new NotSavedSubEntityException("Owner hasn't been provided");
        }
        if (message.getChatRoom() == null) {
            throw new NotSavedSubEntityException("ChatRoom hasn't been provided");
        }
        if (message.getMessageText() == null || message.getMessageText().isEmpty()) {
            throw new NotSavedSubEntityException("Message text hasn't been provided");
        }
        if (message.getDateTime() == null) {
            throw new NotSavedSubEntityException("DateTime hasn't been provided");
        }

        try {
            if (findUserById(message.getMessageOwner().getId()) == null) {
                throw new NotSavedSubEntityException("User ID doesn't exist");
            }
            if (findRoomById(message.getChatRoom().getId()) == null) {
                throw new NotSavedSubEntityException("ChatRoom ID doesn't exist");
            }
        } catch (SQLException sqlException) {
            System.err.println(sqlException);
        }
    }
}

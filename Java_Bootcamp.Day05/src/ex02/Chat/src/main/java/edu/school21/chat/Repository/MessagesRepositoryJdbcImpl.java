package edu.school21.chat.Repository;

import edu.school21.chat.Chat.ChatRoom;
import edu.school21.chat.Chat.Message;
import edu.school21.chat.Chat.User;
import edu.school21.chat.Exception.NotSavedSubEntityException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
    private final Connection connection;
    private static final String FIND_MESSAGE_QUERY = 
            "SELECT m.*, u.login, u.password, c.chatroomname, c.chatroomowner " +
            "FROM chat.message m " +
            "JOIN chat.user u ON m.messageauthor = u.userid " +
            "JOIN chat.chatroom c ON m.messageroom = c.chatroomid " +
            "WHERE m.messageid = ?";
    private static final String SAVE_MESSAGE_QUERY = 
            "INSERT INTO chat.message(messageauthor, messageroom, messagetext, messagedate) " +
            "VALUES (?, ?, ?, ?) RETURNING messageid";
    private static final String CHECK_ENTITY_EXISTS_QUERY = 
            "SELECT EXISTS(SELECT 1 FROM chat.%s WHERE %sid = ?)";

    private final PreparedStatement findMessageStmt;
    private final PreparedStatement checkUserStmt;
    private final PreparedStatement checkChatRoomStmt;

    public MessagesRepositoryJdbcImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
        this.findMessageStmt = connection.prepareStatement(FIND_MESSAGE_QUERY);
        this.checkUserStmt = connection.prepareStatement(
                String.format(CHECK_ENTITY_EXISTS_QUERY, "user", "user"));
        this.checkChatRoomStmt = connection.prepareStatement(
                String.format(CHECK_ENTITY_EXISTS_QUERY, "chatroom", "chatroom"));
    }

    @Override
    public Optional<Message> findById(Long id) throws SQLException {
        findMessageStmt.setLong(1, id);
        try (ResultSet rs = findMessageStmt.executeQuery()) {
            if (rs.next()) {
                User author = new User(
                    rs.getLong("messageauthor"),
                    rs.getString("login"),
                    rs.getString("password"),
                    new ArrayList<>(),
                    new ArrayList<>()
                );
                
                User roomOwner = new User(
                    rs.getLong("chatroomowner"),
                    null, // Не загружаем лишние данные
                    null,
                    new ArrayList<>(),
                    new ArrayList<>()
                );
                
                ChatRoom room = new ChatRoom(
                    rs.getLong("messageroom"),
                    rs.getString("chatroomname"),
                    roomOwner,
                    new ArrayList<>()
                );
                
                return Optional.of(new Message(
                    rs.getLong("messageid"),
                    author,
                    room,
                    rs.getString("messagetext"),
                    rs.getTimestamp("messagedate").toLocalDateTime()
                ));
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveMessage(Message message) throws NotSavedSubEntityException {
        checkMessage(message);
        checkEntitiesExist(message);

        try (PreparedStatement stmt = connection.prepareStatement(SAVE_MESSAGE_QUERY)) {
            stmt.setLong(1, message.getMessageOwner().getId());
            stmt.setLong(2, message.getChatRoom().getId());
            stmt.setString(3, message.getMessageText());
            stmt.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    message.setId(rs.getLong(1));
                } else {
                    throw new NotSavedSubEntityException("Message could not be saved");
                }
            }
        } catch (SQLException e) {
            throw new NotSavedSubEntityException("Message could not be saved: " + e.getMessage());
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
            throw new NotSavedSubEntityException("Date time hasn't been provided");
        }
    }

    private void checkEntitiesExist(Message message) throws NotSavedSubEntityException {
        if (!checkEntityExists(checkUserStmt, message.getMessageOwner().getId())) {
            throw new NotSavedSubEntityException("User with ID " + message.getMessageOwner().getId() + " not found");
        }
        if (!checkEntityExists(checkChatRoomStmt, message.getChatRoom().getId())) {
            throw new NotSavedSubEntityException("ChatRoom with ID " + message.getChatRoom().getId() + " not found");
        }
    }

    private boolean checkEntityExists(PreparedStatement stmt, Long id) throws NotSavedSubEntityException {
        try {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new NotSavedSubEntityException("Error checking entity existence: " + e.getMessage());
        }
    }
}
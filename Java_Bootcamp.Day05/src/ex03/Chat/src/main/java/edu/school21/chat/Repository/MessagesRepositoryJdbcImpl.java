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
    private static final String UPDATE_MESSAGE_QUERY = 
            "UPDATE chat.message SET messageauthor = ?, messageroom = ?, messagetext = ?, messagedate = ? " +
            "WHERE messageid = ?";
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
                    null,
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
                    rs.getTimestamp("messagedate") != null ? 
                        rs.getTimestamp("messagedate").toLocalDateTime() : null
                ));
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Message message) throws NotSavedSubEntityException {
        if (message.getId() == null) {
            throw new NotSavedSubEntityException("Cannot update message without ID");
        }

        checkEntitiesExist(message);

        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_MESSAGE_QUERY)) {
            stmt.setLong(1, message.getMessageOwner().getId());
            stmt.setLong(2, message.getChatRoom().getId());
            setStringOrNull(stmt, 3, message.getText());
            setTimestampOrNull(stmt, 4, message.getDateTime());
            stmt.setLong(5, message.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new NotSavedSubEntityException("Message with ID " + message.getId() + " not found");
            }
        } catch (SQLException e) {
            throw new NotSavedSubEntityException("Failed to update message: " + e.getMessage());
        }
    }

    private void checkEntitiesExist(Message message) throws NotSavedSubEntityException {
        if (message.getMessageOwner() == null || message.getMessageOwner().getId() == null) {
            throw new NotSavedSubEntityException("Message owner or owner ID is null");
        }
        if (message.getChatRoom() == null || message.getChatRoom().getId() == null) {
            throw new NotSavedSubEntityException("Chat room or room ID is null");
        }
        
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

    private void setStringOrNull(PreparedStatement stmt, int parameterIndex, String value) throws SQLException {
        if (value == null) {
            stmt.setNull(parameterIndex, Types.VARCHAR);
        } else {
            stmt.setString(parameterIndex, value);
        }
    }

    private void setTimestampOrNull(PreparedStatement stmt, int parameterIndex, java.time.LocalDateTime dateTime) 
            throws SQLException {
        if (dateTime == null) {
            stmt.setNull(parameterIndex, Types.TIMESTAMP);
        } else {
            stmt.setTimestamp(parameterIndex, Timestamp.valueOf(dateTime));
        }
    }
}
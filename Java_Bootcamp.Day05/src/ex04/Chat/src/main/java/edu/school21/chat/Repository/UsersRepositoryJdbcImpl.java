package edu.school21.chat.Repository;

import edu.school21.chat.Chat.ChatRoom;
import edu.school21.chat.Chat.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final Connection connection;
    private static final String SQL_QUERY = """
            WITH PagedUsers AS (
                SELECT *
                FROM Chat.User
                ORDER BY userid
                LIMIT ? OFFSET ?
            )
            SELECT DISTINCT 
                u.userid, 
                u.login, 
                u.password,
                cr.chatroomid,
                cr.chatroomname,
                cr.chatroomowner
            FROM PagedUsers u
            LEFT JOIN Chat.user_chatrooms uc ON u.userid = uc.userid
            LEFT JOIN Chat.ChatRoom cr ON uc.chatroomid = cr.chatroomid
            ORDER BY u.userid, cr.chatroomid""";

    public UsersRepositoryJdbcImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    @Override
    public List<User> findAll(int page, int size) throws SQLException {
        List<User> users = new ArrayList<>();
        List<ChatRoom> chatRooms = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {
            int offset = page * size;
            preparedStatement.setInt(1, size);
            preparedStatement.setInt(2, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = users.stream()
                            .filter(u -> {
                                try {
                                    return u.getId().equals(resultSet.getLong("userid"));
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .findFirst()
                            .orElseGet(() -> {
                                try {
                                    User u = new User(
                                            resultSet.getLong("userid"),
                                            resultSet.getString("login"),
                                            resultSet.getString("password"),
                                            new ArrayList<>(),
                                            new ArrayList<>()
                                    );
                                    users.add(u);
                                    return u;
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                    ChatRoom chatRoom = chatRooms.stream()
                            .filter(r -> {
                                try {
                                    return r.getId().equals(resultSet.getLong("chatroomid"));
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .findFirst()
                            .orElseGet(() -> {
                                try {
                                    ChatRoom r = new ChatRoom(
                                            resultSet.getLong("chatroomid"),
                                            resultSet.getString("chatroomname"),
                                            user,
                                            null
                                    );
                                    chatRooms.add(r);
                                    return r;
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                    if (user.getId().equals(resultSet.getLong("chatroomowner"))) {
                        user.getCreatedRooms().add(chatRoom);
                    }

                    user.getSocializeRooms().add(chatRoom);
                }
            }
            return users;
        }
    }
}
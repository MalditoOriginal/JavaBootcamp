package edu.school21.chat.Main;

import edu.school21.chat.Chat.User;
import edu.school21.chat.Repository.UsersRepositoryJdbcImpl;
import java.sql.*;
import java.util.List;

public class Program {

    public static void main(String[] args) throws SQLException {
        List<User> users = new UsersRepositoryJdbcImpl(DataSource.getDataSource()).findAll(0, 6);
        users.forEach(user -> System.out.println(user + "\n"));
    }

}
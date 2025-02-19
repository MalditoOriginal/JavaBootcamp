package edu.school21.chat.Main;

import edu.school21.chat.Chat.ChatRoom;
import edu.school21.chat.Chat.Message;
import edu.school21.chat.Chat.User;
import edu.school21.chat.Exception.NotSavedSubEntityException;
import edu.school21.chat.Repository.MessagesRepository;
import edu.school21.chat.Repository.MessagesRepositoryJdbcImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static edu.school21.chat.Main.DataSource.*;

public class Program {
    public static void main(String[] args) throws SQLException {
        MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(getDataSource());

        User creator = new User(1L, "Alex", "1111", new ArrayList<>(), new ArrayList<>());
        ChatRoom room = new ChatRoom(1L, "1", creator, new ArrayList<>());

        try {
            Message message = new Message(null, creator, room,  "Hello!", LocalDateTime.now());
            messagesRepository.saveMessage(message);
            System.out.println(message.getId()); // ex. id == 7
        }  catch (NotSavedSubEntityException none) {
            System.out.println(none);
        }

        try {
            Message message1 = new Message(null, creator, null, "Hello!", LocalDateTime.now());
            messagesRepository.saveMessage(message1);
            System.out.println(message1.getId()); // exception
        }  catch (NotSavedSubEntityException none) {
            System.out.println(none);
        }

        try {
            Message message2 = new Message(null, creator, room,  "Hello! I am Alex", LocalDateTime.now());
            messagesRepository.saveMessage(message2);
            System.out.println(message2.getId()); // ex. id == 9
        }  catch (NotSavedSubEntityException none) {
            System.out.println(none);
        }

        try {
            User creator2 = new User(10L, "Alex", "1111", new ArrayList<>(), new ArrayList<>());
            Message message2 = new Message(null, creator2, room,  "Hello! I am Alex", LocalDateTime.now());
            messagesRepository.saveMessage(message2);
            System.out.println(message2.getId()); // ex. id == 9
        }  catch (NotSavedSubEntityException none) {
            System.out.println(none);
        }
    }
}
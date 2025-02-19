package edu.school21.chat.Models.Main;

import edu.school21.chat.Models.Chat.Message;
import edu.school21.chat.Models.Repository.MessagesRepository;
import edu.school21.chat.Models.Repository.MessagesRepositoryJdbcImpl;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) throws SQLException {
        MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(ConnectionDB.connectToDb());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a message ID");
            System.out.print("-> ");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input.trim())) {
                break;
            }

            try {
                long id = Long.parseLong(input);
                Optional<Message> message = messagesRepository.findById(id);
                message.ifPresent(System.out::println);
            } catch (NumberFormatException e) {
                System.err.println("Error: Input is not a valid number");
            }
        }

        scanner.close();
    }
}
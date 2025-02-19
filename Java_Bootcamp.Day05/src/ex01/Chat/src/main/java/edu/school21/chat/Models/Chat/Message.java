package edu.school21.chat.Models.Chat;

import java.time.LocalDateTime;

public final class Message {
    private final Long id;
    private final User author;
    private final ChatRoom room;
    private final String text;
    private final LocalDateTime dateTime;

    public Message(Long id, User author, ChatRoom room, String text, LocalDateTime dateTime) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.text = text;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public ChatRoom getRoom() {
        return room;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Message message && id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Message : {%n" +
                           "  id=%d,%n" +
                           "  author=%s,%n" +
                           "  room=%s,%n" +
                           "  text=\"%s\",%n" +
                           "  dateTime=%s%n" +
                           "}", 
                           id, author, room, text, dateTime);
    }
}
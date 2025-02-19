package edu.school21.chat.Models.Chat;

import java.util.List;
import java.util.Objects;

public class ChatRoom {
    private final Long id;
    private final String chatName;
    private final String chatOwner;
    private final List<Message> messages;

    public ChatRoom(Long id, String chatName, String chatOwner, List<Message> messages) {
        this.id = id;
        this.chatName = chatName;
        this.chatOwner = chatOwner;
        this.messages = List.copyOf(messages);
    }

    public Long getId() {
        return id;
    }

    public String getChatName() {
        return chatName;
    }

    public String getChatOwner() {
        return chatOwner;
    }

    public List<Message> getMessages() {
        return List.copyOf(messages);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ChatRoom chatRoom && id.equals(chatRoom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Chat{" +
               "id=" + id +
               ", name='" + chatName + '\'' +
               ", creator='" + chatOwner + '\'' +
               '}';
    }
}

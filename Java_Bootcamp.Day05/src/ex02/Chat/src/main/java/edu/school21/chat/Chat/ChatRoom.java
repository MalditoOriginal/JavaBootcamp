package edu.school21.chat.Chat;

import java.util.ArrayList;

public class ChatRoom {
    final private long id;
    final private String chatName;
    final private User chatOwner;
    final private ArrayList<Message> messages;

    public ChatRoom(long id, String chatName, User chatOwner, ArrayList<Message> messages) {
        this.id = id;
        this.chatName = chatName;
        this.chatOwner = chatOwner;
        this.messages = messages;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ChatRoom && id == ((ChatRoom) o).id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChatRoom{id=" + id + ",name=" + chatName + ",owner=" + chatOwner + ",messages=" + messages.size() + "}";
    }
}

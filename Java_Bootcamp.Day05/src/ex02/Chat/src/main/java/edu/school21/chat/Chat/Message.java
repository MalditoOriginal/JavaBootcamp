package edu.school21.chat.Chat;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private final User messageOwner;
    private final ChatRoom chatRoom;
    private final String messageText;
    private final LocalDateTime dateTime;

    public Message(Long id, User messageOwner, ChatRoom chatRoom, String messageText, LocalDateTime dateTime) {
        this.id = id;
        this.messageOwner = messageOwner;
        this.chatRoom = chatRoom;
        this.messageText = messageText;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getMessageOwner() {
        return messageOwner;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public String getMessageText() {
        return messageText;
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
        return "Message : {" +
                "\n\tid=" + id +
                "\n\tauthor=" + messageOwner +
                "\n\troom=" + chatRoom +
                "\n\ttext=\"" + messageText + '\"' +
                "\n\tdateTime=" + dateTime +
                "\n}";
    }
}

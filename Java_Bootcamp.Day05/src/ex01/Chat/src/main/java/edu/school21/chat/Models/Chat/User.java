package edu.school21.chat.Models.Chat;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private final Long id;
    private String login;
    private String password;
    private final ArrayList<ChatRoom> createdRooms;
    private final ArrayList<ChatRoom> socializeRooms;

    public User(Long id, String login, String password, ArrayList<ChatRoom> createdRooms, ArrayList<ChatRoom> socializeRooms) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.createdRooms = Objects.requireNonNullElseGet(createdRooms, ArrayList::new);
        this.socializeRooms = Objects.requireNonNullElseGet(socializeRooms, ArrayList::new);
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<ChatRoom> getCreatedRooms() {
        return new ArrayList<>(createdRooms);
    }

    public ArrayList<ChatRoom> getSocializeRooms() {
        return new ArrayList<>(socializeRooms);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User user && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, login='%s', password='%s', createdRooms=%s, socializeRooms=%s}", 
            id, login, password, createdRooms, socializeRooms);
    }
}

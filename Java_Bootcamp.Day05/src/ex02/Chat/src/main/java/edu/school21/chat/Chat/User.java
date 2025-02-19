package edu.school21.chat.Chat;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private final Long id;
    private final String login;
    private final String password;
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
        return createdRooms;
    }

    public ArrayList<ChatRoom> getSocializeRooms() {
        return socializeRooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ",login='" + login + '\'' + ",password=***" + ",createdRooms=" + createdRooms.size() + ",socializeRooms=" + socializeRooms.size() + '}';
    }
}
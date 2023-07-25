package Models;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable {
    private String name;
    private String username;
    private String password;
    private LinkedList<Chat> chats = new LinkedList<>();
    private boolean isLoggedin = false;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // set username
    public void setName(String name) {
        this.name = name;
    }

    // Set the username
    public void setUsername(String username) {
        this.username = username;
    }

    // set password, Hash the password!
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public LinkedList<Chat> getChats() {
        return chats;
    }

    // set if logged
    public void setLoggedin(boolean isLoggedin) {
        this.isLoggedin = isLoggedin;
    }

    // get if loggedIn
    public boolean isLoggedin() {
        return this.isLoggedin;
    }

    public String getPassword() {
        return password;
    }

    public void setChats(Chat chat) {
        this.chats.add(chat);
    }

    @Override
    public String toString() {
        return String.format("");
    }

}

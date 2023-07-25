package Models;

public class Message {
    private User sender;
    private String text;

    public void setSender(User user) {
        this.sender = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return sender;
    }
}

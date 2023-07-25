package Models;

import java.io.Serializable;
import java.util.LinkedList;

public class Chat implements Serializable {
    private int chatId;
    private LinkedList<Message> messages = new LinkedList<>();
    private LinkedList<User> participants;

    public Chat(int chatId) {
        this.chatId = chatId;
    }

    public Chat(int chatId,Message message){
        this(chatId);
        this.messages.add(message);
    }

    // add message
    public void addMessage(Message message){
        this.messages.add(message);
    }

    public void addParticipant(User user){
        this.participants.add(user);
    }

    // get messages
    public LinkedList<Message> getMessages(){
        return this.messages;
    }
    public int getChatId() {
        return chatId;
    }
    public LinkedList<User> getParticipants() {
        return participants;
    }
}

import Models.Chat;
import Models.Message;
import Models.User;
import Generic.Repository;

public class Test {
    public static void main(String[] args) {
        var user = new User("Gilbert", "ngandu", "test1234");
        var user1 = new User("Joseph", "jose", "test12");

        var chat = new Chat(1); // Start a chat
        var message = new Message();
        var message2 = new Message();
        Repository<User> repo = new Repository<>();
        
        

        // sender
        message.setSender(user);
        message.setText("Hello, this Is Gilbert");

        // Receiver
        message2.setSender(user1);
        message2.setText("Hello, this Is Joseph");

        // Chat
        chat.addMessage(message);
        chat.addMessage(message2);


        user.setChats(chat);
        user1.setChats(chat);

        repo.add(user);

        // iterate to get the chats

        var iterator = chat.getMessages().iterator();
        while(iterator.hasNext()){
            var messageDetails = iterator.next();

            System.out.println("Name: "+messageDetails.getSender().getName()+"\nMessage: "+messageDetails.getText());
        }

    }
}

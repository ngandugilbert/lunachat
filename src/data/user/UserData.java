package data.user;

import Generic.IDatabase;
import Models.User;
import data.Authentication;

public class UserData implements IDatabase<User> {

    private User user;

    public User getUser() {
        return this.user;
    }

    @Override
    public User getAll() {
        return new User(); // This is a placeholder
    }

    @Override
    public User getSingleItem(int itemId) {
        // Collect and map userdata
        user = Authentication.getLoggedUser();


        return user; // This is a placeholder
    }

    @Override
    public User insertItem(User data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertItem'");
    }

}

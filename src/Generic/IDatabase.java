package Generic;

public interface IDatabase<T> {
    
    // get all items
    T getAll();

    // get an item using the Id
    T getSingleItem(int itemId);

    // insert item in the database
    T insertItem(T data);

}
